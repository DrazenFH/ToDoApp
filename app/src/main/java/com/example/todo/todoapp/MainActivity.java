package com.example.todo.todoapp;


import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private int mTheme = -1;
    private ListView mListView;
    private ArrayList<ToDoItem> mToDoItemsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getData();

        mTheme = R.style.CustomStyle_LightTheme;
        this.setTheme(mTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Liste erstellen
        mListView = (ListView) findViewById(R.id.recipe_list_view);
// 1



// 2
//        String[] listItems = new String[todoList.size()];
//// 3
//        for(int i = 0; i < todoList.size(); i++){
//            String todoTitle = todoList.get(i).getmToDoText();
//            listItems[i] = todoTitle;
//        }
// 4
     /*   ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
*/
    }
    @Override
    protected void onStart() {
        super.onStart();
        //Only for testing purpose
        mToDoItemsArrayList=new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,18);
        cal.set(Calendar.MINUTE, 51);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        Date d = cal.getTime();
        System.out.println("This is the date-->"+d);
        ToDoItem item=new ToDoItem("Test1", true, d);

        item.setmToDoDate(d);
        mToDoItemsArrayList.add(item);
        System.out.println("This is the item-->"+item.getmToDoDate());
        setAlarms();

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

    public void getData(){
        ArrayList<ToDoItem> listItem = new ArrayList<>();
        FirebaseDB db = new FirebaseDB();
        db.readData();
        listItem = db.getItemList();
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
        Intent intent = new Intent(this, AddTodoActivity.class);
        startActivity(intent);
    }

}
