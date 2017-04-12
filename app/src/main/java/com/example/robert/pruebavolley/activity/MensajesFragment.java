package com.example.robert.pruebavolley.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Adapter.AdapterMensajesEnt;
import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class MensajesFragment extends Fragment {

    String nomCol;
    final Map<String,String> paramsMensajesEnt = new HashMap<String,String>();
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String profesorcodiNM,tipo,profcodi, nombreProfesor, materia,key, text,mensajeDe,mensajeTipoDe,mensajeTitulo,mensajeDetalle,mensajeFechaEnvio,mensajeFechaLectura,codigoEmisor,codigoRecibo,mensajecodigo;
    String[] nombresListaProfesores,nombresEmisor,codigoDe,codigoPara,tipoEmisor,titulo,detalle,fechaEnvio,fechaLectura, stringFechaEnvio, stringFechaLect, nombreUL,imageURL,mensCodi,detalleTotal,nombreP;
    int i;
    Bitmap b;
    Bitmap[] images;
    ListView list;
    Fragment fragment = null;
    FloatingActionButton enviados,eliminados,nuevoMens,recibidos;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String padre;
    View dialoglayout;
    Button enviar, cancelar;
    TextView nombrePadre,tituloFragment,noMensajes;
    Spinner profesoresSpinner;
    Dialog d;
    HashMap<String, String> profesoresCopy = new HashMap<>();
    HashMap<String, String> profesores = new HashMap<>();
    final Map<String, String> params = new HashMap<String, String>();
    final Map<String, String> paramsMensajeN = new HashMap<String, String>();
    final Map<String, String> paramsMensajesUpdate = new HashMap<String, String>();
    ArrayAdapter<String> adapterProfesores;
    String alumnocodi, pericodi,colegio,reprcodigo ;
    EditText asunto,detallemensaje;
    String opcionFragment;
    String[] nombreCompletos,textoDetalle;
    int j;


    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_mensajes, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar();
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


        noMensajes = (TextView) rootView.findViewById(R.id.noMensajes);
        tituloFragment = (TextView) rootView.findViewById(R.id.tituloFragment);
        enviados = (FloatingActionButton) rootView.findViewById(R.id.fabEnv);
        eliminados = (FloatingActionButton) rootView.findViewById(R.id.fabElim);
        nuevoMens = (FloatingActionButton) rootView.findViewById(R.id.fabNM);
        recibidos = (FloatingActionButton) rootView.findViewById(R.id.fabRecib);
        enviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*fragment = new MensajesEnvsFragment();
                FragmentManager fragmentManager2 = getFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();*/

                opcionFragment = "3";
                editor.putString("opcionfragment", String.valueOf(opcionFragment));
                editor.commit();
                fragment = new MensajesFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
            }
        });
        eliminados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionFragment = "4";
                editor.putString("opcionfragment", String.valueOf(opcionFragment));
                editor.commit();
                fragment = new MensajesFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
            }
        });

        recibidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*
                fragment = new MensajesEntFragment();
                FragmentManager fragmentManager3 = getFragmentManager();
                fragmentManager3.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
                Toast.makeText(rootView.getContext(), "RECIBIDOOOS", Toast.LENGTH_SHORT).show();*/
                opcionFragment = "2";
                tituloFragment.setText("RECIBIDOS");
                editor.putString("opcionfragment", String.valueOf(opcionFragment));
                editor.commit();
                fragment = new MensajesFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
            }
        });
        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        editor = settings.edit();
        reprcodigo = settings .getString("codigo", "null");
        colegio = settings .getString("colegio", "null");
        final String nombreP = settings.getString("nombreRep","null");
        final String apelP= settings.getString("apelRep","null");
        alumnocodi= settings.getString("alumnocodi","null");
        Log.d("alumnocodi",alumnocodi);
        pericodi = settings.getString("periodo", "null");
        padre = nombreP + " " + apelP;

        tipo = settings .getString("tipoEmisor", "null");

        nuevoMens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layout = getActivity().getLayoutInflater();
                dialoglayout = layout.inflate(R.layout.popup_nm, null);
                profesoresSpinner = (Spinner) dialoglayout.findViewById(R.id.listaProfesores);
                enviar = (Button) dialoglayout.findViewById(R.id.buttonEnv);
                cancelar = (Button) dialoglayout.findViewById(R.id.buttonCan);
                asunto = (EditText) dialoglayout.findViewById(R.id.Asunto_popup);
                detallemensaje= (EditText) dialoglayout.findViewById(R.id.mensaje);
                nombrePadre = (TextView) dialoglayout.findViewById(R.id.nombrePadre);
                nombrePadre.setText(padre);

                d = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Nuevo Mensaje")
                        .setView(dialoglayout)
                        .create();
                d.show();

                params.put("alumnocodi", alumnocodi);
                params.put("pericodi", pericodi);
                params.put("colegio", colegio);
                params.put("opcion", "profesores_lista");
                ShowProfesoresSpinner(URL, params);

                profesoresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int i;
                        Set set = profesoresCopy.entrySet();
                        Iterator iter = set.iterator();


                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            for (i = 0; i < profesoresCopy.size(); i++) {
                                if (parent.getItemAtPosition(position).equals(entry.getValue().toString())) {
                                    key = entry.getKey().toString();
                                    editor = settings.edit();
                                    if(entry.getValue().equals("No tiene materias asignadas")){
                                        editor.putString("profesorcodiNM", "0");
                                        editor.commit();
                                    }else{
                                        editor.putString("profesorcodiNM", key);
                                        editor.commit();
                                    }


                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                profesorcodiNM = settings.getString("profesorcodiNM","null");
                if(profesorcodiNM.equals("0")){
                    enviar.setVisibility(View.GONE);
                }

                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profesorcodiNM = settings.getString("profesorcodiNM","null");
                        paramsMensajeN.put("reprcodi",reprcodigo);
                        paramsMensajeN.put("tipoMens","R");
                        if(profesorcodiNM.equals("0")){

                        }else{
                            paramsMensajeN.put("profcodi",profesorcodiNM);
                            paramsMensajeN.put("tipoMensPara","D");
                            paramsMensajeN.put("menstitu", asunto.getText().toString());
                            paramsMensajeN.put("mensdeta", detallemensaje.getText().toString());
                            paramsMensajeN.put("colegio",colegio);
                            paramsMensajeN.put("opcion", "add_message");
                            SendNewMessage(URL,paramsMensajeN);
                            d.dismiss();
                        }

                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();

                    }
                });
            }
        });

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

        opcionFragment = settings.getString("opcionfragment", "null");
        if(opcionFragment.equals("0")){
            paramsMensajesEnt.put("op", "2");
            tituloFragment.setText("RECIBIDOS");
            pd.show();
            h.postDelayed(r, 2000);
        }
        else{

            paramsMensajesEnt.put("op", opcionFragment);
            if(opcionFragment.equals("2")) {
                tituloFragment.setText("RECIBIDOS");
                pd.show();
                h.postDelayed(r, 2000);
            }
            if(opcionFragment.equals("3")) {
                tituloFragment.setText("ENVIADOS");
                pd.show();
                h.postDelayed(r, 1000);
            }
            if(opcionFragment.equals("4")) {
                tituloFragment.setText("ELIMINADOS");
                pd.show();
                h.postDelayed(r, 1000);
            }


        }


        paramsMensajesEnt.put("colegio", colegio);
        paramsMensajesEnt.put("reprcodi", reprcodigo);
        paramsMensajesEnt.put("tipoMens", "R");
        paramsMensajesEnt.put("rowCount", "1");
        paramsMensajesEnt.put("opcion", "mensajes_all");

        ShowMensajesEnt(URL, paramsMensajesEnt);
        return rootView;
    }

    public void ShowMensajesEnt(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){

                        noMensajes.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    nombresEmisor = new String[Keys.size()];
                    tipoEmisor = new String[Keys.size()];
                    titulo = new String[Keys.size()];
                    detalle = new String[Keys.size()];
                    detalleTotal = new String[Keys.size()];
                    fechaEnvio = new String[Keys.size()];
                    fechaLectura = new String[Keys.size()];

                    imageURL = new String[Keys.size()];
                    codigoDe = new String[Keys.size()];
                    codigoPara = new String[Keys.size()];
                    images = new Bitmap[Keys.size()];
                    mensCodi = new String[Keys.size()];
                    textoDetalle = new String[Keys.size()];
                    for (i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        mensajeDe = Object.getString("mensajeNombEmisor");
                        mensajecodigo = Object.getString("mensajeCodigo");
                        mensCodi[i] = mensajecodigo;
                        mensajeTipoDe = Object.getString("mensajeTipoEmisor");
                        tipoEmisor[i] = mensajeTipoDe;
                        mensajeTitulo = Object.getString("mensajeTitulo");
                        titulo[i] = mensajeTitulo;
                        editor.commit();   mensajeDetalle = Object.getString("mensajeDetalle");
                        detalle[i] = mensajeDetalle;
                        text = stripHtml(mensajeDetalle);
                        text = text.replace("\n", " ");
                        textoDetalle[i] = text;

                        mensajeDetalle.replace("<p>", " ");
                        mensajeDetalle.replace("</p>", "<br>");

                        mensajeDetalle = stripHtml(mensajeDetalle);
                        Log.d("text",mensajeDetalle);

                        detalleTotal[i] = mensajeDetalle;
                        Log.d("mensajeDetalle", mensajeDetalle);

                        mensajeFechaEnvio = Object.getString("mensajeFechaEnvio");
                        fechaEnvio[i] = mensajeFechaEnvio;
                        mensajeFechaLectura = Object.getString("mensajeFechaLect");
                        Log.d("fecha de lectura", mensajeFechaLectura);
                        fechaLectura[i] = mensajeFechaLectura;
                        codigoEmisor = Object.getString("codigoDe");
                        codigoDe[i] = codigoEmisor;

                        codigoRecibo = Object.getString("codigoPara");
                        codigoPara[i] = codigoRecibo;

                        Log.d("mensajeDe", mensajeDe);
                        Log.d("mensajeTipoDe", mensajeTipoDe);
                        Log.d("mensajeTitulo", mensajeTitulo);
                        Log.d("mensajeDetalle", mensajeDetalle);
                        Log.d("mensajeFechaEnvio", mensajeFechaEnvio);
                        Log.d("mensajeFechaLectura", mensajeFechaLectura);

                        stringFechaEnvio = mensajeFechaEnvio.split(":");
                        String newDateIni = (stringFechaEnvio[1]);
                        newDateIni = newDateIni.substring(1, newDateIni.length() - 3);
                        fechaEnvio[i] = newDateIni;
                        Log.d("fechaEnvi", fechaEnvio[i]);

                        String[] nombre = mensajeDe.split("\\s+");
                        nombreUL= new String[nombre.length];
                        for (int j = 0; j < nombre.length; j++) {
                            if(!nombre[j].equals("")) {
                                String s = nombre[j].substring(0, 1).toUpperCase();
                                nombreUL[j] = s + nombre[j].substring(1).toLowerCase();
                                if (nombresEmisor[i] == null) {
                                    nombresEmisor[i] = "";
                                }
                                nombresEmisor[i] = nombresEmisor[i] + nombreUL[j] + " ";
                                Log.d("emisooor", nombresEmisor[0]);
                            }
                        }


                        if (!mensajeFechaLectura.equals("null")) {
                            stringFechaLect = mensajeFechaLectura.split(":");
                            String newDateFin = (stringFechaLect[1]);
                            newDateFin = newDateFin.substring(1, newDateFin.length() - 3);
                            fechaLectura[i] = newDateFin;
                            Log.d("fechaLect", fechaLectura[i]);
                        }

                        if (opcionFragment.equals("3")){//enviados

                            if (mensajeTipoDe.equals("Docente")) {
                                imageURL[i] = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/docentes/" + codigoPara[i] + ".jpg";
                                Log.d("imageurl:",imageURL[i]);
                            } else if (mensajeTipoDe.equals("Administrativo")) {
                                imageURL[i] = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/admin/" + codigoPara[i] + ".jpg";
                                Log.d("imageurl:",imageURL[i]);
                            }

                        }else {

                            if (mensajeTipoDe.equals("Docente")) {
                                imageURL[i] = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/docentes/" + codigoDe[i] + ".jpg";
                                Log.d("imageurl:",imageURL[i]);
                            } else if (mensajeTipoDe.equals("Administrativo")) {
                                imageURL[i] = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/admin/" + codigoDe[i] + ".jpg";
                                Log.d("imageurl:",imageURL[i]);
                            }


                        }

                        /*int length = mensajeDetalle.length();


                        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
                            if(length>80){
                                detalle[i] = text.substring(0,50) + "...";
                            }
                            else{
                                detalle[i] =text;
                            }
                        }
                        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                            if(length>30){
                                detalle[i] = text.substring(0,35) + "...";
                            }
                            else{
                                detalle[i] =text;
                            }
                        }
                        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
                            if(length>30){
                                detalle[i] = text.substring(0,40) + "...";
                            }
                            else{
                                detalle[i] =text;
                            }
                        }
                        else {
                            if(length>60){
                                detalle[i] = text.substring(0,80) + "...";
                            }
                            else{
                                detalle[i] =text;
                            }
                        }*/




                        /*if (ImageStorage.checkifImageExists(codigoDe[i])) {
                            File file = ImageStorage.getImage("/" + codigoDe[i] + ".jpg");
                            String path = file.getAbsolutePath();
                            if (path != null) {
                                b = BitmapFactory.decodeFile(path);
                                images[i] = b;
                            }
                        } else {

                            new GetImages(imageURL[i], codigoDe[i], i).execute();

                        }*/
                    }
                    AdapterMensajesEnt adapter = new AdapterMensajesEnt(getActivity().getBaseContext(),nombresEmisor, titulo, textoDetalle,fechaEnvio,fechaLectura);//,imageProfesor);
                    list=(ListView)getActivity().findViewById(R.id.listViewMensajes);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            paramsMensajesUpdate.put("colegio", colegio);
                            paramsMensajesUpdate.put("menscodi", mensCodi[position]);
                            paramsMensajesUpdate.put("opcion", "update_message");

                            ReadMessage(URL, paramsMensajesUpdate);

                            editor.putString("tituloMensaje",titulo[position]);
                            editor.putString("profesor",nombresEmisor[position]);
                            editor.putString("tipoEmisor",tipoEmisor[position]);
                            editor.putString("fechaEnvio",fechaEnvio[position]);
                            editor.putString("detalle",detalleTotal[position]);
                            editor.putString("mensajeCod",mensCodi[position]);
                            editor.putString("codigoDe",codigoDe[position]);
                            editor.putString("imagesUrl",imageURL[position]);
                            editor.putString("fragment","RECIBIDOS");

                            editor.commit();
                            fragment = new LeerMensajeFragment();
                            FragmentManager fragmentManager5 = getFragmentManager();
                            fragmentManager5.beginTransaction().replace(R.id.frame, fragment,"LeerMensaje").addToBackStack("Mensajes").commit();
                            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.d("errorIn", e.toString());
                    //Log.d("otro error:",e.getMessage().toString());
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


    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private int i_;


        private GetImages(String requestUrl, String _imagename_, int i ) {
            this.requestUrl = requestUrl;
            this.imagename_ = _imagename_;
            this.i_ = i;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                java.net.URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                images[i_] = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                images[i_] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
                Log.d("error en getimages:",ex.toString());
            }
            return images[i_];
        }

        @Override
        protected void onPostExecute(Object o) {
            /*if (!ImageStorage.checkifImageExists(imagename_)) {
                ImageStorage.saveToSdCard(images[i_], imagename_);
            }*/
        }
    }

    public String stripHtml(String html) {

        return Html.fromHtml(html).toString();
    }

    public void ShowProfesoresSpinner(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());
                    JSONObject jresponse = new JSONObject(response);
                    JsonAll = jresponse.getJSONArray("result");
                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    if(JsonAll.length() == 0){

                        profesores.put("0","No tiene materias asignadas");
                    }

                    nombreP = new String[Keys.size()];
                    nombresListaProfesores = new String[Keys.size()];
                    nombreCompletos = new String[Keys.size()];
                    for (int i = 0; i < JsonAll.length(); i++) {
                        JSONObject Object = JsonAll.getJSONObject(i);
                        profcodi = Object.getString("profcodi");
                        nombreProfesor = Object.getString("nombreProfesor");
                        materia = Object.getString("materia");


                        String[] nombres = nombreProfesor.split("\\s+");

                        for (j = 0; j < nombres.length; j++) {
                            String s = nombres[j].substring(0, 1).toUpperCase();
                            nombreP[j] = s + nombres[j].substring(1).toLowerCase();

                            /*if(nombresListaProfesores[i] == null){
                                nombresListaProfesores[i] = "";
                            }
                            nombresListaProfesores[i] = nombresListaProfesores[i] + nombreP[j]+ " " ;*/
                        }

                            Log.d("nombrelenght", String.valueOf(nombreP.length));
                            if(j==4){
                                nombreCompletos[i] = nombreP[2] + " " + nombreP[0];
                            }
                        else{
                                nombreCompletos[i] = nombreP[1] + " " + nombreP[0];
                            }

                        profesores.put(profcodi, nombreCompletos[i] + " (" + materia + ")");
                    }


                    ShowProfesores(profesores);
                    profesoresCopy = ShowProfesores(profesores);


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("otro error:", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGG", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = json;

                return map;
            }
        };
        queue.add(post);
    }

    public HashMap<String, String> ShowProfesores(HashMap<String, String> alumnos) {
        Map<String, String> map = new TreeMap<String, String>(alumnos);
        Collection<String> vals = map.values();
        String[] array = vals.toArray(new String[vals.size()]);
        adapterProfesores = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_profesor, array);
        adapterProfesores.setDropDownViewResource(R.layout.spinner_drop_down_profesor);
        profesoresSpinner.setAdapter(adapterProfesores);
        return alumnos;
    }

    public void SendNewMessage(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "New Message: " + response.toString());

                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("otro error:", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGG", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = json;

                return map;
            }
        };
        queue.add(post);
    }

    public void ReadMessage(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "message Updated " + response.toString());

                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("otro error:", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAGG", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = json;

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

    public String LowCase (String nombrePa, String apellPa){
        String[] nombre,apellido;
        String newNom, newApel,newNom1, newApel1;

        nombre = nombrePa.split("\\s+");
        apellido = apellPa.split("\\s+");
        newNom1 = nombre[0].substring(0, 1).toUpperCase();
        newNom = newNom1 + nombre[0].substring(1).toLowerCase();

        newApel1 = apellido[0].substring(0, 1).toUpperCase();
        newApel = newApel1 + apellido[0].substring(1).toLowerCase();


        String padreNombres = newNom + " " + newApel;

        return padreNombres;
    }

}
