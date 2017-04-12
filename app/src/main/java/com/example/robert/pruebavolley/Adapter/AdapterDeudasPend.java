package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.activity.ConfirmarPagoActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pe.solera.api_payme_android.bean.Address;
import pe.solera.api_payme_android.bean.Commerce;
import pe.solera.api_payme_android.bean.Person;
import pe.solera.api_payme_android.bean.Product;
import pe.solera.api_payme_android.bean.PurchaseInformation;
import pe.solera.api_payme_android.bean.Tax;
import pe.solera.api_payme_android.bean.TransactionInformation;
import pe.solera.api_payme_android.util.Constants;

/**
 * Created by redlinks on 28/11/16.
 */
public class AdapterDeudasPend extends ArrayAdapter {
    Context context;
    private final ArrayList<String> detalle;
    private final ArrayList<String> añoLectivo;
    private final ArrayList<String> fechaCobro;
    private final ArrayList<String> valorTotal;
    private final ArrayList<String> codigoDeuda;
    final Map<Integer,String> deudaCodigoDatos = new TreeMap<Integer,String>();
    Button btnPagar,btnRegTarjetas;
    CheckBox cb;
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsDeudasVenc= new HashMap<String,String>();
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String colegio;
    SharedPreferences settings;
    String[] descripcionDeuda,deudaTotalPendiente,deudaCodigoS, deudasxml;
    ListView listView;
    View dialoglayout2,dialoglayout3;
    String xmlDeudas;
    String XML = "";
    Boolean deudasVencidas = false;

    //boton de pago
    private Commerce commerce = new Commerce();
    private TransactionInformation transactionInformation = new TransactionInformation();
    private ArrayList<Person> personsBilling = new  ArrayList<Person>();
    private ArrayList<Person> personsShipping = new  ArrayList<Person>();
    private ArrayList<Tax> taxes = new  ArrayList<Tax>();
    private ArrayList<Product> products = new  ArrayList<Product>();
    private ArrayList<PurchaseInformation> purchasesInformation = new  ArrayList<PurchaseInformation>();
    //private final String TAG = getClass().getSimpleName();

    private ProgressBar spinner;
    private final String TAG = getClass().getSimpleName();


    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;



    public AdapterDeudasPend(Context context, ArrayList<String> detalle, ArrayList<String> añoLectivo, ArrayList<String> fechaCobro, ArrayList<String> valorTotal, ArrayList<String> codigoDeuda, Button btnPagar)
    {
        super(context, R.layout.lista_deudas_pendientes,detalle);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.detalle = detalle;
        this.añoLectivo = añoLectivo;
        this.fechaCobro = fechaCobro;
        this.valorTotal = valorTotal;
        this.codigoDeuda = codigoDeuda;
        this.btnPagar = btnPagar;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.lista_deudas_pendientes, parent, false);

