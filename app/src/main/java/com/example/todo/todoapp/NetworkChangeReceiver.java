package com.example.todo.todoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

/**
 * Created by w7pro on 16.04.2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get system service object.
        Object systemServiceObj = context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Convert the service object to ConnectivityManager instance.
        ConnectivityManager connectivityManager = (ConnectivityManager)systemServiceObj;

        // Get network info object.
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Check whether network is available or not.
        boolean networkIsAvailable = false;

        if(networkInfo!=null)
        {
            if(networkInfo.isAvailable())
            {
                networkIsAvailable = true;
            }
        }

        // Display message based on whether network is available or not.
        String networkMessage = "";
        if(networkIsAvailable)
        {
            networkMessage = "Network is available";
            MainActivity.getInstace().enableAddButton();
        }else
        {
          new Alert(context, "No Network connections are available!","Can't connect to Database!");
            MainActivity.getInstace().disableAddButton();

        }
    }
        }


