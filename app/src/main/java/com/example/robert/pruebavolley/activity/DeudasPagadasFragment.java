package com.example.robert.pruebavolley.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.robert.pruebavolley.Adapter.AdapterDeudasPag;
import com.example.robert.pruebavolley.Adapter.AdapterDeudasPend;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeudasPagadasFragment extends Fragment {


    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;

    ListView listView;
    String[] names = new String[4];
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsDeudasP= new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    TextView noDeudasP;
    String alumcodi,periCodi, nomCol,colegio;
    SharedPreferences settings;
    ArrayList<String> numeroFacturaPag= new ArrayList<String>(),codigoDeudaPag= new ArrayList<String>(),descripcionDeudaPag= new ArrayList<String>(),perilectivoPag= new ArrayList<String>(),totalPendientePag= new ArrayList<String>(),fechaCreacionPag= new ArrayList<String>(),estado = new ArrayList<String>();
    private static final String ARG_POSITION = "position";

    public static DeudasPagadasFragment newInstance(int position) {
        DeudasPagadasFragment f = new DeudasPagadasFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deudas_pagadas, container, false);
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

        paramsDeudasP.put("opcion","boton_pagos_lista");
        paramsDeudasP.put("colegio",colegio);
        paramsDeudasP.put("alumnocodi",alumcodi);
        paramsDeudasP.put("deud_estado","P");
        ShowDeudasPagadas(URL,paramsDeudasP);
        return rootView;
    }

    public void ShowDeudasPagadas(String url, final Map<String,String> json){
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

                        estado.add(Object.getString("estado"));
                        if(estado.contains("PAGADA") && !estado.contains("POR")){
                            numeroFacturaPag.add(Object.getString("numeroFactura"));
                            codigoDeudaPag.add(Object.getString("codigoDeuda"));
                            descripcionDeudaPag.add(Object.getString("descripcionDeuda"));
                            perilectivoPag.add(Object.getString("periodo"));
                            totalPendientePag.add(Object.getString("totalAbonado"));
                            fechaCreacionPag.add(Object.getString("fechaVencimiento"));
                        }

                    }

                    AdapterDeudasPag adapter = new AdapterDeudasPag(getActivity(),descripcionDeudaPag,perilectivoPag,totalPendientePag,codigoDeudaPag);
                    listView=(ListView)getActivity().findViewById(R.id.listaPagadas);
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
