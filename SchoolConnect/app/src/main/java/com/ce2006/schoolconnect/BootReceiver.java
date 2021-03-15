package com.ce2006.schoolconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("com.ce2006.schoolconnect.NotificationService");
            context.startService(serviceIntent);
        }
    }
}

