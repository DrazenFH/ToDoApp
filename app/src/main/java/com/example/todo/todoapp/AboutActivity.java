package com.example.todo.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * Created by dl_asus on 19.03.2018.
 */

public class AboutActivity extends AppCompatActivity {
    private TextView versionTextView;
    private String appVersion = "0.1";
    private Toolbar toolbar;
    private TextView contactMe;
    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        Intent i = getIntent();


        versionTextView = (TextView)findViewById(R.id.aboutVersionTextView);
        versionTextView.setText(String.format(getResources().getString(R.string.app_version), appVersion));
        toolbar = (Toolbar)findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(NavUtils.getParentActivityName(this)!=null){
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
