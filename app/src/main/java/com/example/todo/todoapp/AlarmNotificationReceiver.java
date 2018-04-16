package com.example.todo.todoapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.UUID;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by w7pro on 16.04.2018.
 */

public class AlarmNotificationReceiver extends IntentService {

    public static final String TODOTEXT = "todotext";
    public static final String TODOUUID = "todoid";
    private String mTodoText;
    private UUID mTodoUUID;
    private Context mContext;

    public AlarmNotificationReceiver() {
        super("AlarmNotificationReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoUUID = (UUID) intent.getSerializableExtra(TODOUUID);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTodoText)
                .setSmallIcon(R.drawable.ic_done_white_24dp)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();

        manager.notify(100, notification);

    }
}
