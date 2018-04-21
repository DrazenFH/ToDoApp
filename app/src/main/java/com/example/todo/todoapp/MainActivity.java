package com.example.todo.todoapp;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    }
    @Override
    protected void onStart() {
        super.onStart();
        //Only for testing purpose
        mToDoItemsArrayList=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,14);
        cal.set(Calendar.MINUTE, 04);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        Date d = cal.getTime();
        System.out.println("This is the date-->"+d);
        ToDoItem item=new ToDoItem("Test1", true, d);

        item.setmToDoDate(d);
        mToDoItemsArrayList.add(item);
        System.out.println("This is the item-->"+item.getmToDoDate());
        setAlarms();



        //TODO: Checks if network connection is available
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
        if(mToDoItemsArrayList!=null){
            for(ToDoItem item : mToDoItemsArrayList){
                if(item.ismHasReminder() && item.getmToDoDate()!=null){
                    if(item.getmToDoDate().before(new Date())){
                        item.setmToDoDate(null);
                        continue;
                    }
                    Intent i = new Intent(this, AlarmNotificationReceiver.class);
                    i.putExtra(AlarmNotificationReceiver.TODOUUID, item.getmTodoIdentifier());
                    i.putExtra(AlarmNotificationReceiver.TODOTEXT, item.getmToDoText());
                    createAlarm(i, item.getmTodoIdentifier().hashCode(), item.getmToDoDate().getTime());
                }
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

  /*  public void getData(){
        ArrayList<ToDoItem> listItem = new ArrayList<>();
        FirebaseDB db = new FirebaseDB();
        db.readData();
        listItem = db.getItemList();
    }
*/

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
            new Alert(this);
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
