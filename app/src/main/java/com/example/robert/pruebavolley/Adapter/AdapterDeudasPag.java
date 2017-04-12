package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.activity.Activity3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redlinks on 28/11/16.
 */
public class AdapterDeudasPag extends ArrayAdapter {
    Context context;
    private final ArrayList<String> detalle;
    private final ArrayList<String> añoLectivo;
    private final ArrayList<String> valorTotal;
    private final ArrayList<String> codigoDeudaPag;
    Button VerPagos;
    TextView totalPago,formaPago;
    SharedPreferences settings;
    String colegio;
    String[] totalPagoPag,formaPagoPag,codigoPagoPag;
    ListView listView;
    View dialoglayout2;

    //Request
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsPagosRealizados= new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;

    public AdapterDeudasPag(Context context, ArrayList<String> detalle, ArrayList<String> añoLectivo, ArrayList<String> valorTotal, ArrayList<String> codigoDeudaPag)
    {
        super(context, R.layout.lista_deudas_pagadas,detalle);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.detalle = detalle;
        this.añoLectivo = añoLectivo;
        this.valorTotal = valorTotal;
        this.codigoDeudaPag = codigoDeudaPag;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.lista_deudas_pagadas, parent, false);
        final TextView deta = (TextView) convertView.findViewById(R.id.Detalle);
        TextView total = (TextView) convertView.findViewById(R.id.ValorTotal);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);


        settings = PreferenceManager.getDefaultSharedPreferences((Activity)context);
        colegio = settings .getString("colegio", "null");


        deta.setText(detalle.get(position));
        total.setText(valorTotal.get(position));

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }





        VerPagos = (Button) convertView.findViewById(R.id.VerPagos);

        VerPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = ((Activity)context).getLayoutInflater();
                dialoglayout2 = inflater2.inflate(R.layout.popup_pagos, null);

                paramsPagosRealizados.put("colegio",colegio);
                paramsPagosRealizados.put("opcion","pagos_realizados");
                paramsPagosRealizados.put("deuda",codigoDeudaPag.get(position));
                PagosRealizados(URL,paramsPagosRealizados);

                //String hola = "hola";


                //totalPago.setText(totalPagoPag[position]);
                //formaPago.setText(formaPagoPag[position]);

                Dialog d = new AlertDialog.Builder(((Activity)context), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Pagos Realizados")
                        .setNegativeButton("Cancel", null)
                        .setView(dialoglayout2)
                        .create();
                d.show();
            }
        });

        return convertView;
    }

    public void PagosRealizados(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");
                    if(JsonAll.length()!=0) {
                        final ArrayList<String> Keys = new ArrayList<String>();
                        for (int t = 0; t < JsonAll.length(); t++) {
                            Keys.add(String.valueOf(t));
                        }

                        totalPagoPag = new String[Keys.size()];
                        formaPagoPag = new String[Keys.size()];
                        codigoPagoPag = new String[Keys.size()];


                        for (int i = 0; i < JsonAll.length(); i++) {
                            final int Index = i;
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            totalPagoPag[i] = Object.getString("totalPago");
                            formaPagoPag[i] = Object.getString("formaPago");
                            codigoPagoPag[i] = Object.getString("codigoPago");
                        }

                        AdapterPagosRealizados adapter = new AdapterPagosRealizados((Activity)context,totalPagoPag,formaPagoPag,codigoPagoPag);
                        listView=(ListView) dialoglayout2.findViewById(R.id.ListaPagosRealizados);
                        listView.setAdapter(adapter);

                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("facturaerror", e.toString());

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGerror", error.toString());
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
