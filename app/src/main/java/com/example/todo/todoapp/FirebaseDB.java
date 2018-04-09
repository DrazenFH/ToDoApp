package com.example.todo.todoapp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dl_asus on 09.04.2018.
 */

public class FirebaseDB {

   protected FirebaseFirestore db;

    public FirebaseDB(){
        db = FirebaseFirestore.getInstance();
    }

    public void addData(ToDoItem item ){



        // Add a new document with a generated ID
        db.collection("TodoItems")
                .add(item)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("success", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail", "Error adding document", e);
                    }
                });

    }

    public void readData(){
        db.collection("TodoItems")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("success", document.getId() + " => " + document.getData());
                                System.out.println(document.getData());
                            }
                        } else {
                            Log.w("fail", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
