package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import edu.dartmouth.cs.pantryplanner.app.R;

import static android.app.Notification.VISIBILITY_PUBLIC;


public class EMAAlarmReceiver extends BroadcastReceiver {
    //Receive broadcas
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.d("received","ahahahah");
        Intent notificationIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Notification notification = builder.setContentTitle("Something in your pantry is going bad")
                .setContentText("")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.drawable.refrigerator)
                .setContentIntent(pendingIntent)
                .setVisibility(VISIBILITY_PUBLIC).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }


}