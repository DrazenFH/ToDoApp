package com.example.todo.todoapp;

/**
 * Created by dl_asus on 26.03.2018.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ToDoItem implements Serializable{
    private String mToDoText;
    private boolean mHasReminder;

    public String getmToDoText() {
        return mToDoText;
    }

    public void setmToDoText(String mToDoText) {
        this.mToDoText = mToDoText;
    }

    public boolean ismHasReminder() {
        return mHasReminder;
    }

    public void setmHasReminder(boolean mHasReminder) {
        this.mHasReminder = mHasReminder;
    }

    public Date getmToDoDate() {
        return mToDoDate;
    }

    public void setmToDoDate(Date mToDoDate) {
        this.mToDoDate = mToDoDate;
    }


    private Date mToDoDate;


public ToDoItem(){

}


}