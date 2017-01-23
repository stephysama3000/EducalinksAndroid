package com.example.robert.pruebavolley.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.robert.pruebavolley.R;

public class ActivityInicial extends AppCompatActivity {

    SharedPreferences app_preferences;
    String currentUsername, currentpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);


        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentUsername = app_preferences.getString("usuario", "");
        currentpassword = app_preferences.getString("password", "");

        //Toast.makeText(getApplicationContext(), "this app has started" + currentUsername + " " + currentpassword, Toast.LENGTH_LONG).show();
        if(isInternet()){
            if(!currentUsername.equals("") && !currentpassword.equals("")){
                Intent intent = new Intent(ActivityInicial.this, Activity3.class);
                startActivity(intent);
            }
            else {

                Intent intent = new Intent(ActivityInicial.this, MainActivity.class);
                startActivity(intent);
            }
        }
        else{

            Dialog();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_activity_inicial, menu);
        return true;
    }

    public boolean isInternet()
    {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {
            return true;
        }
        return false;
    }

    public void Dialog()
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        dlgAlert.setMessage("Compruebe su conexión a Internet");
        dlgAlert.setTitle("Educalinks");
        dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }


}
