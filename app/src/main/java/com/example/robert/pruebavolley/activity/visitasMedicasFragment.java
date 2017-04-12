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
import com.example.robert.pruebavolley.Adapter.AdapterFacturas;
import com.example.robert.pruebavolley.Adapter.AdapterVisitasMedicas;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class visitasMedicasFragment extends Fragment {

    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;
    TextView noVisitas;
    String colegio,alumnocodi, nomCol;
    final Map<String,String> paramsVisitasMedicas = new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String URL = "http://app.educalinks.com.ec/mobile/main.php";
    SharedPreferences settings;
    String[] enfe_descripcion,aten_fechaCreacion,aten_codigo;
    ListView list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_visitas_medicas, container, false);

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
        nomCol = settings .getString("nomCol", "null");
        alumnocodi = settings .getString("alumnocodi", "null");
        noVisitas = (TextView) rootView.findViewById(R.id.noVisitas);

        paramsVisitasMedicas.put("alumnocodi",alumnocodi);
        paramsVisitasMedicas.put("colegio",colegio);
        paramsVisitasMedicas.put("opcion","mostrar_visitasMedicas");
        ShowVisitasMedicas(URL, paramsVisitasMedicas);


        return rootView;
    }

    public void ShowVisitasMedicas(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){
                        noVisitas.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    enfe_descripcion = new String[Keys.size()];
                    aten_fechaCreacion = new String[Keys.size()];
                    aten_codigo = new String[Keys.size()];

                    for (int i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        String enfe_descripcionS = Object.getString("enfe_descripcion");
                        String aten_fechaCreacionS = Object.getString("aten_fechaCreacion");
                        String aten_codigoS = Object.getString("aten_codigo");

                        enfe_descripcion[i] = enfe_descripcionS;
                        //aten_fechaCreacion[i] = aten_fechaCreacionS;
                        aten_codigo[i] = aten_codigoS;

                        String[] stringIni = aten_fechaCreacionS.split(":");
                        String newDateIni = (stringIni[1]);
                        newDateIni = newDateIni.substring(1, newDateIni.length() - 3);
                        aten_fechaCreacion [i] = newDateIni;

                    }
                    //String[] motivo, String[] fechaHora, String[] atenCodigo, String nomCol, String colegio
                    AdapterVisitasMedicas adapter = new AdapterVisitasMedicas(getActivity(), enfe_descripcion, aten_fechaCreacion, aten_codigo, nomCol,colegio);//,imageProfesor);
                    list=(ListView)getActivity().findViewById(R.id.listViewVisitasMedicas);
                    list.setAdapter(adapter);
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
