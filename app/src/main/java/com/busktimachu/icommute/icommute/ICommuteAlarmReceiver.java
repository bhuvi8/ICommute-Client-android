package com.busktimachu.icommute.icommute;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by bhuvi on 28/3/15.
 */
public class ICommuteAlarmReceiver extends WakefulBroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private String logTag = "ICommute AlaramReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(logTag, "In onReceive...");
        Intent service = new Intent(context, RouteUpdateCheckService.class);
        startWakefulService(context,service);
    }

    public void setAlarm(Context context) {
        Log.d(logTag, "In setAlarm...");
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, RouteUpdateCheckService.class);
        intent.setAction("com.busktimachu.icommute.icommute.action.CHECK_UPDATE");
        context.startService(intent);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                60000,60000,
                alarmIntent);

        ComponentName receiver = new ComponentName(context,ICommuteBootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
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

