package com.example.robert.pruebavolley.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.robert.pruebavolley.Adapter.AdapterAgenda;
import com.example.robert.pruebavolley.Adapter.AdapterFacturas;
import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacturaFragment extends android.app.Fragment {

    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;

    String URL_alumnos= "http://demo.educalinks.com.ec/mobile/main.php";
    SharedPreferences settings;
    TextView noFacturas;
    String nomCol;
    String[] codigoFactura,totalNetoFactura,nombresAlumno,codigoAlumno,fechaEmision,estadoElectronico,numeroFactura,stringIni;
    String codigoFacturaSt,totalNetoFacturaSt,nombresAlumnoSt,codigoAlumnoSt,fechaEmisionSt,estadoElectronicoSt,prefijoSucursalSt,prefijoPuntoVentaSt,numeroFacturaSt, fechaIni;
    final Map<String,String> paramsFacturas = new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    ListView list;
    Button descargar;
    String usuario;
    int itemEscogido = 0;
    String urlPdf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_factura, container, false);

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
        noFacturas = (TextView) rootView.findViewById(R.id.noFacturas);


         usuario = settings .getString("usuario", "null");
        String reprcodigo = settings .getString("codigo", "null");
        String colegio = settings .getString("colegio", "null");

        switch (Integer.parseInt(colegio)) {
            case 1:
                nomCol = "liceopanamericano";
                break;
            case 2:
                nomCol = "ecocab";
                break;
            case 5:
                nomCol = "moderna";
                break;
            case 11:
                nomCol = "ecocabvesp";
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

        paramsFacturas.put("estadoElectronico","AUTORIZADO");
        paramsFacturas.put("fechaemision_ini"," ");
        paramsFacturas.put("fechaemision_fin"," ");
        paramsFacturas.put("reprcodi",reprcodigo);
        paramsFacturas.put("username",usuario);
        paramsFacturas.put("colegio", colegio);
        paramsFacturas.put("opcion","facturas_Autorizadas");
        ShowFacturas(URL_alumnos, paramsFacturas);

        return rootView;
    }

    public void ShowFacturas(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){
                        noFacturas.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    codigoFactura = new String[Keys.size()];
                    totalNetoFactura = new String[Keys.size()];
                    nombresAlumno = new String[Keys.size()];
                    codigoAlumno = new String[Keys.size()];
                    fechaEmision = new String[Keys.size()];
                    estadoElectronico = new String[Keys.size()];
                    numeroFactura = new String[Keys.size()];
                    stringIni = new String[1];

                    for (int i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        codigoFacturaSt = Object.getString("codigoFactura");
                        totalNetoFacturaSt = Object.getString("totalNetoFactura");
                        nombresAlumnoSt = Object.getString("nombresAlumno");
                        codigoAlumnoSt = Object.getString("codigoAlumno");
                        fechaEmisionSt = Object.getString("fechaEmision");
                        estadoElectronicoSt = Object.getString("estadoElectronico");
                        prefijoSucursalSt = Object.getString("prefijoSucursal");
                        prefijoPuntoVentaSt = Object.getString("prefijoPuntoVenta");
                        numeroFacturaSt = Object.getString("numeroFactura");


                        stringIni = fechaEmisionSt.split(" ");
                        String newDateIni = (stringIni[0]);
                        //newDateIni = newDateIni.substring(1, newDateIni.length() - 3);

                        codigoFactura[i] = codigoFacturaSt;
                        totalNetoFactura[i] = totalNetoFacturaSt;
                        nombresAlumno[i] = nombresAlumnoSt;
                        codigoAlumno[i] = codigoAlumnoSt;
                        fechaEmision[i] = newDateIni;
                        estadoElectronico[i] = estadoElectronicoSt;

                        numeroFactura[i] = "FAC" + prefijoSucursalSt + "-" + prefijoPuntoVentaSt + "-" + numeroFacturaSt;
                        //FAC001-001-000114470
                    }

                    AdapterFacturas adapter = new AdapterFacturas(getActivity(),nombresAlumno, numeroFactura, fechaEmision ,totalNetoFactura, nomCol,usuario,numeroFactura);//,imageProfesor);
                    list=(ListView)getActivity().findViewById(R.id.listViewFacturas);
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
