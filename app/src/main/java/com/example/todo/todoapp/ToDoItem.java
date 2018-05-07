package com.example.todo.todoapp;

/**
 * Created by dl_asus on 26.03.2018.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ToDoItem implements Serializable{
    private String todoTxt;
    private boolean hasReminder;
    private UUID todoId;
    private Date todoDate;
    private String id;
    private String place;
    private ArrayList<String> assignedPersons;
    private boolean done;

    public ToDoItem(){
     todoId = UUID.randomUUID();

    }
    public ToDoItem(String todoBody, boolean hasReminder, Date toDoDate){
        todoTxt = todoBody;
        this.hasReminder = hasReminder;
        todoDate = toDoDate;
        todoId = UUID.randomUUID();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTodoTxt() {
        return todoTxt;
    }

    public void setTodoTxt(String todoTxt) {
        this.todoTxt = todoTxt;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public Date getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(Date todoDate) {
        this.todoDate = todoDate;
    }

    public UUID getTodoId() {
        return todoId;
    }

    public void setTodoId(UUID todoId) {
        this.todoId = todoId;
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
        return this.todoTxt;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}