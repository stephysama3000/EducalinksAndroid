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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterNotas;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class notasFragment extends Fragment {

    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;
    ImageView warning;


    String URL_alumnos= "http://app.educalinks.com.ec/mobile/main.php";
    SharedPreferences settings;
    TextView noNotas,bloqueo;
    final Map<String,String> paramsNotas = new HashMap<String,String>();
    final Map<String,String> paramsBloqueo = new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String [] periDistCodi,periDistDeta,periDistDetaPadre;
    String alumcodi,cursParaCodi,periCodi, nomCol;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notas, container, false);
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
        noNotas = (TextView) rootView.findViewById(R.id.noNotas);
        warning = (ImageView) rootView.findViewById(R.id.warning);
        bloqueo = (TextView) rootView.findViewById(R.id.bloqueo);
        alumcodi = settings .getString("alumnocodi", "null");
        cursParaCodi = settings .getString("cursoCodi", "null");
        String periDistCabTipo = settings .getString("distipo", "null");
        periCodi = settings .getString("periodoAlum", "null");
        String colegio = settings .getString("colegio", "null");

        switch (Integer.parseInt(colegio)) {
            case 1:
                nomCol = "liceopanamericano";
                break;
            case 2:
                nomCol = "ecobab";
                break;
            case 5:
                nomCol = "moderna";
                break;
            case 11:
                nomCol = "ecobabvesp";
                break;
            case 12:
                nomCol = "desarrollo";
                break;
            case 16:
                nomCol = "delfos";
                break;
            case 17:
                nomCol = "delfosvesp";
                break;
            case 18:
                nomCol = "liceopanamericanosur";
                break;
            case 26:
                nomCol = "duplos";
                break;
            case 27:
                nomCol = "arcoiris";
                break;
            default:
                Toast.makeText(getActivity(), "Su institución no se encuentra en el directorio", Toast.LENGTH_SHORT).show();
        }

        paramsNotas.put("pericodi",periCodi);
        paramsNotas.put("colegio",colegio);
        paramsNotas.put("opcion","obtener_Etapas");
        paramsNotas.put("periDistCabTipo",periDistCabTipo);
        paramsNotas.put("periEtapCodi","U");

        paramsBloqueo.put("alumnocodi",alumcodi);
        paramsBloqueo.put("pericodi",periCodi);
        paramsBloqueo.put("colegio",colegio);
        paramsBloqueo.put("opcion","bloqueo_alumnos2");
        BloqueoPermiso(URL_alumnos,paramsBloqueo);

        return rootView;
    }

    public void ShowNotas(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){
                        noNotas.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    periDistCodi = new String[Keys.size()];
                    periDistDeta = new String[Keys.size()];
                    periDistDetaPadre = new String[Keys.size()];

                    for (int i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        periDistCodi[i] = Object.getString("peridistcodi");
                        periDistDeta[i] = Object.getString("peridistdeta");
                        periDistDetaPadre[i] = Object.getString("peridistdetapadr");

                    }

                    AdapterNotas adapter = new AdapterNotas(getActivity(),periDistDetaPadre,periDistDeta, alumcodi, cursParaCodi, periCodi, nomCol,periDistCodi);
                    list=(ListView)getActivity().findViewById(R.id.listViewNotas);
                    list.setAdapter(adapter);

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

    public void BloqueoPermiso(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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

                    for (int i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        int tiene_deuda = Object.getInt("tiene_deuda");
                        if(tiene_deuda==1){
                            warning.setVisibility(View.VISIBLE);
                            bloqueo.setVisibility(View.VISIBLE);
                            list=(ListView)getActivity().findViewById(R.id.listViewNotas);
                            list.setVisibility(View.GONE);
                        }
                        else{

                            ShowNotas(URL_alumnos, paramsNotas);
                        }

                        //alumcodi + cursParaCodi + periDistCodi[i] + periodo
                        //FAC001-001-000114470
                    }
                    /*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {


                        }
                    });*/
                    // urlPdf =
                    //http://moderna.educalinks.com.ec/documentos/autorizados/moderna/0922655949/FAC001-001-000114470.PDF



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
