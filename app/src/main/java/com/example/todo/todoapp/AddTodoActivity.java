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
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
    private EditText address;
    private EditText title;
    private int year, month, day, hour, minute;
    private FloatingActionButton mToDoSendFloatingActionButton;
    private SwitchCompat reminderSwitch;
    private static final int PICK_CONTACT = 1000;
    private ArrayAdapter<String> arrayAdapter;
    private String placeToAdd;

   private static final int PLACE_PICKER_REQ_CODE = 1;
    private ArrayList<String> contactsTempList;

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
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            AddTodoActivity.this.year =year;
                            month =monthOfYear;
                            day =dayOfMonth;
                            btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);

            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            hour =hourOfDay;
                            AddTodoActivity.this.minute =minute;
                            btnTimePicker.setText(hourOfDay + ":" + minute);
                        }
                    }, hour, minute, false);
            timePickerDialog.show();


        }
    }


    //save added ToDO to the Database
    public void addToDo(View view){
        if(title.getText().length()!=0) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            FirebaseDB helper = new FirebaseDB(db);

            ToDoItem newItem = new ToDoItem();

            String todoTitle = title.getText().toString();
            boolean reminder = reminderSwitch.isChecked();
            newItem.setTodoTxt(todoTitle);
            newItem.setHasReminder(reminder);
            newItem.setAssignedPersons(contactsTempList);

            // get Date and Time from Pickers
                if(newItem.isHasReminder()) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.HOUR_OF_DAY, hour);
                    cal.set(Calendar.MINUTE, minute);
                    cal.set(Calendar.SECOND, 0);

                    cal.set(Calendar.DAY_OF_MONTH, day);
                    cal.set(Calendar.MONTH, month);
                    cal.set(Calendar.YEAR, year);
                    Date date = cal.getTime();
                    newItem.setTodoDate(date);
                }
                if(placeToAdd!=null&&placeToAdd.length()>0) {
                    newItem.setPlace(placeToAdd);
                }

            if (helper.save(newItem) ) {
                if(newItem.isHasReminder()) {
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

        //gets the result of the picker
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
                        break;
                    }
                }case(PLACE_PICKER_REQ_CODE):

                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    if(place.getAddress()!=null) {
                        placeToAdd=place.getAddress().toString();
                        address=findViewById(R.id.placeTextArea);
                        address.setText(place.getAddress().toString());

                    }
                }
                break;
        }

    }

    public void pickPlace(View view) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


