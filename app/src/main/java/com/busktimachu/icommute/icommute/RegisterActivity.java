package com.busktimachu.icommute.icommute;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class RegisterActivity extends ActionBarActivity {
    public static final String UNIQUE_ID = "com.busktimachu.icommute.icommute.UID";
    public static final String S_URL = "com.busktimachu.icommute.icommute.URL";

    private EditText empnbr;
    private Button register;
    private CheckBox agree;
    private final String logTag = "iCommute";
    private String serverUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //setupActionBar();
        Log.d(logTag, "in method onCreate");
        serverUrl = getIntent().getStringExtra(S_URL);
        if (serverUrl != null) {
            Log.d(logTag, "server address received:"+serverUrl);
        }
        else{
            Log.e(logTag, "Server address not found");
        }
        agree = (CheckBox) findViewById(R.id.checkBox);
        register = (Button) findViewById(R.id.button);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(RegisterActivity.this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user clicks the register button
     */
    public void register(View view) {
        Log.d(logTag, "in method register");
        if (agree.isChecked()) {
            empnbr = (EditText) findViewById(R.id.editText);
            String empno = empnbr.getText().toString();
            Log.d(logTag, "in method register : emp no =" + empno);
            if (empno.isEmpty())
                Toast.makeText(getApplicationContext(), "Enter your employee number", Toast.LENGTH_SHORT).show();
            else {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Registering...", Toast.LENGTH_SHORT).show();
                    new RegisterAppTask().execute("http://192.168.1.3:8080/?emp_id="+empno);
                } else {
                    // display error
                    Toast.makeText(getApplicationContext(), "Could not connect to network, check your internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Agree to Terms & Conditions", Toast.LENGTH_SHORT).show();
        }
    }

    private class RegisterAppTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "FAILED";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.contentEquals("FAILED")){
                Toast.makeText(getApplicationContext(), "Unable to connect to server at this time, please check back later", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Device registered successfully", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra(UNIQUE_ID,result);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
            Log.d(logTag, "result of n/w op:" + result);
        }

        private String downloadUrl(String myUrl) throws IOException {
            InputStream is = null;
            int len = 16;

            try {
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(logTag, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
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
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

    }
}


