package com.example.robert.pruebavolley.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.example.robert.pruebavolley.Adapter.AdapterNoti;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NotificacionesFragment extends Fragment {

    String URL_alumnos= "http://app.educalinks.com.ec/mobile/main.php";
    private String TAG = "TAG";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    final Map<String,String> paramsAgenda = new HashMap<String,String>();
    String fechaRegistro,notiTipoDetalle,notiDetalle;
    String[] stringIni,noti_fech_reg,noti_tipo_deta,noti_deta;
    SharedPreferences settings;
    int i;
    int a =0;
    ListView list;
    String newDateIni,newDateIni2,hora;
    TextView noNotificaciones;

    public NotificacionesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_notificaciones, container, false);
        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        String usuario = settings .getString("usuario", "null");
        String password = settings .getString("password", "null");
        String colegio = settings .getString("colegio", "null");
        String reprcodigo = settings .getString("codigo", "null");
        String periodo = settings .getString("periodo", "null");
        String alumnocodi = settings .getString("alumnocodi", "null");
        noNotificaciones = (TextView) rootView.findViewById(R.id.noNotificaciones);

        paramsAgenda.put("colegio", colegio);
        paramsAgenda.put("username", usuario);
        paramsAgenda.put("password", password);
        paramsAgenda.put("tipo_usua", "2");
        paramsAgenda.put("reprcodi", reprcodigo);
        paramsAgenda.put("pericodi", periodo);
        paramsAgenda.put("opcion", "notificaciones_all");
        paramsAgenda.put("alumnocodi", alumnocodi);

        ShowNoti(URL_alumnos, paramsAgenda);

        return rootView;
    }

    public void ShowNoti(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDateandTime = sdf.format(new Date());
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){

                        noNotificaciones.setVisibility(View.VISIBLE);
                    }else {

                        final ArrayList<String> Keys = new ArrayList<String>();
                        for (int t = 0; t < JsonAll.length(); t++) {
                            Keys.add(String.valueOf(t));
                        }

                        noti_tipo_deta = new String[Keys.size()];
                        noti_deta = new String[Keys.size()];
                        noti_fech_reg = new String[Keys.size()];

                        for (i = 0; i < JsonAll.length(); i++) {
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            notiTipoDetalle = Object.getString("noti_tipo_deta");
                            noti_tipo_deta[i] = notiTipoDetalle;
                            notiDetalle = Object.getString("noti_deta");
                            noti_deta[i] = notiDetalle;

                            fechaRegistro = Object.getJSONObject("noti_deta_fech_regi").getString("date");
                            String[] fechaSplit = fechaRegistro.split(" ");
                            noti_fech_reg[i] = fechaSplit[0];


                        }
                        AdapterNoti adapter = new AdapterNoti(getActivity(), noti_tipo_deta, noti_fech_reg, noti_deta);//,imageProfesor);
                        list = (ListView) getActivity().findViewById(R.id.listViewNotificaciones);
                        list.setAdapter(adapter);
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
        queue.add(post);
    }

}
