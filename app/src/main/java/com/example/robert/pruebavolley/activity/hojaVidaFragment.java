package com.example.robert.pruebavolley.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterHojaVida;
import com.example.robert.pruebavolley.Adapter.AdapterTratamiento;
import com.example.robert.pruebavolley.Clases.AppController;
import com.example.robert.pruebavolley.Clases.getString;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class hojaVidaFragment extends Fragment {


    final Map<String,String> paramsObservaciones= new HashMap<String,String>();
    Spinner TipoObservaciones;
    final Map<String, String> paramsTipoObserv = new HashMap<String, String>();
    SharedPreferences settings;
    TreeMap<Integer,String> tipos = new TreeMap<Integer,String>();
    TreeMap<Integer,String> tiposCopy = new TreeMap<Integer,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String obsTipoCodi, obsTipoDeta;
    ArrayAdapter<CharSequence> adapterPrueba;
    String URL = "http://app.educalinks.com.ec/mobile/main.php";
    String result;
    String tag_string_req = "req_login";
    String[] obseCodi,obseDeta,obseFech,obseTipoDeta, usuaDeta, usuaTipo;
    String colegio, pericodi, alumnocodi;
    int codObservacion;
    ListView list;
    ExpandableListView expandableListView;
    TextView noObservaciones;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hoja_vida, container, false);


        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        colegio = settings.getString("colegio", "null");
        pericodi = settings.getString("periodo", "null");
        alumnocodi = settings.getString("alumnocodi", "null");
        TipoObservaciones = (Spinner) rootView.findViewById(R.id.tipoObservaciones);
        noObservaciones = (TextView) rootView.findViewById(R.id.noObservaciones);


        paramsTipoObserv.put("opcion", "tipo_Observaciones");
        paramsTipoObserv.put("colegio", colegio );
        funcion(paramsTipoObserv);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;*/

        expandableListView = (ExpandableListView)rootView.findViewById(R.id.listObservaciones);
        //expandableListView.setIndicatorBounds(width - GetPixelFromDips(48), width - GetPixelFromDips(10));
        //expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.expdropdown));
        //setGroupIndicatorToRight();
        TipoObservaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String opcionEscogida = parent.getItemAtPosition(position).toString();
                int codOpcionEsc = (int) getKeyFromValue(tiposCopy,opcionEscogida);

                if (parent.getItemAtPosition(position).equals(tiposCopy.get(codOpcionEsc))) {
                    //IdColegio = i;
                    //colegio = map.get(i);
                    //Log.d(TAG, "codi cole: " + colegio);
                    codObservacion = codOpcionEsc;
                    //Log.d(TAG, "codi cole: " + colegio);
                    //Toast.makeText(getApplicationContext(), "id colegio:" + IdColegio + ", colegio: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                    paramsObservaciones.put("opcion", "Observaciones_por_Tipo");
                    paramsObservaciones.put("pericodi", pericodi);
                    paramsObservaciones.put("alumnocodi", alumnocodi );
                    paramsObservaciones.put("tipo_observacion", String.valueOf(codObservacion));
                    paramsObservaciones.put("colegio", colegio);
                    observaciones(URL,paramsObservaciones);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    /*private void setGroupIndicatorToRight() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;

        expandableListView.setIndicatorBounds(width - getDipsFromPixel(35), width
                - getDipsFromPixel(5));
    }
    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }*/


    public void funcion(final Map<String,String> json) {

        tipos.put(-1, "- Todas -");
        getString(new getString.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    jsonObject = new JSONObject(result.toString());
                    JsonAll = jsonObject.getJSONArray("result");
                    for (int i = 0; i < JsonAll.length(); i++) {
                        JSONObject Object = JsonAll.getJSONObject(i);
                        obsTipoCodi = Object.getString("obse_tipo_codi");
                        obsTipoDeta = Object.getString("obse_tipo_deta");
                        tipos.put(Integer.parseInt(obsTipoCodi), obsTipoDeta);
                    }
                    showTipos(tipos);
                    tiposCopy = showTipos(tipos);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error:", e.getMessage());
                }
            }
        },json);
    }

    private String getString(final getString.VolleyCallback volleyCallback, final Map<String,String> json) {
        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result=response;
                volleyCallback.onSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //System.out.println(volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = json;

                return map;
            }
        };;
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return result;
    }

    public TreeMap<Integer,String> showTipos(TreeMap<Integer,String> colegios){
        //map = new TreeMap<Integer,String>(colegios);
        Collection<String> vals = colegios.values();
        String[] array = vals.toArray(new String[vals.size()]);

        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(getActivity(),array);
        //adapterPrueba = new ArrayAdapter<CharSequence>(getActivity(),);
        //adapterPrueba.setDropDownViewResource(R.layout.spinner_drop_down);
        //TipoObservaciones.setAdapter(adapterPrueba);
        TipoObservaciones.setAdapter(customSpinnerAdapter);
        TipoObservaciones.setSelection(0);
        //Toast.makeText(MainActivity.this, map.get(2), Toast.LENGTH_SHORT).show();
        return colegios;
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private String[] asr;

        public CustomSpinnerAdapter(Context context,String[] asr) {
            this.asr=asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.length;
        }

        public Object getItem(int i)
        {
            return asr[i];
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(getActivity());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr[position]);
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getActivity());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr[i]);
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }


   public void observaciones(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");
                    if(JsonAll.length()!=0) {
                        noObservaciones.setVisibility(View.GONE);
                        expandableListView.setVisibility(View.VISIBLE);
                        final ArrayList<String> Keys = new ArrayList<String>();
                        for (int t = 0; t < JsonAll.length(); t++) {
                            Keys.add(String.valueOf(t));
                        }
                        //String[] obseCodi,obseDeta,obseFech,obseTipoDeta, usuaDeta, usuaTipo;
                        obseCodi = new String[Keys.size()];
                        obseDeta = new String[Keys.size()];
                        obseFech = new String[Keys.size()];
                        obseTipoDeta = new String[Keys.size()];
                        usuaDeta = new String[Keys.size()];
                        usuaTipo = new String[Keys.size()];


                        for (int i = 0; i < JsonAll.length(); i++) {
                            final int Index = i;
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            obseCodi[i] = Object.getString("obse_codi");
                            obseDeta[i] = Object.getString("obse_deta");
                            String obseFechS = Object.getString("obse_fech");
                            obseTipoDeta[i] = Object.getString("obse_tipo_deta");
                            usuaDeta[i] = Object.getString("usua_deta");
                            usuaTipo[i] = Object.getString("usua_tipo");

                            String[] stringIni = obseFechS.split(":");
                            String newDateIni = (stringIni[1]);
                            newDateIni = newDateIni.substring(1, newDateIni.length() - 3);
                            obseFech[i] = newDateIni;


                        }
                        //(Activity context, String[] tipoObserv, String[] detalleObserv, String[] profesor, String[] rol, String[] fecha)
                        AdapterHojaVida adapter = new AdapterHojaVida(getActivity(),obseTipoDeta,obseDeta,usuaDeta,usuaTipo,obseFech);
                        expandableListView.setAdapter(adapter);
                        //list=(ListView) rootView.findViewById(R.id.ListaTratamiento);
                        //list.setAdapter(adapter);

                    }
                    else{
                        noObservaciones.setVisibility(View.VISIBLE);
                        expandableListView.setVisibility(View.GONE);
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

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

}
