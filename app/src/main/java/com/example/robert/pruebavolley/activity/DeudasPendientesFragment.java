package com.example.robert.pruebavolley.activity;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterDeudasPend;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pe.solera.api_payme_android.bean.Commerce;
import pe.solera.api_payme_android.bean.PersonAPIRC;
import pe.solera.api_payme_android.bean.TransactionInformationAPIRC;
import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.classes.activities.LoadingInformationAct;
import pe.solera.api_payme_android.util.Constants;


public class DeudasPendientesFragment extends Fragment {


    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;

    private static final String ARG_POSITION = "position";
    ListView listView;
    String[] names = new String[4];
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsDeudasP= new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    TextView noDeudasP;
    String alumcodi,periCodi, nomCol,colegio;
    SharedPreferences settings;
    //String [] numeroFacturaPend,codigoDeudaPend,descripcionDeudaPend,perilectivoPend,totalPendientePend,fechaCreacionPend,estadoPend,estado;
    public static ArrayList<String> numeroFacturaPag= new ArrayList<String>(),codigoDeudaPag= new ArrayList<String>(),descripcionDeudaPag= new ArrayList<String>(),perilectivoPag= new ArrayList<String>(),totalPendientePag= new ArrayList<String>(),fechaCreacionPag= new ArrayList<String>(),estadoPag;
    ArrayList<String> numeroFacturaPend= new ArrayList<String>(),codigoDeudaPend= new ArrayList<String>(),descripcionDeudaPend= new ArrayList<String>(),perilectivoPend= new ArrayList<String>(),totalPendientePend= new ArrayList<String>(),fechaCreacionPend= new ArrayList<String>(),estadoPend,estado= new ArrayList<String>();
    Button btnPagar;

    //reg Tarjetas
    private Commerce commerce = new Commerce();
    private PersonAPIRC personAPIRC = new PersonAPIRC();
    private TransactionInformationAPIRC transactionInformationAPIRC = new TransactionInformationAPIRC();

    private final String TAG = getClass().getSimpleName();



    public static DeudasPendientesFragment newInstance(int position) {
        DeudasPendientesFragment f = new DeudasPendientesFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deudas_pendientes, container, false);
        h = new Handler();
        pd = new TransparentProgressDialog(getActivity(), R.drawable.processdialog);
        r =new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

        };
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        pd.show();
        h.postDelayed(r, 2000);

        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        colegio = settings .getString("colegio", "null");
        alumcodi = settings .getString("alumnocodi", "null");
        periCodi = settings .getString("periodoAlum", "null");
        noDeudasP = (TextView) rootView.findViewById(R.id.noDeudasP);
        btnPagar = (Button) rootView.findViewById(R.id.btnPagar);

        paramsDeudasP.put("opcion","boton_pagos_lista");
        paramsDeudasP.put("colegio",colegio);
        paramsDeudasP.put("alumnocodi",alumcodi);
        paramsDeudasP.put("deud_estado","PC");
        ShowDeudasPendientes(URL,paramsDeudasP);


        return rootView;
    }


    public void ShowDeudasPendientes(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){
                        noDeudasP.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    //estado = new String[Keys.size()];


                    for (int i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        estado.add(i,Object.getString("estado"));
                        if(!estado.contains("PAGADA") && !estado.contains("ANULADA")){
                            numeroFacturaPend.add(Object.getString("numeroFactura"));
                            codigoDeudaPend.add(Object.getString("codigoDeuda"));
                            descripcionDeudaPend.add(Object.getString("descripcionDeuda"));
                            perilectivoPend.add(Object.getString("periodo"));
                            totalPendientePend.add(Object.getString("totalPendiente"));
                            fechaCreacionPend.add(Object.getString("fechaVencimiento"));
                        }

                    }

                    AdapterDeudasPend adapter = new AdapterDeudasPend(getActivity(),descripcionDeudaPend,perilectivoPend,fechaCreacionPend,totalPendientePend,codigoDeudaPend,btnPagar);
                    listView=(ListView)getActivity().findViewById(R.id.listaPendientes);
                    listView.setAdapter(adapter);

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


    private class TransparentProgressDialog extends Dialog {

        private ImageView iv;

        public TransparentProgressDialog(Context context, int resourceIdOfImage) {
            super(context, R.style.TransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            iv = new ImageView(context);
            iv.setImageResource(resourceIdOfImage);
            layout.addView(iv, params);
            addContentView(layout, params);
        }

        @Override
        public void show() {
            super.show();
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(4000);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        }
    }

}
