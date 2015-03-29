package com.busktimachu.icommute.icommute;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RouteUpdateCheckService extends IntentService {

    public static final String ACTION_CHECK_UPDATE = "com.busktimachu.icommute.icommute.action.CHECK_UPDATE";
    public static final String ACTION_DOWNLOAD_UPDATE = "com.busktimachu.icommute.icommute.action.DOWNLOAD_UPDATE";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.busktimachu.icommute.icommute.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.busktimachu.icommute.icommute.extra.PARAM2";

    private String logTag = "ICommute RouteUpdateCheckService";
    private SharedPreferences sharepref;
    private String prefile = "STOR_FILE";
    private String keyUid = "u_id";
    private String keyFileHash = "file_hash";
    private String uid;
    private String storedHash;
    private String server_url;

    /**
     * Starts this service to perform action startActionCheckUpdate with the given parameters. If
     * the service is already performing a sk this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionCheckUpdate(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RouteUpdateCheckService.class);
        intent.setAction(ACTION_CHECK_UPDATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionDownloadUpdate(Context context, String param1, String param2) {
        Intent intent = new Intent(context, RouteUpdateCheckService.class);
        intent.setAction(ACTION_DOWNLOAD_UPDATE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public RouteUpdateCheckService() {
        super("RouteUpdateCheckService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(logTag, "In onHandleIntent...service triggered");
        if (intent != null) {
            final String action = intent.getAction();
            Log.d(logTag, "Intent action:"+action);
            if (ACTION_CHECK_UPDATE.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                //final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionCheckUpdate(); //param1, param2);
            } else if (ACTION_DOWNLOAD_UPDATE.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                //final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                //handleActionDownloadUpdate(); //param1, param2);
            }
        }
        ICommuteAlarmReceiver.completeWakefulIntent(intent);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCheckUpdate() { //String param1, String param2) {
        Log.d(logTag, "In handleActionCheckUpdate");
        sharepref = getSharedPreferences(prefile,MODE_PRIVATE);
        uid = sharepref.getString(keyUid, "");
        storedHash = sharepref.getString(keyFileHash, "");
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(this);
        server_url = settingPref.getString(SettingsActivity.KEY_PREF_SERVER_ADDR, "");
        try {
            final String fileHash = downloadUrl(server_url + "/route_map/?u_id=" + uid);
            if (fileHash.contentEquals(storedHash)) {
                Log.d(logTag, "no updates from server...");
            } else {
                if (settingPref.getBoolean(SettingsActivity.KEY_PREF_UPDATE_NOTIFY,false)) {
                    NewRouteDataNotification notifynewfile = new NewRouteDataNotification();
                    notifynewfile.notify(this, "New updates available", 1);
                }
                else {
                    handleActionDownloadUpdate(fileHash);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleActionDownloadUpdate(String fileHash) {
        Log.d(logTag, "In handleActionDownloadUpdate");
        sharepref = getSharedPreferences(prefile,MODE_PRIVATE);
        uid = sharepref.getString(keyUid, "");
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(this);
        server_url = settingPref.getString(SettingsActivity.KEY_PREF_SERVER_ADDR, "");
        try {
            if (downloadFile(server_url + "/route_map/download/?u_id=" + uid)) {
                SharedPreferences.Editor edit = sharepref.edit();
                edit.putString(keyFileHash, fileHash);
                edit.apply();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String downloadUrl(String myUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            //setDoOutput doest POST req
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            getExternalFilesDir(null);
            int response = conn.getResponseCode();
            Log.d(logTag, "downloadurl response code is: " + response);
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            Log.d(logTag, "downloadurl response data: " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = null;
        String output = "";

        try {
            reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output += line;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return output;
    }

    private boolean downloadFile(String myUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(logTag, "downloadFile response code is: " + response);
            //String filename_and_disposition = conn.getHeaderField("Content-Disposition");
            is = conn.getInputStream();
            // Convert the InputStream into a string
            saveToFile(is);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch ( IOException e) {
            return false;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return true;
    }

    public void saveToFile(InputStream stream) throws IOException, UnsupportedEncodingException {
        byte[] buffer = new byte[1024];
        int bufferLength = 0;

        if (isExternalStorageWritable()) {
            File file = new File(getExternalFilesDir(null), "route_map.dat");
            OutputStream os = new FileOutputStream(file);

            try {
                while ((bufferLength = stream.read(buffer)) > 0) {
                    os.write(buffer, 0, bufferLength);
                }
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
