package com.example.todo.todoapp;

import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Boolean save(ToDoItem spacecraft)
    {
        if(spacecraft==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("TodoItem").push().setValue(spacecraft);
                saved=true;

            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
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

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
                item.setmToDoText(ds.child("mToDoText").getValue(String.class));
                item.setmHasReminder(ds.child("mHasReminder").getValue(boolean.class));
                item.setmToDoDate(ds.child("mToDoDate").getValue(Date.class));
                HashMap <String, Object> hashMap=(HashMap<String, Object>) ds.child("mTodoIdentifier").getValue();
                item.setmTodoIdentifier(new UUID((long)hashMap.get("mostSignificantBits"), (long)hashMap.get("leastSignificantBits")));
                item.setAssignedPersons((ArrayList<String>)ds.child("assignedPersons").getValue());

                toDoItemArrayList.add(item);
                dataChangedListener.dataChanged();
             }
        }
}
