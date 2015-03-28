package com.busktimachu.icommute.icommute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ICommuteBootReceiver extends BroadcastReceiver {

    ICommuteAlarmReceiver alarm = new ICommuteAlarmReceiver();
    public String logTag = "ICommute BootReceiver";

    //public ICommuteBootReceiver() {
    //}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(logTag, "In onReceive...received boot complete broadcast");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d(logTag, "Inside boot complete intent if cond");
            alarm.setAlarm(context);
        }
    }
}
