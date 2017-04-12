package com.example.robert.pruebavolley.activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.robert.pruebavolley.Adapter.AdapterAgenda;
import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.widget.TableLayout.*;


public class AgendaFragment extends Fragment {

    String URL_alumnos= "http://app.educalinks.com.ec/mobile/main.php";
    private String TAG = "TAG";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    final Map<String,String> paramsAgenda = new HashMap<String,String>();
    String codigoAgenda,tituloAgenda,detalleAgenda,fechaIniAgenda,fechaFinAgenda,detalleMateria,nombreProfesor,codigoProfesor;
    String[] stringIni,stringFin,titulos,deta,profesor,detaMateria,fechaIni,fechaFin, fechaEntera,tituloAgendaPequenio,codigoProf,imageUrl;
    TextView titAgenda,detaAgenda,Profesor,fechaInicio,fechaFinal,NoAgenda;
    SharedPreferences settings;
    int i;
    String nomCol;
    ListView list;
    Bitmap b;
    Bitmap[] img_prof;
    ImageView profesorImg;
    int j=0;

    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        this.setRetainInstance(true);
        Log.d("mensaje", "oncreateview");

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

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        NoAgenda = (TextView) rootView.findViewById(R.id.noAgenda);
        String usuario = settings .getString("usuario", "null");
        String password = settings .getString("password", "null");
        String colegio = settings .getString("colegio", "null");
        String reprcodigo = settings .getString("codigo", "null");
        String periodo = settings .getString("periodo", "null");
        String alumnocodi = settings .getString("alumnocodi", "null");

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

        paramsAgenda.put("colegio", colegio);
        paramsAgenda.put("username", usuario);
        paramsAgenda.put("password", password);
        paramsAgenda.put("tipo_usua", "2");
        paramsAgenda.put("reprcodi", reprcodigo);
        paramsAgenda.put("pericodi", periodo);
        paramsAgenda.put("alumnocodi", alumnocodi);
        paramsAgenda.put("opcion", "mostrar_agenda");
        //pd.show();
        //h.postDelayed(r, 2000);
        ShowAgenda(URL_alumnos, paramsAgenda);

