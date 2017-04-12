package com.example.robert.pruebavolley.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.robert.pruebavolley.Adapter.AdapterBtnRecibos;
import com.example.robert.pruebavolley.Adapter.AdapterDeudasCancelar;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pe.solera.api_payme_android.bean_response.PayMeResponse;
import pe.solera.api_payme_android.util.Constants;

public class RespuestaPagoActivity extends AppCompatActivity {

    TextView totalPagarExit,exitoso,fallido,mensajeFallo,recibos;
    LinearLayout layoutFail;
    Button regresar;
    ImageView exitosoimg,fallidoimg;
    ListView listViewRecibos;
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String[] pagos;
    SharedPreferences settings;
    ListView list;
    String nomCol="", colegio="" , monto="" , ponCode="";
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    final Map<String,String> paramsRecibos= new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_pago);

        settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        nomCol = settings.getString("nomCol", "null");
        colegio = settings.getString("colegio", "null");
        monto = settings.getString("monto", "null");
        ponCode = settings.getString("ponCode", "null");

        Intent intent = getIntent();
        String response = intent.getExtras().getString("response");
        String error = intent.getExtras().getString("error");
        String respuesta = intent.getExtras().getString("respuesta");
        String nombre = settings.getString("nombre","null");
        String apellido = settings.getString("apellido","null");
        String totalPagar = settings.getString("totalPagar","null");

        if(response != null) {
            String jsonE = removeWords(response, "PayMeResponse");
            //        String jsonE = "{errorCode='000', errorMessage='Successfully Operation.', date='2017/03/07', hour='09:15:28', operationNumber='30008', idTransaction='30008', codAsoCardHolderWallet='5--567--4050', cardNumber='0051'}";

            JSONObject json = null;
            try {
                json = new JSONObject(jsonE);
                String errorCode = json.getString("errorCode");
                String errorMessage = json.getString("errorMessage");
                String operationNumber = json.getString("operationNumber");
                String cardNumber = json.getString("cardNumber");

                paramsRecibos.put("colegio", colegio);
                paramsRecibos.put("opcion", "actualiza_boton_pago");
                paramsRecibos.put("authorizationCode", errorCode);
                paramsRecibos.put("errorCode", errorCode);
                paramsRecibos.put("errorMessage", errorMessage);
                paramsRecibos.put("cardNumber", cardNumber);
                paramsRecibos.put("purchaseOperationNumber", ponCode);
                paramsRecibos.put("purchaseAmount", monto);
                ShowRecibosPagoActualiza(URL, paramsRecibos);
            } catch (JSONException e) {
                e.printStackTrace();
                e.toString();
            }
        }
        totalPagarExit = (TextView) findViewById(R.id.totalPagarExit);
        mensajeFallo = (TextView) findViewById(R.id.mensajeFallo);
        listViewRecibos = (ListView) findViewById(R.id.listViewRecibos);
        exitoso = (TextView) findViewById(R.id.exito);
        fallido = (TextView) findViewById(R.id.fallo);
        recibos = (TextView) findViewById(R.id.recibos);
        exitosoimg = (ImageView) findViewById(R.id.exitoimg);
        fallidoimg = (ImageView) findViewById(R.id.falloimg);
        layoutFail = (LinearLayout) findViewById(R.id.layoutFail);
        regresar = (Button) findViewById(R.id.regresar);


        if(respuesta != null) {
            if (respuesta.equals("1")) {
                exitoso.setVisibility(View.VISIBLE);
                exitosoimg.setVisibility(View.VISIBLE);
                listViewRecibos.setVisibility(View.VISIBLE);
                totalPagarExit.setVisibility(View.VISIBLE);
                recibos.setVisibility(View.VISIBLE);
                totalPagarExit.setText("$ " + totalPagar);
            } else {
                fallido.setVisibility(View.VISIBLE);
                fallidoimg.setVisibility(View.VISIBLE);
                layoutFail.setVisibility(View.VISIBLE);
                mensajeFallo.setText("Lo sentimos, su pago no se ha completado. Por favor compruebe que su tarjeta está operativa y que los dígitos introducidos sean correctos.");
            }
        }else
        {
            fallido.setVisibility(View.VISIBLE);
            fallidoimg.setVisibility(View.VISIBLE);
            layoutFail.setVisibility(View.VISIBLE);
            mensajeFallo.setText("Lo sentimos, su pago no se ha completado. Por favor compruebe que su tarjeta está operativa y que los dígitos introducidos sean correctos.");
        }


        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new HomeFragment ()).commit();*/
                Intent i = new Intent(RespuestaPagoActivity.this,Activity3.class);
                startActivity(i);
            }
        });

    }

    public void Home() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagHome").commit();
    }

    public void ShowRecibosPagoActualiza(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    pagos = new String[JsonAll.length()];

                    for (int i = 0; i < JsonAll.length(); i++)
                    {
                        final JSONObject Object = JsonAll.getJSONObject(i);
                        String pagosS = Object.getString("pagos");
                        pagos = pagosS.split(";");

                    }

                    AdapterBtnRecibos adapter = new AdapterBtnRecibos(RespuestaPagoActivity.this,pagos,nomCol);
                    list=(ListView) findViewById(R.id.listViewRecibos);
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

    public static String removeWords(String word ,String remove) {
        return word.replace(remove,"");
    }

}
