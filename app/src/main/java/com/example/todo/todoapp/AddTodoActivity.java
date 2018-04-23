package com.example.todo.todoapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by dl_asus on 26.03.2018.
 */

public class AddTodoActivity extends AppCompatActivity implements
        View.OnClickListener {

    private Button btnDatePicker, btnTimePicker;
    private ListView contactList;
    private EditText title;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ToDoItem toDoItem;
    private FloatingActionButton mToDoSendFloatingActionButton;
    private SwitchCompat reminderSwitch;
    private static final int PICK_CONTACT = 1000;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> contactsTempList;
    //TODO: add person list for todoitem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        btnDatePicker=(Button)findViewById(R.id.newToDoChooseDateButton);
        btnTimePicker=(Button)findViewById(R.id.newToDoChooseTimeButton);
        title=(EditText) findViewById(R.id.editText);
        reminderSwitch = (SwitchCompat)findViewById(R.id.toDoHasDateSwitchCompat);
        contactList=(ListView) findViewById(R.id.newToDoDateTimeReminderTextView);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        contactsTempList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(this, R.layout.list_item, R.id.txtitem, contactsTempList);
        contactList.setAdapter(arrayAdapter);



    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear=year;
                            mMonth=monthOfYear;
                            mDay=dayOfMonth;
                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mHour=hourOfDay;
                            mMinute=minute;
                            btnTimePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();

        }
    }

    public void addToDo(View view){
        if(title.getText().length()!=0) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            FirebaseDB helper = new FirebaseDB(db);

            ToDoItem newItem = new ToDoItem();

            String todoTitle = title.getText().toString();
            boolean reminder = reminderSwitch.isChecked();
            newItem.setmToDoText(todoTitle);
            newItem.setmHasReminder(reminder);
            newItem.setAssignedPersons(contactsTempList);


            // get Date and Time from Pickers
            if(reminder) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, mHour);
                cal.set(Calendar.MINUTE, mMinute);
                cal.set(Calendar.SECOND, 0);

                cal.set(Calendar.DAY_OF_MONTH, mDay);
                cal.set(Calendar.MONTH, mMonth);
                cal.set(Calendar.YEAR, mYear);
                Date date = cal.getTime();
                newItem.setmToDoDate(date);
            }

            if (helper.save(newItem) ) {
                if(newItem.ismHasReminder()) {
                    MainActivity.getInstace().setSingleAlarm(newItem);
                }
            } else {
                new Alert(this, "An Error occurred", "Can't connect to Database!");
            }
            finish();
        }
    }

        public void pickAContactNumber(View view) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor phone = getContentResolver().query(contactData, null, null, null, null);
                    if (phone.moveToFirst()) {
                        String contactNumberName = phone.getString(phone.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        //Todo Add Person to Personlist from TODOItem
                        System.out.println("Added Person: " + contactNumberName);
                        if(!contactsTempList.contains(contactNumberName)) {


                            contactsTempList.add(contactNumberName);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
        }
    }
}