        return rootView;
    }


    public void ShowAgenda(String url, final Map<String,String> json){
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){

                        NoAgenda.setVisibility(View.VISIBLE);
                    }

                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    titulos = new String[Keys.size()];
                    deta = new String[Keys.size()];
                    profesor = new String[Keys.size()];
                    detaMateria = new String[Keys.size()];
                    fechaIni = new String[Keys.size()];
                    fechaFin = new String[Keys.size()];
                    fechaEntera = new String[Keys.size()];
                    codigoProf = new String[Keys.size()];
                    tituloAgendaPequenio = new String[Keys.size()];
                    imageUrl = new String[Keys.size()];
                    img_prof = new Bitmap[Keys.size()];

                       for (i = 0; i < JsonAll.length(); i++) {
                           final int Index = i;
                           final JSONObject Object = JsonAll.getJSONObject(i);

                           codigoAgenda = Object.getString("codigoAgenda");

                           tituloAgenda = Object.getString("tituloAgenda");


                           detalleAgenda = Object.getString("detalleAgenda");
                           if(!detalleAgenda.equals("")) {
                               deta[i] = detalleAgenda;
                           }else{
                               deta[i] = "NA";

                           }

                           detalleMateria = Object.getString("detalleMateria");
                           detaMateria[i] = detalleMateria;

                           nombreProfesor = Object.getString("nombreProfesor");
                           profesor[i] = nombreProfesor;
                           codigoProfesor = Object.getString("profcodi");
                           codigoProf[i] = codigoProfesor;
                           Log.d("codigoprof:",codigoProfesor);
                           fechaIniAgenda = Object.getString("fechaIniAgenda");
                           stringIni = fechaIniAgenda.split(":");
                           String newDateIni = (stringIni[1]);
                           newDateIni = newDateIni.substring(1, newDateIni.length() - 3);
                           fechaIni [i] = newDateIni;

                           fechaFinAgenda = Object.getString("fechaFinAgenda");
                           stringFin = fechaFinAgenda.split(":");
                           String newDateFin = (stringFin[1]);
                           newDateFin = newDateFin.substring(1, newDateFin.length() - 3);
                           fechaFin[i] = newDateFin;

                           fechaEntera[i] = fechaIni[i] +" / "+ fechaFin[i];

                           if(!tituloAgenda.equals("")) {
                               //para q teodo sea minuscula excepto la primera letra
                               tituloAgenda = tituloAgenda.substring(0, 1).toUpperCase() + tituloAgenda.substring(1).toLowerCase();
                               detalleMateria = detalleMateria.substring(0, 1).toUpperCase() + detalleMateria.substring(1).toLowerCase();
                               titulos[i] = tituloAgenda;

                               int length = tituloAgenda.length();
                               if(length>35){
                                   tituloAgendaPequenio[i] = tituloAgenda.substring(0,35) + "...";
                               }
                               else{
                                   tituloAgendaPequenio[i] =tituloAgenda;
                               }
                           }
                           else{
                               titulos[i] = "NA";

                           }

                           detaMateria[i] = detalleMateria;


                           //imageUrl[i] = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/docentes/"  + codigoProf[i] +".jpg";

                           /*if (ImageStorage.checkifImageExists(codigoProf[i])) {
                               File file = ImageStorage.getImage("/" + codigoProf[i] + ".jpg");
                               String path = file.getAbsolutePath();
                               if (path != null) {
                                   b = BitmapFactory.decodeFile(path);
                                   //images[i] = b;
                               }
                           } else {
                               new GetImages(imageUrl[i], codigoProf[i], i).execute();
                           }*/
                       }

                    AdapterAgenda adapter = new AdapterAgenda(getActivity(),detaMateria, tituloAgendaPequenio, fechaEntera);//,imageProfesor);
                    list=(ListView)getActivity().findViewById(R.id.listViewAgenda);
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            View dialoglayout = inflater.inflate(R.layout.popup, null);
                            titAgenda = (TextView) dialoglayout.findViewById(R.id.titleAgenda);
                            detaAgenda = (TextView) dialoglayout.findViewById(R.id.detalleAgenda);
                            Profesor = (TextView) dialoglayout.findViewById(R.id.profesorAgenda);
                            titAgenda.setText(titulos[position]);
                            detaAgenda.setText(deta[position]);
                            Profesor.setText(profesor[position]);
                            if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                                fechaInicio = (TextView) dialoglayout.findViewById(R.id.fechaIni);
                                fechaFinal = (TextView) dialoglayout.findViewById(R.id.fechafin);
                                fechaInicio.setText(fechaIni[position]);
                                fechaFinal.setText(fechaFin[position]);
                            }
                            Dialog d = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT)
                                    .setTitle("Agenda")
                                    .setNegativeButton("Cancel", null)
                                    .setView(dialoglayout)
                                    .create();
                            d.show();
                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
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


    /*private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private int i_;


        private GetImages(String requestUrl, String _imagename_, int i ) {
            this.requestUrl = requestUrl;
            this.imagename_ = _imagename_;
            this.i_ = i;
        }

        InputStream bitmap=null;
        bitmap=getResources().getAssets().open("ProduitsMini/"+productList.get(rang).getImg_mini());

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap preview_bitmap = BitmapFactory.decodeStream(bitmap,null,options);

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                img_prof[i_] = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
                img_prof[i_] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
                Log.d("error en getimages:",ex.toString());
            }
            return img_prof[i_];
        }

        @Override
        protected void onPostExecute(Object o) {
            if (!ImageStorage.checkifImageExists(imagename_)) {
                ImageStorage.saveToSdCard(img_prof[i_], imagename_);
            }
        }
    }*/private class TransparentProgressDialog extends Dialog {

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
