package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redlinks on 31/3/17.
 */

public class AdapterVisitasMedicas extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] motivo;
    private final String[] fechaHora;
    private final String[] atenCodigo;
    String nomCol,colegio;
    View dialoglayout2;
    TextView mot, fecha;

    //Request
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    final Map<String,String> paramsTratamiento= new HashMap<String,String>();
    String[] medicamento, cantidad;
    SharedPreferences settings;
    Button verTratamiento,descargarComprobante;
    ListView list;

    public AdapterVisitasMedicas(Activity context, String[] motivo, String[] fechaHora, String[] atenCodigo, String nomCol, String colegio) {
        super(context, R.layout.lista_visitasmed, motivo);
        this.context = context;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.atenCodigo = atenCodigo;
        this.nomCol = nomCol;
        this.colegio = colegio;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.lista_visitasmed, null, true);
        verTratamiento = (Button) rowView.findViewById(R.id.medButton);
        descargarComprobante = (Button) rowView.findViewById(R.id.ComprobButton);

        mot  = (TextView) rowView.findViewById(R.id.motivo);
        fecha = (TextView) rowView.findViewById(R.id.fechaHora);

        mot.setText(motivo[position]);
        fecha.setText(fechaHora[position]);



        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }



        descargarComprobante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("url","http://" + nomCol + ".educalinks.com.ec/medic/comprobante_atencion/" + atenCodigo[position] );
                String URL = "http://" + nomCol + ".educalinks.com.ec/medic/comprobante_atencion/" + atenCodigo[position];
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
            }
        });

        verTratamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = ((Activity)context).getLayoutInflater();
                dialoglayout2 = inflater2.inflate(R.layout.popup_tratamiento, null);

                paramsTratamiento.put("colegio",colegio);
                paramsTratamiento.put("opcion","detalle_visitasMedicas");
                paramsTratamiento.put("aten_codigo",atenCodigo[position]);
                Tratamiento(URL,paramsTratamiento);

                Dialog d = new AlertDialog.Builder(((Activity)context), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Tratamiento")
                        .setNegativeButton("Cancel", null)
                        .setView(dialoglayout2)
                        .create();
                d.show();
            }
        });

        return rowView;
    }


    public void Tratamiento(String url, final Map<String,String> json){
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

                        medicamento = new String[Keys.size()];
                        cantidad = new String[Keys.size()];


                        for (int i = 0; i < JsonAll.length(); i++) {
                            final int Index = i;
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            String medicamentoS = Object.getString("med_descripcion");
                            cantidad[i] = Object.getString("aten_deta_med_cantidad");

                            if(medicamentoS.equals("null"))
                            {
                                medicamento[i] = "- - - -";
                            }
                            else{
                                medicamento[i] = medicamentoS;
                            }

                        }

                        AdapterTratamiento adapter = new AdapterTratamiento((Activity)context,medicamento,cantidad);
                        list=(ListView) dialoglayout2.findViewById(R.id.ListaTratamiento);
                        list.setAdapter(adapter);

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
