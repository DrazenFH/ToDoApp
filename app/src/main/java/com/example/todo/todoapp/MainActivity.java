package com.example.todo.todoapp;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnFireBaseDataChanged{
    private int mTheme = -1;
    private ListView mListView;
    private ArrayList<ToDoItem> mToDoItemsArrayList;
    private NetworkChangeReceiver networkChangeReceiver = null;
    private DatabaseReference db;
    private FirebaseDB helper;
    private ArrayAdapter<ToDoItem> adapter;
    private static MainActivity instance;
    private boolean connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance = this;
        connection=true;
        mTheme = R.style.CustomStyle_LightTheme;
        this.setTheme(mTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Liste erstellen
        mListView = (ListView) findViewById(R.id.recipe_list_view);

        db= FirebaseDatabase.getInstance().getReference();
        helper=new FirebaseDB(db, this);

        //ADAPTER
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,helper.retrieve());

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToDoItem item = (ToDoItem) adapter.getItem(i);
                System.out.println(item.getmToDoText());
                startDetailActivity(item);
                //based on item add info to intent
            }

        });
        setAlarms();
    }

    private void startDetailActivity(ToDoItem item) {
        Intent i = new Intent(this, DetailTodoActivity.class);

        i.putExtra("TodoItemClicked",item);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();

        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        // Set broadcast receiver priority.
        intentFilter.setPriority(100);

        // Create a network change broadcast receiver.
        networkChangeReceiver = new NetworkChangeReceiver();

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(networkChangeReceiver, intentFilter);
    }



    private void setAlarms(){
            System.out.println("Setting alarms");
            System.out.println("Size "+mListView.getAdapter().getCount());
            for(int i=0; i<mListView.getAdapter().getCount();i++){
                ToDoItem item=(ToDoItem) mListView.getAdapter().getItem(i);
                System.out.println("Todoitem alarm "+item.getmToDoText());
                System.out.println("Todoitem alarm "+item.getmToDoDate());
                if(item.ismHasReminder() && item.getmToDoDate()!=null){
                    if(item.getmToDoDate().before(new Date())){
                        item.setmToDoDate(null);
                        continue;
                    }
                    Intent intent = new Intent(this, AlarmNotificationReceiver.class);
                    intent.putExtra(AlarmNotificationReceiver.TODOUUID, item.getmTodoIdentifier());
                    intent.putExtra(AlarmNotificationReceiver.TODOTEXT, item.getmToDoText());
                    createAlarm(intent, item.getmTodoIdentifier().hashCode(), item.getmToDoDate().getTime());
                }
        }
    }

    private void createAlarm(Intent i, int requestCode, long timeInMillis){
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(this,requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println("setting alarm");
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);

    }



    private AlarmManager getAlarmManager(){
        return (AlarmManager)getSystemService(ALARM_SERVICE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMeMenuItem:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;

            /*case R.id.preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewItem(View view){
        if(connection) {
            Intent intent = new Intent(this, AddTodoActivity.class);
            startActivity(intent);
        }
        else{
            new Alert(this,"No Network connections are available!","Can't connect to Database!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // If the broadcast receiver is not null then unregister it.
        // This action is better placed in activity onDestroy() method.
        if(this.networkChangeReceiver!=null) {
            unregisterReceiver(this.networkChangeReceiver);
        }
    }

    @Override
    public void dataChanged() {
        adapter.notifyDataSetChanged();

    }
    public void setSingleAlarm(ToDoItem item){
        Intent intent = new Intent(this, AlarmNotificationReceiver.class);
        intent.putExtra(AlarmNotificationReceiver.TODOUUID, item.getmTodoIdentifier());
        intent.putExtra(AlarmNotificationReceiver.TODOTEXT, item.getmToDoText());
        createAlarm(intent, item.getmTodoIdentifier().hashCode(), item.getmToDoDate().getTime());
        System.out.println("Setting single Alarm "+item.getmToDoDate());
    }
    public static MainActivity getInstace(){
        return instance;
    }
    public void disableAddButton(){
        connection=false;
    }
    public void enableAddButton(){
       connection=true;
    }


}
