package com.example.robert.pruebavolley.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import com.example.robert.pruebavolley.Adapter.AdapterDeudasCancelar;
import com.example.robert.pruebavolley.Adapter.AdapterDeudasPend;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pe.solera.api_payme_android.bean.Address;
import pe.solera.api_payme_android.bean.Commerce;
import pe.solera.api_payme_android.bean.Person;
import pe.solera.api_payme_android.bean.Product;
import pe.solera.api_payme_android.bean.PurchaseInformation;
import pe.solera.api_payme_android.bean.Tax;
import pe.solera.api_payme_android.bean.TransactionInformation;
import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.classes.activities.LoadingAct;
import pe.solera.api_payme_android.util.Constants;
import pe.solera.api_payme_android.util.Util;

public class ConfirmarPagoActivity extends AppCompatActivity {

    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsDatosPag= new HashMap<String,String>();
    String colegio,alumcodi,reprcodi,ponCode;
    SharedPreferences settings;
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String reprNomb,reprApel,reprEmail,reprDomi,reprTelf;
    TextView reprnomb, reprmail,reprdireccion,ciudad,estado,reprpais,reprtelefono,totalPagar;
    String[] deudTotalPendiente,deudaCodigo,prodNombre, deudasACancelar;
    double totalApagar = 0;
    HashMap<Double,String> deudasAcancelar = new HashMap<Double,String>();
    ListView list;
    Button PagarPension;
    int IdAcquirer,IdCommerce;
    String CommerceName;
    int totalPurchase = 0;
    int Respuesta = 0;//1 es exitoso y 0 con error
    int item = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String xml = "";
    Integer totalAmount = 0 ;

    //dialog
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;

