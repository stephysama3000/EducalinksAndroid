package com.example.robert.pruebavolley.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterAgenda;
import com.example.robert.pruebavolley.Adapter.AdapterClases;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;


public class ClasesFragment extends Fragment {

    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;
    SharedPreferences settings;
    TextView noClases;
    final Map<String,String> paramsMaterias = new HashMap<String,String>();
    final Map<String,String> paramsMateriales = new HashMap<String,String>();
    String URL_alumnos= "http://demo.educalinks.com.ec/mobile/main.php";
    //ShowMaterias
    private String TAG = "TAG";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String colegio;
    //materias
    String alumCursParaMateCodi,cursParaMateCodi,cursParaMateProfCodi,materiaCodi,materiaDetalle,profNombre,profApellido,fechaClases;
    String[] alumCursParaMateCodiA,cursParaMateCodiA,cursParaMateProfCodiA, materiaCodiA,materiaDetalleA,profNombreA,profApellidoA,stringIni;
    //materiales
    String mater_codi,mater_titu,mater_deta,mater_fecha,mater_file,nomCol,periodo;
    String[] mater_codiA,mater_tituA,mater_detaA,mater_fechaA,mater_fileA, materialesnumero,imagenes;
    int i = 0 ;
    int [] numTotalMater;
    int numMateriales = 0;
    ListView list;
    Fragment fragment;
    int variable = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_clases, container, false);

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
        noClases = (TextView) rootView.findViewById(R.id.noClases);
        colegio = settings .getString("colegio", "null");
        periodo = settings .getString("periodo", "null");
        String cursoCodiAlum = settings .getString("cursoCodiAlum", "null");

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

        paramsMaterias.put("colegio", colegio);
        paramsMaterias.put("alumCursParaCodi", cursoCodiAlum );
        paramsMaterias.put("opcion", "clases_materias");
        //pd.show();
        //h.postDelayed(r, 2000);
        ShowMaterias(URL_alumnos, paramsMaterias);
        SharedPreferences.Editor editor1 = settings.edit();
        editor1.putInt("variables",variable);
        editor1.commit();

        return rootView;
    }

    public void ShowMaterias(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){

                        noClases.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    alumCursParaMateCodiA = new String[Keys.size()];
                    cursParaMateCodiA = new String[Keys.size()];
                    cursParaMateProfCodiA = new String[Keys.size()];
                    materiaCodiA = new String[Keys.size()];
                    materiaDetalleA = new String[Keys.size()];
                    profNombreA = new String[Keys.size()];
                    profApellidoA = new String[Keys.size()];
                    numTotalMater = new int[Keys.size()];
                    materialesnumero = new String[Keys.size()];
                    imagenes = new String[Keys.size()];


                    for (i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        alumCursParaMateCodiA[i] = Object.getString("alumCursParaMateCodi");
                        cursParaMateCodiA[i] = Object.getString("cursParaMateCodi");
                        cursParaMateProfCodiA[i]  = Object.getString("cursParaMateProfCodi");
                        Log.d("cursParaMateProfCodiA", cursParaMateProfCodiA[i]);
                        materiaCodiA[i] = Object.getString("materiaCodi");
                        materiaDetalleA[i] = Object.getString("materiaDetalle");
                        Log.d("materiaA", materiaDetalleA[i]);
                        profNombreA[i] = Object.getString("profNombre");
                        profApellidoA[i] = Object.getString("profApellido");
                        materialesnumero[i] = Object.getString("materCount");
                        imagenes[i] = "http://" + nomCol + ".educalinks.com.ec/iconos/" + nomCol + "/" + periodo + "/" + materiaCodiA[i] + ".png";

                        if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            if(materiaDetalleA[i].length() > 30){
                                materiaDetalleA[i] = materiaDetalleA[i].substring(0,29) + "...";
                            }

                        }
                        else if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                            if(materiaDetalleA[i].length() > 18){
                                materiaDetalleA[i] = materiaDetalleA[i].substring(0,17) + "...";
                            }

                        }
                        else if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_XLARGE) {

                        }
                        else if ((getResources().getConfiguration().screenLayout &
                                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                                Configuration.SCREENLAYOUT_SIZE_SMALL) {
                            if(materiaDetalleA[i].length() > 15){
                                materiaDetalleA[i] = materiaDetalleA[i].substring(0,14) + "...";
                            }
                        }



                    }


                    AdapterClases adapter = new AdapterClases(getActivity(), materiaDetalleA, materialesnumero,imagenes);//,imageProfesor);
                    list=(ListView)getActivity().findViewById(R.id.listViewClases);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("cursParaMateProfCodiEsc", cursParaMateProfCodiA[position]);
                            editor.putString("profesorEsc", profNombreA[position].toUpperCase() + " " + profApellidoA[position].toUpperCase());
                            editor.putString("materiaEsc", materiaDetalleA[position].toUpperCase());
                            editor.putString("imagenEsc", imagenes[position].toUpperCase());
                            editor.commit();

                            fragment = new MaterialesFragment();
                            FragmentManager fragmentManager3 = getFragmentManager();
                            fragmentManager3.beginTransaction().replace(R.id.frame, fragment, "tagMateriales").addToBackStack("tagMateriales").commit();


                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorMaterias", error.toString());
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
