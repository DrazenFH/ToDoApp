package com.example.todo.todoapp;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    private int mTheme = -1;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseDB db = new FirebaseDB();
        db.readData();

        mTheme = R.style.CustomStyle_LightTheme;
        this.setTheme(mTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Liste erstellen
        mListView = (ListView) findViewById(R.id.recipe_list_view);
// 1
        final ArrayList<String> recipeList = new ArrayList<String>();
        recipeList.add("Tomaten kaufen!");
        recipeList.add("Grüne Paprika kaufen!");
        recipeList.add("Ayran!");
        recipeList.add("Schweinegrammeln");

// 2
        String[] listItems = new String[recipeList.size()];
// 3
        for(int i = 0; i < recipeList.size(); i++){
            String recipe = recipeList.get(i);
            listItems[i] = recipe;
        }
// 4
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aboutMeMenuItem:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                return true;

            /*case R.id.preferences:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewItem(View view){
        Intent intent = new Intent(this, AddTodoActivity.class);
        startActivity(intent);
    }

}