    //ValuesToSend
    private Commerce commerce = new Commerce();
    private TransactionInformation transactionInformation = new TransactionInformation();
    private ArrayList<Person> personsBilling = new  ArrayList<Person>();
    private ArrayList<Person> personsShipping = new  ArrayList<Person>();
    private ArrayList<Tax> taxes = new  ArrayList<Tax>();
    private ArrayList<Product> products = new  ArrayList<Product>();
    private ArrayList<PurchaseInformation> purchasesInformation = new  ArrayList<PurchaseInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmar_pago);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //getSupportActionBar().setLogo(R.drawable.redlinks);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.btn_back);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4084b9")));

        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        Intent intent = getIntent();
        xml = intent.getExtras().getString("xml");

        reprnomb = (TextView)findViewById(R.id.reprnomb);
        reprmail = (TextView)findViewById(R.id.reprmail);
        reprdireccion = (TextView)findViewById(R.id.reprdireccion);
        ciudad = (TextView)findViewById(R.id.ciudad);
        reprpais = (TextView)findViewById(R.id.reprpais);
        reprtelefono = (TextView)findViewById(R.id.reprtelefono);
        totalPagar = (TextView)findViewById(R.id.totalPagar);
        PagarPension = (Button) findViewById(R.id.buttonPagarPension);

        colegio = settings .getString("colegio", "null");
        alumcodi = settings .getString("alumnocodi", "null");
        reprcodi = settings .getString("codigo", "null");
        IdAcquirer = settings.getInt("IdAcquirer",0);
        IdCommerce = settings.getInt("IdCommerce",0);
        CommerceName = settings.getString("CommerceName","null");


        paramsDatosPag.put("opcion","boton_pagos_deudas");
        paramsDatosPag.put("colegio",colegio);
        paramsDatosPag.put("alumnocodi",alumcodi);
        paramsDatosPag.put("reprcodi",reprcodi);
        paramsDatosPag.put("deudaCodigo", xml);
        ShowDatosPagos(URL,paramsDatosPag);



        PagarPension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideSoftKeyboard(ConfirmarPagoActivity.this);

                setValuesToSend();
                getIntent().removeExtra("xml");
                Intent intent = new Intent(getApplicationContext(), LoadingAct.class);
                intent.putExtra("commerce", commerce);
                intent.putExtra("transactionInformation", transactionInformation);
                intent.putExtra("personsBilling", personsBilling);
                intent.putExtra("personsShipping", personsShipping);
                intent.putExtra("taxes", taxes);
                intent.putExtra("products", products);
                intent.putExtra("purchasesInformation", purchasesInformation);
                startActivityForResult(intent, Constants.RESULT_API);
            }
        });

    }

    public void ShowDatosPagos(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    h = new Handler();
                    pd = new TransparentProgressDialog(ConfirmarPagoActivity.this, R.drawable.processdialog);
                    r =new Runnable() {
                        @Override
                        public void run() {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                        }

                    };
                    pDialog = new ProgressDialog(ConfirmarPagoActivity.this);
                    pDialog.setCancelable(false);

                    pd.show();
                    h.postDelayed(r, 2000);
                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    deudTotalPendiente = new String[JsonAll.length()];
                    deudaCodigo = new String[JsonAll.length()];
                    prodNombre = new String[JsonAll.length()];
                    deudasACancelar = new String[JsonAll.length()];
                    products.clear();
                    for (int i = 0; i < JsonAll.length(); i++)
                    {
                        final JSONObject Object = JsonAll.getJSONObject(i);
                        deudaCodigo[i] = Object.getString("deud_codigo");
                        ponCode = Object.getString("pon_code");
                        prodNombre[i] = Object.getString("prod_nombre");
                        deudTotalPendiente[i] = Object.getString("deud_totalPendiente");
                        reprNomb = Object.getString("repr_nomb");
                        reprApel = Object.getString("repr_apel");
                        reprEmail = Object.getString("repr_email");
                        reprDomi = Object.getString("repr_domi");
                        reprTelf = Object.getString("repr_telf");
                        totalApagar = totalApagar + Double.parseDouble(deudTotalPendiente[i]);
                        //int total=0;
                        //total = totalPurchase * 100;

                        //deudasAcancelar.put(Double.parseDouble(deudTotalPendiente[i]),prodNombre[i]);
                        deudasACancelar[i] = prodNombre[i] + "( $" +  deudTotalPendiente[i]  + " )";




                        Product product = new Product();
                        item = item + 1;
                        product.setItem(String.valueOf(item));
                        product.setCode(deudaCodigo[i]);
                        product.setName(prodNombre[i]);
                        product.setUnitPrice(deudTotalPendiente[i]);
                        product.setQuantity("1");

                        products.add(product);


                    }

                    totalAmount = (int)(totalApagar * 100);
                    pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    editor = pref.edit();
                    editor.putString("nombre",reprNomb);
                    editor.putString("apellido",reprApel);
                    editor.putString("monto",String.valueOf(totalApagar));
                    editor.putString("ponCode",ponCode);
                    reprnomb.setText(reprNomb + " " + reprApel);
                    reprmail.setText(reprEmail);
                    reprdireccion.setText(reprDomi);
                    ciudad.setText("Guayas - Guayaquil");
                    reprpais.setText("Ecuador");
                    reprtelefono.setText(reprTelf);
                    totalPagar.setText(String.format( "$" + "%.2f", totalApagar ));
                    editor.putString("totalPagar",String.valueOf(totalApagar));
                    editor.commit();
                    AdapterDeudasCancelar adapter = new AdapterDeudasCancelar(ConfirmarPagoActivity.this,deudasACancelar);
                    list=(ListView) findViewById(R.id.listaDeudasACancelar);
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

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setValuesToSend() {
        commerce.setCommerceName(CommerceName);
        commerce.setCommerceLogo("logo_commerce");
        commerce.setCommerceColor("000000");

        /*transactionInformation.setIdAcquirer(String.valueOf(IdAcquirer));
        transactionInformation.setIdEntCommerce(String.valueOf(IdCommerce));
        transactionInformation.setCodAsoCardHolderWallet("");//no se que e esto
        transactionInformation.setCodCardHolderCommerce("ABC120");//no se q es esto
        transactionInformation.setMail(reprEmail);
        transactionInformation.setNameCardholder(reprNomb);
        transactionInformation.setLastNameCardholder(reprApel);*/

        transactionInformation.setIdAcquirer(String.valueOf(IdAcquirer));
        //al poner el IdCommerce de LAMODERNA no va a la sgte activity
        transactionInformation.setIdEntCommerce(String.valueOf(IdCommerce));
        transactionInformation.setCodAsoCardHolderWallet("");
        transactionInformation.setCodCardHolderCommerce(reprcodi);
        transactionInformation.setMail(reprEmail);
        transactionInformation.setNameCardholder(reprNomb);
        transactionInformation.setLastNameCardholder(reprApel);

        //
        personsBilling.clear();

        Person personBilling = new Person();
        personBilling.setFirstName(reprNomb);
        personBilling.setLastName(reprApel);

        Address addressBilling = new Address();
        addressBilling.setAddress(reprDomi);
        addressBilling.setCity("Guayaquil");
        addressBilling.setState("Guayas");
        addressBilling.setCountryCode("EC");
        addressBilling.setZipCode("090150");
        addressBilling.setPhoneNumber(reprTelf);
        addressBilling.setEmail(reprEmail);

        ArrayList<Address> addressesBilling = new ArrayList<Address>();
        addressesBilling.add(addressBilling);
        personBilling.setAddresses(addressesBilling);

        personsBilling.add(personBilling);
        //

        //
        personsShipping.clear();

        Person personShipping = new Person();
        personShipping.setFirstName(reprNomb);
        personShipping.setLastName(reprApel);

        Address addressShipping = new Address();
        addressShipping.setAddress(reprDomi);
        addressShipping.setCity("Guayaquil");
        addressShipping.setState("Guayas");
        addressShipping.setCountryCode("EC");
        addressShipping.setZipCode("090150");
        addressShipping.setPhoneNumber(reprTelf);
        addressShipping.setEmail(reprEmail);

        ArrayList<Address> addressesShipping = new ArrayList<Address>();
        addressesShipping.add(addressShipping);
        personShipping.setAddresses(addressesShipping);

        personsShipping.add(personShipping);
        //

        purchasesInformation.clear();

        PurchaseInformation purchaseInformation = new PurchaseInformation();
        purchaseInformation.setCurrencyCode("840");
        purchaseInformation.setPurchaseAmount(totalAmount.toString());//colocar el amount
        purchaseInformation.setOperationNumber(ponCode);
        purchaseInformation.setCallerPhoneNumber("999111999");//no se que es
        purchaseInformation.setTerminalCode("00000000");
        purchaseInformation.setIpAddress(getLocalIpAddress());

        purchasesInformation.add(purchaseInformation);
        //
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("TAG", "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("TAG", ex.toString());
        }
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }
    // Result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RESULT_API) {
                boolean successful = data.getBooleanExtra("successful", false);
                PayMeResponse payMeResponse = (PayMeResponse) data.getSerializableExtra("payMeResponse");

                if (successful) {
                    Log.d("payOK", "pay - ok\n" + payMeResponse.toString());
                    Respuesta = 1;
                    Intent intent=new Intent(getApplicationContext(),RespuestaPagoActivity.class);
                    intent.putExtra("response", payMeResponse.toString());
                    intent.putExtra("respuesta", Integer.toString(Respuesta));
                    this.startActivityForResult(intent,Constants.RESULT_API);
                } else {
                    if (payMeResponse != null) {
                        Log.d("payError", "pay - error\n" + payMeResponse.getErrorCode());
                        Respuesta = 0;
                        Intent intent=new Intent(getApplicationContext(),RespuestaPagoActivity.class);
                        intent.putExtra("response", payMeResponse.toString());
                        intent.putExtra("error", payMeResponse.getErrorCode());
                        intent.putExtra("respuesta", Integer.toString(Respuesta));
                        this.startActivityForResult(intent,Constants.RESULT_API);
                    } else {
                        Log.d("payError", "pay - error");
                        Respuesta = 0;
                        Intent intent=new Intent(getApplicationContext(),RespuestaPagoActivity.class);
                        intent.putExtra("response", payMeResponse.toString());
                        intent.putExtra("error", payMeResponse.getErrorCode());
                        intent.putExtra("respuesta", Integer.toString(Respuesta));
                        this.startActivityForResult(intent,Constants.RESULT_API);
                    }
                }
            }
        } else {
            Log.d("payError", "pay - error, no existe data para generar el objeto PaymeResponse");
            Respuesta = 0;
            Intent intent=new Intent(getApplicationContext(),RespuestaPagoActivity.class);
            intent.putExtra("error", "pay - error, no existe data para generar el objeto PaymeResponse");
            intent.putExtra("respuesta", Respuesta);
            intent.putExtra("nombre",reprNomb);
            intent.putExtra("apellido", reprApel);
            this.startActivityForResult(intent,Constants.RESULT_API);
        }


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
