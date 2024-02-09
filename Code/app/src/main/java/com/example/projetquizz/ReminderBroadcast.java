package com.example.projetquizz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * La class pour l'envoie de notification.
 */
public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String txt = context.getString(R.string.descNotif);

        //Crée une notification.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notificationRocketQuizz")
                .setSmallIcon(R.drawable.coeur)
                .setContentTitle("Rocket Quizz")
                .setContentText(txt)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
