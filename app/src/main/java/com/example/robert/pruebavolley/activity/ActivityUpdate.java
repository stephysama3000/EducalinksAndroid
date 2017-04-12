package com.example.robert.pruebavolley.activity;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityUpdate extends AppCompatActivity {

    String URL_alumnos= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsAgenda = new HashMap<String,String>();
    SharedPreferences settings;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        String usuario = settings .getString("usuario", "null");
        String password = settings .getString("password", "null");
        String colegio = settings .getString("colegio", "null");
        String reprcodigo = settings .getString("codigo", "null");
        String periodo = settings .getString("periodo", "null");
        String alumnocodi = settings .getString("alumnocodi", "null");

        paramsAgenda.put("colegio", colegio);
        paramsAgenda.put("username", usuario);
        paramsAgenda.put("password", password);
        paramsAgenda.put("tipo_usua", "2");
        paramsAgenda.put("reprcodi", reprcodigo);
        paramsAgenda.put("pericodi", periodo);
        paramsAgenda.put("opcion", "notificaciones_update");
        paramsAgenda.put("alumnocodi", alumnocodi);
        UpdateNoti(URL_alumnos, paramsAgenda);
        Intent intent = new Intent(ActivityUpdate.this, Activity3.class);
        intent.putExtra("fragment","agenda");
        startActivity(intent);
    }

    public void UpdateNoti(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG", "Login Response: " + response.toString());

                    }

                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("otro error:",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = json;

                return map;
            }
        };
        queue.add(post);
    }


}
