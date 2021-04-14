/**
 * @author Ong Jun Sen
 * @version 1.1
 @since 2021-04-06
 */

package com.ce2006.schoolconnect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This class is to start pulling data from database to check whether students is dismissed.
 */
public class BootReceiver extends BroadcastReceiver {

    /**
     *
     * @param context context for application to start service
     * @param intent is the object to run
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("com.ce2006.schoolconnect.NotificationService");
            context.startService(serviceIntent);
        }
    }
}

