package com.example.todo.todoapp;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by dl_asus on 09.04.2018.
 */

public class FirebaseDB {

    private DatabaseReference db;
    private Boolean saved=null;
    private ArrayList<ToDoItem> toDoItemArrayList=new ArrayList<>();
    private OnFireBaseDataChanged dataChangedListener;

        public FirebaseDB(DatabaseReference db, OnFireBaseDataChanged listener) {
            this.db = db;
            dataChangedListener=listener;
        }

    public FirebaseDB(DatabaseReference db) {
        this.db = db;
    }

    //WRITE
    public Boolean save(ToDoItem newItem)
    {
        if(newItem==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("TodoItem").push().setValue(newItem);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

        public void removeItem(ToDoItem item){
            db.child("TodoItem").child(item.getId()).removeValue();

        }
        //READ
        public ArrayList<ToDoItem> retrieve()
        {
            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    fetchData(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    fetchData(dataSnapshot);

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    fetchData(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    fetchData(dataSnapshot);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }


            });

            return toDoItemArrayList;
        }

        private void fetchData(DataSnapshot dataSnapshot) {
            toDoItemArrayList.clear();

          for (DataSnapshot ds : dataSnapshot.getChildren())
            {

                ToDoItem item=new ToDoItem();
                item.setTodoTxt(ds.child("todoTxt").getValue(String.class));
                item.setHasReminder(ds.child("hasReminder").getValue(boolean.class));
                item.setTodoDate(ds.child("todoDate").getValue(Date.class));
                HashMap <String, Object> hashMap=(HashMap<String, Object>) ds.child("todoId").getValue();
                item.setTodoId(new UUID((long)hashMap.get("mostSignificantBits"), (long)hashMap.get("leastSignificantBits")));
                item.setAssignedPersons((ArrayList<String>)ds.child("assignedPersons").getValue());
                item.setPlace(ds.child("place").getValue(String.class));
                item.setDone(ds.child("done").getValue(boolean.class));
                item.setId(ds.getKey());
                toDoItemArrayList.add(item);
                dataChangedListener.dataChanged();
             }
        }

        public void updateDone(ToDoItem item){
            try {
                db.child("TodoItem").child(item.getId()).child("done").setValue(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
}
