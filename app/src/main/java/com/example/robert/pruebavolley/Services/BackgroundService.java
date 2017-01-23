package com.example.robert.pruebavolley.Services;

import android.app.*;
import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Clases.AppController;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.activity.Activity3;
import com.example.robert.pruebavolley.activity.ActivityUpdate;
import com.example.robert.pruebavolley.activity.AgendaFragment;
import com.example.robert.pruebavolley.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BackgroundService extends Service {

    String URL_alumnos= "http://demo.educalinks.com.ec/mobile/main.php";
    private String TAG = "TAG";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    static final Map<String,String> codigos = new HashMap<String,String>();
    final Map<String,String> paramsAgenda = new HashMap<String,String>();
    SharedPreferences settings;
    int i;
    String notiEstado,notiTipoDetalle,notiDetalle;
    String[] noti_tipo_deta,noti_deta,notiDetaEsta;

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    SharedPreferences.Editor editor;
    String notificacion;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {

        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;

            settings = PreferenceManager.getDefaultSharedPreferences(this);
            editor = settings.edit();
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
            paramsAgenda.put("opcion", "notificaciones_all");
            paramsAgenda.put("alumnocodi", alumnocodi);

            ShowNoti(URL_alumnos, paramsAgenda);
            //Notify("Agenda","Materia:");

            this.backgroundThread.start();
        }
        return START_STICKY;
    }

    private void NotifyAgenda(String notificationTitle, String notificationMessage, int i){
        Random r = new Random();
        int i1 = i;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.logo_smallnoti,"Nueva Agenda", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,ActivityUpdate.class);

        editor.putString("notificacion","agenda");
        editor.commit();

        Intent intent = new Intent();
        intent.setAction("one");
        intent.putExtra("fragment","agenda");
        sendBroadcast(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        notification.setLatestEventInfo(this, notificationTitle, notificationMessage, pendingIntent);
        notification.flags= Notification.FLAG_AUTO_CANCEL;
        //paramsAgenda.put("opcion", "notificaciones_update");

            notificationManager.notify(i, notification);
        //notificationManager.notify(i1, notification);
    }

    private void NotifyActualizacion(String notificationTitle, String notificationMessage, int i){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.logo_smallnoti,"Nueva Actualizacion", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,ActivityUpdate.class);

        editor.putString("notificacion","actualizacion");
        editor.commit();

        Intent intent = new Intent();
        intent.setAction("one");
        intent.putExtra("fragment","agenda");
        sendBroadcast(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        notification.setLatestEventInfo(this, notificationTitle, notificationMessage, pendingIntent);
        notification.flags= Notification.FLAG_AUTO_CANCEL;
        //paramsAgenda.put("opcion", "notificaciones_update");

        notificationManager.notify(i, notification);
    }

    private void NotifyMensajes(String notificationTitle, String notificationMessage, int i){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.logo_smallnoti,"Nuevo Mensaje", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,ActivityUpdate.class);
        editor.putString("notificacion","mensajes");
        editor.commit();
        Intent intent = new Intent();
        intent.setAction("two");
        intent.putExtra("mensajes","mensajes");
        sendBroadcast(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, 0);

        notification.setLatestEventInfo(this, notificationTitle, notificationMessage, pendingIntent);
        notification.flags= Notification.FLAG_AUTO_CANCEL;
        //paramsAgenda.put("opcion", "notificaciones_update");

        notificationManager.notify(i, notification);
    }

    public void ShowNoti(String url, final Map<String,String> json){
        //final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    noti_tipo_deta = new String[Keys.size()];
                    noti_deta = new String[Keys.size()];
                    notiDetaEsta = new String[Keys.size()];

                    for (i = 0; i < JsonAll.length(); i++) {
                        final JSONObject Object = JsonAll.getJSONObject(i);
                        notiEstado = Object.getString("noti_deta_esta");
                        notiDetaEsta[i]=notiEstado;

                        notiTipoDetalle = Object.getString("noti_tipo_deta");
                        noti_tipo_deta[i]=notiTipoDetalle;

                        notiDetalle = Object.getString("noti_deta");
                        noti_deta[i] = notiDetalle;
//&& newDateIni.equals(currentDateandTime)
                        if(notiDetaEsta[i].equals("A") && noti_tipo_deta[i].equals("Agenda")) {
                            NotifyAgenda(noti_tipo_deta[i], noti_deta[i], i);
                        }
                        if(notiDetaEsta[i].equals("A") && noti_tipo_deta[i].equals("Actualizaciones")) {
                            NotifyActualizacion(noti_tipo_deta[i], noti_deta[i], i);
                        }
                        if(notiDetaEsta[i].equals("A") && noti_tipo_deta[i].equals("Mensajes")) {
                            NotifyMensajes(noti_tipo_deta[i], noti_deta[i], i);
                        }

                    }

                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("otro error:",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = json;

                return map;
            }
        };
        AppController.getInstance().getRequestQueue().add(post);
    }



}