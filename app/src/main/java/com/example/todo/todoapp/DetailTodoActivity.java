package com.example.todo.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by dl_asus on 23.04.2018.
 */

public class DetailTodoActivity extends AppCompatActivity {

    private TextView title;
    private TextView date;
    private TextView contacts;
    private TextView reminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_details);
        Intent i=getIntent();
        ToDoItem item=(ToDoItem)i.getSerializableExtra("TodoItemClicked");

        title=findViewById(R.id.editTextTitle);
        title.setText(item.getmToDoText());

        date=findViewById(R.id.editTextDate);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        date.setText(simpleDateFormat.format(item.getmToDoDate()));

        reminder=findViewById(R.id.editTextReminder);
        reminder.setText(String.valueOf(item.ismHasReminder()));

        if(item.getAssignedPersons()!=null&&item.getAssignedPersons().size()>0) {
            String persons = new String();
            for (String person : item.getAssignedPersons()) {
                persons += person + ",\n";
            }
            contacts = findViewById(R.id.editTextContacts);
            contacts.setText(persons);
        }

    }

    public void removeItem(View view) {
    }

    public void setAsDone(View view) {
    }
}
