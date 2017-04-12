package com.example.robert.pruebavolley.activity;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterAgenda;
import com.example.robert.pruebavolley.Adapter.AdapterAgendaResumen;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.Services.AlarmReceiver;
import com.example.robert.pruebavolley.Services.BackgroundService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HomeFragment extends Fragment {

    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    ImageButton btnMensajes, btnAgenda, btnnotificaciones,btnFacturas, btnMateriales, btncalificaciones;

    public HomeFragment() {
    }

    @Override
    public void onStart() {

        setRetainInstance(true);
        /*verMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verMas.isSelected()) {
                    verMas.setTextColor(Color.RED);
                    v.setSelected(false);
                } else {
                    Fragment fragment = new AgendaFragment();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagAgenda").commit();
                    verMas.setTextColor(Color.BLUE);
                    v.setSelected(true);
                }
            }
        });*/



        super.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("Ejecutando", "onResume()");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("Ejecutando", "onStop()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("Ejecutando", "onCreateView()");
        setRetainInstance(true);
        if (!isMyServiceRunning(BackgroundService.class)) {
            alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent alarm = new Intent(getActivity(), AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, alarm, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 30);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 5, pendingIntent); // 5 minutos
        }
        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String colegio = settings.getString("colegio", "null");

        btnAgenda = (ImageButton) rootView.findViewById(R.id.ButtonAgenda);
        btnMensajes = (ImageButton) rootView.findViewById(R.id.ButtonMensajes);
        btnnotificaciones = (ImageButton) rootView.findViewById(R.id.ButtonNotificaciones);
        btnFacturas = (ImageButton) rootView.findViewById(R.id.ButtonFacturas);
        btnMateriales = (ImageButton) rootView.findViewById(R.id.ButtonMateriales);
        btncalificaciones = (ImageButton) rootView.findViewById(R.id.ButtonCalificaciones);

        if(colegio.equals("26") || colegio.equals("27")) {
            btnFacturas.setVisibility(View.GONE);
        }

        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        editor = settings.edit();
        editor.putString("opcionfragment", "0");
        editor.commit();

        btnAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AgendaFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagAgenda").commit();
                /*Intent myIntent = new Intent(getContext(), PagoActivity.class);
                getContext().startActivity(myIntent);*/
                //probar resultado activity
                //Intent myIntent = new Intent(getContext(), RespuestaPagoActivity.class);
                //getContext().startActivity(myIntent);
                //probar pagos
                /*Fragment fragment = new PagosFragment();
                FragmentManager fragmentManager5 = getFragmentManager();
                fragmentManager5.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagPagos").commit();*/
            }
        });

        btnMensajes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new MensajesFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagMensajes").commit();
            }
        });

        btnnotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NotificacionesFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagNotificaciones").commit();
            }
        });

        btnFacturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FacturaFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagFactura").commit();
            }
        });

        btnMateriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ClasesFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagClases").commit();
            }
        });

        btncalificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new notasFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagNotas").commit();
            }
        });

        return rootView;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}