        TextView deta = (TextView) convertView.findViewById(R.id.Detalle);
        TextView cobro = (TextView) convertView.findViewById(R.id.FechaCobro);
        TextView total = (TextView) convertView.findViewById(R.id.ValorTotal);
        cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        deta.setText(detalle.get(position));
        cobro.setText(fechaCobro.get(position));
        total.setText("$ " + valorTotal.get(position));

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //rowView.setAlpha(0.2f);
        }
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        colegio = settings .getString("colegio", "null");



        cb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {

                    h = new Handler();
                    pd = new TransparentProgressDialog((Activity)context, R.drawable.processdialog);
                    r =new Runnable() {
                        @Override
                        public void run() {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }

                    };
                    pDialog = new ProgressDialog((Activity)context);
                    pDialog.setCancelable(false);

                    pd.show();
                    h.postDelayed(r, 2000);
                    LayoutInflater inflater2 = ((Activity)context).getLayoutInflater();
                    dialoglayout2 = inflater2.inflate(R.layout.popup_deudasvenc, null);

                    paramsDeudasVenc.put("colegio",colegio);
                    paramsDeudasVenc.put("opcion","deudas_pendientes");
                    paramsDeudasVenc.put("deudaCodigo",codigoDeuda.get(position));
                    VerificarDeudasVencidas(URL,paramsDeudasVenc,position);
                    Log.d("enter","Escogio" + detalle.get(position));

                    btnPagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Set<Integer> keys = deudaCodigoDatos.keySet();
                            deudasxml = new String[keys.size()];
                            for(Integer key: keys){
                                System.out.println(key);
                                XML = XML +  "<deuda id=\"" +  key +"\"/>";
                            }
                            if(XML != null){
                                xmlDeudas =   "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><deudas>" +
                                        XML +
                                        "</deudas>";
                            }

                            Intent intent=new Intent(context,ConfirmarPagoActivity.class);
                            intent.putExtra("xml", xmlDeudas);
                            ((Activity) getContext()).startActivityForResult(intent,Constants.RESULT_API);


                        }
                    });
                }
                else{
                    deudaCodigoDatos.remove(Integer.parseInt(codigoDeuda.get(position)));
                    if(position < deudaCodigoDatos.size()){
                        paramsDeudasVenc.put("colegio",colegio);
                        paramsDeudasVenc.put("opcion","deudas_pendientes");
                        paramsDeudasVenc.put("deudaCodigo",deudaCodigoDatos.keySet().toArray()[position].toString());
                        VerificarDeudasVencidasUnchecked(URL,paramsDeudasVenc,position);
                    }




                    //si no ha escogido nada y aplasta el boton Pagar le debe salir error.
                    btnPagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.popup_btnpago);
                            dialog.setTitle("Error");
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    });
                }
                //case 2

            }
        });


        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(cb.isChecked()){
                    btnPagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.popup_btnpago);
                            dialog.setTitle("CHECKBOX");
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    });
                }
                else {
                    btnPagar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(getContext());
                            dialog.setContentView(R.layout.popup_btnpago);
                            dialog.setTitle("Error");
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    });

                }
            }
        });


        return convertView;
    }

    public void VerificarDeudasVencidas(String url, final Map<String,String> json, final int position ){
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialoglayout2.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                      Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");
                    if(JsonAll.length()!=0) {
                        final ArrayList<String> Keys = new ArrayList<String>();
                        for (int t = 0; t < JsonAll.length(); t++) {
                            Keys.add(String.valueOf(t));
                        }

                        descripcionDeuda = new String[Keys.size()];
                        deudaTotalPendiente = new String[Keys.size()];
                        deudaCodigoS = new String[Keys.size()];
                        //estado = new String[Keys.size()];


                        for (int i = 0; i < JsonAll.length(); i++) {
                            final int Index = i;
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            descripcionDeuda[i] = Object.getString("descripcionDeuda");
                            deudaTotalPendiente[i] = Object.getString("deud_totalPendiente");
                            deudaCodigoS[i] = Object.getString("deud_codigo");
                        }
                        for (int i = 0; i < deudaCodigoS.length; i++) {
                            if(deudaCodigoDatos.containsKey(Integer.parseInt(deudaCodigoS[i])) !=  true){

                                AdapterDeudasVencidas adapter = new AdapterDeudasVencidas((Activity)context,descripcionDeuda,deudaTotalPendiente);
                                listView=(ListView) dialoglayout2.findViewById(R.id.ListaDeudasVencidas);
                                listView.setAdapter(adapter);
                                dialoglayout2.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                Dialog d = new AlertDialog.Builder(((Activity)context), AlertDialog.THEME_HOLO_LIGHT)
                                        .setTitle("¡Deuda Pendiente!")
                                        .setNegativeButton("Cancel", null)
                                        .setView(dialoglayout2)
                                        .create();
                                d.show();
                            }
                            else{

                                deudaCodigoDatos.put(Integer.parseInt(codigoDeuda.get(position)),detalle.get(position));
                            }

                        }


                    }
                    else{

                        deudaCodigoDatos.put(Integer.parseInt(codigoDeuda.get(position)),detalle.get(position));
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

    public void VerificarDeudasVencidasUnchecked(String url, final Map<String,String> json, final int position ){
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

                        descripcionDeuda = new String[Keys.size()];
                        deudaTotalPendiente = new String[Keys.size()];
                        deudaCodigoS = new String[Keys.size()];
                        //estado = new String[Keys.size()];


                        for (int i = 0; i < JsonAll.length(); i++) {
                            final int Index = i;
                            final JSONObject Object = JsonAll.getJSONObject(i);
                            descripcionDeuda[i] = Object.getString("descripcionDeuda");
                            deudaTotalPendiente[i] = Object.getString("deud_totalPendiente");
                            deudaCodigoS[i] = Object.getString("deud_codigo");
                        }


                                AdapterDeudasVencidas adapter = new AdapterDeudasVencidas((Activity)context,descripcionDeuda,deudaTotalPendiente);
                                listView=(ListView) dialoglayout2.findViewById(R.id.ListaDeudasVencidas);
                                listView.setAdapter(adapter);
                                dialoglayout2.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                                Dialog d = new AlertDialog.Builder(((Activity)context), AlertDialog.THEME_HOLO_LIGHT)
                                        .setTitle("¡Deuda Pendiente!")
                                        .setNegativeButton("Cancel", null)
                                        .setView(dialoglayout2)
                                        .create();
                                d.show();

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
