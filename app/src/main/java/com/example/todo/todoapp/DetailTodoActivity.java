package com.example.todo.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;

/**
 * Created by dl_asus on 23.04.2018.
 */

public class DetailTodoActivity extends AppCompatActivity {

    private TextView title;
    private TextView date;
    private TextView time;
    private TextView contacts;
    private TextView reminder;
    private TextView place;
    private DatabaseReference db;
    private FirebaseDB helper;
    private Toolbar toolbar;

    //shows detail of the choosen Todo on the mainpage, save when entry is changed (remove or update)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseDB(db);

        setContentView(R.layout.activity_todo_details);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent i = getIntent();
        ToDoItem item = (ToDoItem) i.getSerializableExtra("TodoItemClicked");

        title = findViewById(R.id.editTextTitle);
        title.setText(item.getTodoTxt());
        if (item.getTodoDate() != null) {
            date = findViewById(R.id.editTextDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date.setText(simpleDateFormat.format(item.getTodoDate()));

            time = findViewById(R.id.editTextTime);
            simpleDateFormat = new SimpleDateFormat("hh:mm");
            time.setText(simpleDateFormat.format(item.getTodoDate()));
        }
        reminder = findViewById(R.id.editTextReminder);
        reminder.setText(String.valueOf(item.isHasReminder()));

        if (item.getAssignedPersons() != null && item.getAssignedPersons().size() > 0) {
            String persons = new String();
            for (String person : item.getAssignedPersons()) {
                persons += person + ",\n";
            }
            contacts = findViewById(R.id.editTextContacts);
            contacts.setText(persons);
        }
        if(item.getPlace()!=null&&item.getPlace().length()>0){
            place = findViewById(R.id.editTextPlace);
            place.setText(item.getPlace());
        }

    }

    public void removeItem(View view) {
        helper.removeItem((ToDoItem) getIntent().getSerializableExtra("TodoItemClicked"));
        finish();
    }

    public void setAsDone(View view) {
        helper.updateDone((ToDoItem) getIntent().getSerializableExtra("TodoItemClicked"));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(this) != null) {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}