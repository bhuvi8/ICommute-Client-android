package com.busktimachu.icommute.icommute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by bhuvi on 28/3/15.
 */
public class ICommuteAlarmReceiver extends WakefulBroadcastReceiver {

    public static final String SYNC_DISABLED = "-1";
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private int sync_freq;

    private String logTag = "ICommute AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(logTag, "In onReceive...");
        Intent service = new Intent(context, RouteUpdateCheckService.class);
        service.setAction(RouteUpdateCheckService.ACTION_CHECK_UPDATE);
        startWakefulService(context,service);
    }

    public void setAlarm(Context context) {
        Log.d(logTag, "In setAlarm...");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ICommuteAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        SharedPreferences settingPref = PreferenceManager.getDefaultSharedPreferences(context);
        sync_freq = Integer.parseInt(settingPref.getString(SettingsActivity.KEY_PREF_UPDATE_CHECK_FREQ, SYNC_DISABLED));
        if (sync_freq != -1) {

            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    AlarmManager.INTERVAL_HOUR * sync_freq, AlarmManager.INTERVAL_HOUR * sync_freq,
                    alarmIntent);

            ComponentName receiver = new ComponentName(context, ICommuteBootReceiver.class);
            PackageManager pm = context.getPackageManager();
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    public void cancelAlarm(Context context) {
        Log.d(logTag, "in cancelAlarm...");
        if (alarmMgr!= null) {
            alarmMgr.cancel(alarmIntent);
        }

        ComponentName receiver = new ComponentName(context,ICommuteBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }
}

