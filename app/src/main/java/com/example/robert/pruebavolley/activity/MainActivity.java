package com.example.robert.pruebavolley.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.robert.pruebavolley.Adapter.AdapterColegios;
import com.example.robert.pruebavolley.Clases.AppController;
import com.example.robert.pruebavolley.Clases.getString;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.Services.AlarmReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = "TAG";
    String URL = "http://demo.educalinks.com.ec/mobile/main.php";
    HashMap<String,String> colegios = new HashMap<>();
    HashMap<String,String> colegiosCopy = new HashMap<>();
    String ID,TEXTO;
    EditText username,password;
    JSONObject jsonObject = null;
    Button btnEntrar;
    ProgressDialog pDialog;
    private JSONArray JsonAll;
    CheckBox CheckPassword;
    String result;
    String tag_string_req = "req_login";
    Spinner Institucion;
    ArrayAdapter<CharSequence> adapterPrueba;
    int IdColegio;
    String codigo, nombresRep,apelRep;
    Integer periodo;
    SharedPreferences settings;
    TextInputLayout inputLayoutUsername,inputLayoutPassword;
    final Map<String, String> paramsClientes = new HashMap<String, String>();
    Map<String,String> map;
    String colegio;


    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        h = new Handler();
        pd = new TransparentProgressDialog(this, R.drawable.processdialog);
        r =new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }

        };

        inputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        btnEntrar = (Button) findViewById(R.id.Entrar);
        username = (EditText) findViewById(R.id.editUser);
        password = (EditText) findViewById(R.id.editPassword);
        CheckPassword = (CheckBox) findViewById(R.id.checkBoxPassword);
        Institucion = (Spinner) findViewById(R.id.instituciones);

        paramsClientes.put("opcion", "listar_clientes");
        paramsClientes.put("opci_codi", "310");


        funcion(paramsClientes);
        Institucion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 1; i < 30; i++) {
                    if (parent.getItemAtPosition(position).equals(colegiosCopy.get(Integer.toString(i)))) {
                        //IdColegio = i;
                        //colegio = map.get(i);
                        //Log.d(TAG, "codi cole: " + colegio);
                        IdColegio = i;
                        Log.d(TAG, "codi cole: " + colegio);
                        //Toast.makeText(getApplicationContext(), "id colegio:" + IdColegio + ", colegio: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        btnEntrar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                pd.show();
                h.postDelayed(r, 2000);
                String usernameS = username.getText().toString();
                String passwordS = password.getText().toString();

                final Map<String, String> params = new HashMap<String, String>();
                params.put("colegio", Integer.toString(IdColegio));
                params.put("username", usernameS);
                params.put("password", passwordS);
                params.put("tipo_usua", "2");
                params.put("opcion", "login_representante");
                if (username.length() > 0 && password.length() > 0) {
                    doWebRequestPost(URL, params);
                    settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor1 = settings.edit();
                    editor1.putString("colegio", Integer.toString(IdColegio));
                    editor1.commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese las credenciales", Toast.LENGTH_LONG).show();
                }
            }


        });

        CheckPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (CheckPassword.isChecked()) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        h.removeCallbacks(r);
        if (pd.isShowing() ) {
            pd.dismiss();
        }
        super.onDestroy();
    }

    public void doWebRequestPost(String url, final Map<String,String> json){
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d(TAG, "Login Response: " + response.toString());
                    JSONObject jresponse = new JSONObject(response);
                    String exito = jresponse.getString("exito");


                    if(exito.equals("OK"))
                    {
                        codigo = jresponse.getString("codigo");
                        periodo = jresponse.getInt("periodo");
                        nombresRep = jresponse.getString("nombre");
                        apelRep = jresponse.getString("apellido");
                        Intent intent = new Intent(MainActivity.this, Activity3.class);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("usuario", username.getText().toString());
                        editor.putString("password", password.getText().toString());
                        editor.putString("codigo", codigo);
                        editor.putString("periodo", Integer.toString(periodo));
                        editor.putString("nombreRep", nombresRep);
                        editor.putString("apelRep", apelRep);
                        editor.commit();
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Bienvenido, " + nombresRep +" "+  apelRep, Toast.LENGTH_SHORT).show();
                        limpia_caja();

                    }
                    else if(exito.equals("KO"))
                    {
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Ha ocurrido un error, por favor intente màs tarde", Toast.LENGTH_SHORT).show();
                    Log.d("otro error:",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = json;

                return map;
            }
        };
        AppController.getInstance().getRequestQueue().add(post);

    }

    public void funcion(final Map<String,String> json) {

        colegios.put("0", "SELECCIONE UN COLEGIO");
        getString(new getString.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    jsonObject = new JSONObject(result.toString());
                    JsonAll = jsonObject.getJSONArray("result");
                    for (int i = 0; i < JsonAll.length(); i++) {
                        JSONObject Object = JsonAll.getJSONObject(i);
                        ID = Object.getString("id");
                        Log.d("id:", ID);
                        TEXTO = Object.getString("texto");
                        Log.d("texto:", TEXTO);
                        colegios.put(ID, TEXTO);
                    }
                    ShowColegios(colegios);
                    colegiosCopy = ShowColegios(colegios);

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

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public HashMap<String,String> ShowColegios(HashMap<String,String> colegios){
        map = new TreeMap<String,String>(colegios);
        Collection<String> vals = map.values();
        String[] array = vals.toArray(new String[vals.size()]);
        adapterPrueba = new ArrayAdapter<CharSequence>(this,R.layout.spinner_item,array);
        adapterPrueba.setDropDownViewResource(R.layout.spinner_drop_down);
        Institucion.setAdapter(adapterPrueba);
        Institucion.setSelection(0);
        //Toast.makeText(MainActivity.this, map.get(2), Toast.LENGTH_SHORT).show();
        return colegios;
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
            anim.setDuration(3000);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    public  void limpia_caja()
    {
        username.setText("");
        password.setText("");
    }



}



