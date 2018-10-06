package com.nistores.awesomeurch.nistores.Folders.Helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nistores.awesomeurch.nistores.Folders.Pages.NotificationsActivity;
import com.nistores.awesomeurch.nistores.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Awesome Urch on 27/07/2018.
 * A class that utilizes all the methods frequently called in activity and fragment contexts
 */

public class Utility {
    public Context context;
    public SharedPreferences preferences;
    public Utility(Context c){
        context = c;
    }
    private String productsURL = "https://www.nistores.com.ng/api/src/routes/process_one.php?request=products";
    public String url2 = "https://www.nistores.com.ng/api/src/routes/process_user.php";
    public String getProductsURL(){
        return productsURL;
    }
    public String deliveryOrder_channelName = "Initiate Delivery";
    public String deliveryOrder_channelDesc = "Notifies you of the progress in initiating delivery";
    public String deliveryOrder_channelID = "channel_01";
    NotificationCompat.Builder mBuilder;
    public int NOTIF_INTERVAL = 1000 * 60 * 4; //4 mins

    public String bitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, 0);

        return imageString;
    }

    public String getSelectedCats(RecyclerView categoryRecycler){
        StringBuilder ret = new StringBuilder();
        int cnt = 0;
        for (int x = 0; x<categoryRecycler.getChildCount();x++){
            CheckBox cb = categoryRecycler.getChildAt(x).findViewById(R.id.name);
            TextView tv = categoryRecycler.getChildAt(x).findViewById(R.id.id);
            if(cb.isChecked()){
                String s = tv.getText().toString();
                String comma = (cnt>0)?",":"";
                ret.append(comma).append(s);
                cnt++;
                //Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
        return String.valueOf(ret);
    }

    public String getDeliveryOrder_channelDesc() {
        return deliveryOrder_channelDesc;
    }

    public String getDeliveryOrder_channelName() {
        return deliveryOrder_channelName;
    }

    public String getDeliveryOrder_channelID() {
        return deliveryOrder_channelID;
    }

    public void createNotification(String notifyId, String content){
        // Create an explicit intent for an Activity in your app
        Bundle bundle = new Bundle();
        bundle.putString("last_notification_id",notifyId);

        Intent intent = new Intent(context, NotificationsActivity.class);
        //intent.putExtra("last_notification_id",notifyId);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        int notificationId = Integer.parseInt(notifyId);
        String textTitle = "Nistores Notifications";
        mBuilder = new NotificationCompat.Builder(context, getDeliveryOrder_channelID())
                .setSmallIcon(R.drawable.ic_notification_color)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(textTitle)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setContentIntent(pendingIntent).setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());
        playSound();
    }

    public void playSound(){
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
