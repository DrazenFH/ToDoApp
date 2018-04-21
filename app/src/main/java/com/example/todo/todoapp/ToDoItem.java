package com.example.todo.todoapp;

/**
 * Created by dl_asus on 26.03.2018.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ToDoItem implements Serializable{
    private String mToDoText;
    private boolean mHasReminder;
    private UUID mTodoIdentifier;
    private Date mToDoDate;
    private ArrayList<String> assignedPersons;

public ToDoItem(){
    mTodoIdentifier = UUID.randomUUID();

}
    public ToDoItem(String todoBody, boolean hasReminder, Date toDoDate){
        mToDoText = todoBody;
        mHasReminder = hasReminder;
        mToDoDate = toDoDate;
        mTodoIdentifier = UUID.randomUUID();
    }

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

    public UUID getmTodoIdentifier() {
        return mTodoIdentifier;
    }

    public void setmTodoIdentifier(UUID mTodoIdentifier) {
        this.mTodoIdentifier = mTodoIdentifier;
    }

    public ArrayList<String> getAssignedPersons() {
        return assignedPersons;
    }

    public void setAssignedPersons(ArrayList<String> assignedPersons) {
        this.assignedPersons = assignedPersons;
    }

    public void addAssignedPerson(String person) {
        this.assignedPersons.add(person);
    }


    @Override
    public String toString() {
        return this.mToDoText;
    }
}