package com.nistores.awesomeurch.nistores.Folders.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

// WakefulBroadcastReceiver ensures the device does not go back to sleep
// during the startup of the service
public class BootBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Launch the specified service when this message is received
        Intent startServiceIntent = new Intent(context, MyNotificationService.class);
        startWakefulService(context, startServiceIntent);
    }
}