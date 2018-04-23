package com.example.todo.todoapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import java.util.UUID;

/**
 * Created by w7pro on 16.04.2018.
 */

public class AlarmNotificationReceiver extends IntentService {

    public static final String TODOTEXT = "todotext";
    public static final String TODOUUID = "todoid";
    public static final String TODOCONTENT = "todocontent";
    private String mTodoText;
    private String mTodoContent;
    private UUID mTodoUUID;
    private Context mContext;

    public AlarmNotificationReceiver() {
        super("AlarmNotificationReceiver");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mTodoText = intent.getStringExtra(TODOTEXT);
        mTodoContent = intent.getStringExtra(TODOCONTENT);
        mTodoUUID = (UUID) intent.getSerializableExtra(TODOUUID);


        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(mTodoText)
                .setContentText(mTodoContent)
                .setSmallIcon(R.drawable.ic_done_white_24dp)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
System.out.println("TODOCONTENT="+mTodoContent);
        manager.notify(100, notification);

    }
}
