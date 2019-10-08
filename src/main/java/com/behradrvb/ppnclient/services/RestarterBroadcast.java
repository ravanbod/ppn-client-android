package com.behradrvb.ppnclient.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class RestarterBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("PPN", Context.MODE_PRIVATE);
        Log.e("PPN Connection", "RestarterBroadcast received broadcast...");
        if (sp.getBoolean("enable", false) && !ConnectionService.started) {
            Intent serviceIntent = new Intent(context, ConnectionService.class)
                    .putExtra("host", sp.getString("host", null))
                    .putExtra("port", sp.getInt("port", 25088));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                ContextCompat.startForegroundService(context, serviceIntent);
//            } else {
//                context.startService(serviceIntent);
//            }
            context.startService(serviceIntent);
        } else if (!sp.getBoolean("enable", false) && ConnectionService.started)
            context.stopService(new Intent(context, ConnectionService.class));
    }
}
