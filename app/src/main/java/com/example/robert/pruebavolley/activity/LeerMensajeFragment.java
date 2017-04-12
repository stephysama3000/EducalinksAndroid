package com.example.robert.pruebavolley.activity;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

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


public class LeerMensajeFragment extends Fragment {

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    TextView tituloMensaje,profesorMensaje,fechaMensaje,mensaje,tituloPage;
    ImageView emisor;
    Bitmap b;
    Bitmap image;
    Fragment fragment = null;
    FloatingActionButton enviados,eliminados,nuevoMens,recibidos, btn_back;
    ImageView responder,eliminar;
    View dialoglayout;
    Spinner profesoresSpinner;
    Button enviar, cancelar;
    TextView para,dePadre;
    String profesorcodiNM,padre,reprcodigo,codigomensaje,codigoEmisor,colegio,tipo,alumnocodi,pericodi,profcodi,nombreProfesor,materia, key ;
    String [] nombreCompletos,nombreP,nombresListaProfesores;
    EditText asunto,detallemensaje;
    Dialog d;
    final Map<String, String> params = new HashMap<String, String>();
    final Map<String, String> paramsEliminar = new HashMap<String, String>();
    final Map<String, String> paramsMensajeN = new HashMap<String, String>();
    String URL= "http://app.educalinks.com.ec/mobile/main.php";
    TextView nombrePadre;
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    HashMap<String, String> profesoresCopy = new HashMap<>();
    HashMap<String, String> profesores = new HashMap<>();
    ArrayAdapter<String> adapterProfesores;
    String opcionFragment;
    int j;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_leer_mensaje, container, false);
        enviados = (FloatingActionButton) rootView.findViewById(R.id.fabEnv);
        eliminados = (FloatingActionButton) rootView.findViewById(R.id.fabElim);
        nuevoMens = (FloatingActionButton) rootView.findViewById(R.id.fabNM);
        recibidos = (FloatingActionButton) rootView.findViewById(R.id.fabRecib);
        enviados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            public void onClick(View v) {
                opcionFragment = "2";
                editor.putString("opcionfragment", String.valueOf(opcionFragment));
                editor.commit();
                fragment = new MensajesFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
            }
        });


        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        editor = settings.edit();
        String titulo = settings .getString("tituloMensaje", "null");
        alumnocodi = settings .getString("alumnocodi", "null");
        pericodi = settings .getString("periodo", "null");
        String profesor = settings .getString("profesor", "null");
        tipo = settings .getString("tipoEmisor", "null");
        String fecha = settings .getString("fechaEnvio", "null");
        String detalle = settings .getString("detalle", "null");

        String imagesUrl = settings .getString("imagesUrl", "null");
        String fragmentde = settings.getString("fragment","null");
        String nombreP = settings.getString("nombreRep","null");
        String apelP= settings.getString("apelRep","null");
        reprcodigo = settings .getString("codigo", "null");
        codigomensaje = settings .getString("mensajeCod", "null");//copdigo del mensaje
        codigoEmisor = settings .getString("codigoDe", "null");//codigo a quien voy a responder y de quin recibi
        colegio = settings .getString("colegio", "null");
        padre = nombreP + " " + apelP;

        tituloMensaje = (TextView) rootView.findViewById(R.id.tituloMensaje);
        profesorMensaje = (TextView) rootView.findViewById(R.id.emisor);
        fechaMensaje = (TextView) rootView.findViewById(R.id.fechaEnvio);
        mensaje = (TextView) rootView.findViewById(R.id.mensaje);
        emisor = (ImageView) rootView.findViewById(R.id.emisor_image);
        tituloPage = (TextView) rootView.findViewById(R.id.tituloPage);
        btn_back = (FloatingActionButton) rootView.findViewById(R.id.btn_back);
        responder = (ImageView) rootView.findViewById(R.id.responder);
        eliminar = (ImageView) rootView.findViewById(R.id.eliminar);
        final String de = profesor + "(" + tipo + ")";


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        tituloMensaje.setText(titulo);
        profesorMensaje.setText(de);
        fechaMensaje.setText(fecha);
        mensaje.setText(detalle);
        opcionFragment = settings.getString("opcionfragment", "null");
        if(opcionFragment.equals("0"))
            tituloPage.setText("RECIBIDOS");
        if(opcionFragment.equals("2"))
            tituloPage.setText("RECIBIDOS");
        if(opcionFragment.equals("3"))
            tituloPage.setText("ENVIADOS");
        if(opcionFragment.equals("4")) {
            tituloPage.setText("ELIMINADOS");
            responder.setVisibility(View.GONE);
            eliminar.setVisibility(View.GONE);
        }

        Log.d("tipo",tipo);

        if(tipo.equals("Administrativo")){
            responder.setVisibility(View.GONE);

        }

        /*nuevoMens.setOnClickListener(new View.OnClickListener() {
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
                        .setTitle("NuevoMensaje")
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
                                    editor.putString("profesorcodiNM", key);
                                    editor.commit();

                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        paramsMensajeN.put("reprcodi",reprcodigo);
                        paramsMensajeN.put("tipoMens","R");
                        paramsMensajeN.put("profcodi",codigoEmisor);
                        paramsMensajeN.put("tipoMensPara",tipo);
                        paramsMensajeN.put("menstitu", asunto.getText().toString());
                        paramsMensajeN.put("mensdeta", detallemensaje.getText().toString());
                        paramsMensajeN.put("colegio",colegio);
                        paramsMensajeN.put("opcion", "add_message");
                        SendNewMessage(URL,paramsMensajeN);
                        d.dismiss();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();

                    }
                });
            }
        });*/
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
                                    editor.putString("profesorcodiNM", key);
                                    editor.commit();

                                }
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profesorcodiNM = settings.getString("profesorcodiNM","null");
                        paramsMensajeN.put("reprcodi",reprcodigo);
                        paramsMensajeN.put("tipoMens","R");
                        paramsMensajeN.put("profcodi",profesorcodiNM);
                        paramsMensajeN.put("tipoMensPara","D");
                        paramsMensajeN.put("menstitu", asunto.getText().toString());
                        paramsMensajeN.put("mensdeta", detallemensaje.getText().toString());
                        paramsMensajeN.put("colegio",colegio);
                        paramsMensajeN.put("opcion", "add_message");
                        SendNewMessage(URL,paramsMensajeN);
                        d.dismiss();
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

        responder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layout = getActivity().getLayoutInflater();
                dialoglayout = layout.inflate(R.layout.popup_nm, null);
                profesoresSpinner = (Spinner) dialoglayout.findViewById(R.id.listaProfesores);
                profesoresSpinner.setVisibility(View.GONE);
                para = (TextView) dialoglayout.findViewById(R.id.paramensaje);
                para.setVisibility(View.VISIBLE);
                enviar = (Button) dialoglayout.findViewById(R.id.buttonEnv);
                cancelar = (Button) dialoglayout.findViewById(R.id.buttonCan);
                asunto = (EditText) dialoglayout.findViewById(R.id.Asunto_popup);
                detallemensaje= (EditText) dialoglayout.findViewById(R.id.mensaje);

                dePadre = (TextView) dialoglayout.findViewById(R.id.nombrePadre);
                dePadre.setText(padre);

                para.setText(de);

                d = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Responder")
                        .setView(dialoglayout)
                        .create();
                d.show();



                enviar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        params.put("reprcodi",reprcodigo);
                        params.put("tipoMens","R");
                        params.put("profcodi",codigoEmisor);
                        params.put("tipoMensPara",tipo);
                        params.put("menstitu", asunto.getText().toString());
                        params.put("mensdeta", detallemensaje.getText().toString());
                        params.put("colegio",colegio);
                        params.put("opcion", "add_message");
                        SendNewMessage(URL,params);
                        d.dismiss();
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

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paramsEliminar.put("menscodi",codigomensaje);
                paramsEliminar.put("opcion","erase_message");
                paramsEliminar.put("colegio",colegio);
                EraseMessage(URL,paramsEliminar);
                getFragmentManager().popBackStack();
            }
        });

        if (ImageStorage.checkifImageExists(codigoEmisor)) {
            File file = ImageStorage.getImage("/" + codigoEmisor + ".jpg");
            String path = file.getAbsolutePath();
            if (path != null) {
                b = BitmapFactory.decodeFile(path);
                image = b;
                emisor.setImageBitmap(b);
            }
        } else {

            new GetImages(imagesUrl, codigoEmisor).execute();
            emisor.setImageBitmap(image);
        }
        return rootView;
    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;


        private GetImages(String requestUrl, String _imagename_) {
            this.requestUrl = requestUrl;
            this.imagename_ = _imagename_;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                java.net.URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                image = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                image = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
                Log.d("error en getimages:",ex.toString());
            }

            return image;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!ImageStorage.checkifImageExists(imagename_)) {
                ImageStorage.saveToSdCard(image, imagename_);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.getFragmentManager().popBackStack();

        return true;
    }

    public void SendNewMessage(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());

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

    public void EraseMessage(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "Login Response: " + response.toString());

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
}
