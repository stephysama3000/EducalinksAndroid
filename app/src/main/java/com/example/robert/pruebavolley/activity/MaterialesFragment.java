package com.example.robert.pruebavolley.activity;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.example.robert.pruebavolley.Adapter.AdapterClases;
import com.example.robert.pruebavolley.Adapter.AdapterMateriales;
import com.example.robert.pruebavolley.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MaterialesFragment extends Fragment {
    private android.support.v7.widget.Toolbar toolbar;
    SharedPreferences settings;
    TextView noMateriales, materiaE, profesorE;
    ImageView imagenMat;
    final Map<String,String> paramsMateriales = new HashMap<String,String>();
    String URL_alumnos= "http://demo.educalinks.com.ec/mobile/main.php";
    String colegio,periodo;
    private String TAG = "TAG";
    private JSONArray JsonAll;
    JSONObject jsonObject = null;
    String fechaClases , nomCol , url;
    String[] mater_codiA,mater_tituA,mater_detaA,mater_fechaA,mater_fileA, stringIni;
    int i = 0 ;
    ListView list;
    int variable = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_materiales, container, false);


        settings = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        noMateriales = (TextView) rootView.findViewById(R.id.noMateriales);
        materiaE = (TextView) rootView.findViewById(R.id.materiaEsc);
        profesorE = (TextView) rootView.findViewById(R.id.profesorNomb );
        imagenMat = (ImageView) rootView.findViewById(R.id.imagenEsc);





        colegio = settings .getString("colegio", "null");
        periodo = settings.getString("periodo","null");
        url = settings.getString("imagenEsc","null");
        String cursoCodiAlum = settings .getString("cursoCodiAlum", "null");
        String cursParaMateProfCodi = settings.getString("cursParaMateProfCodiEsc","null");
        String profesor = settings.getString("profesorEsc","null");
        String materia = settings.getString("materiaEsc","null");


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP


        Drawable myIcon = getActivity().getResources().getDrawable( R.drawable.defaultclase);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(myIcon)
                .showImageOnFail(myIcon)
                .showImageOnLoading(myIcon).build();

        imageLoader.displayImage(url, imagenMat, options);




        materiaE.setText(materia);
        profesorE.setText(profesor);

        paramsMateriales.put("colegio", colegio);
        paramsMateriales.put("cursParaMateProfCodi", cursParaMateProfCodi );
        paramsMateriales.put("opcion", "materiales");

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

        ShowMateriales(URL_alumnos, paramsMateriales);


        SharedPreferences.Editor editor1 = settings.edit();
        editor1.putInt("variables",variable);
        editor1.commit();

        return rootView;
    }


    public void ShowMateriales(String url, final Map<String,String> json)  {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Log.d(TAG, "Login Response: " + response.toString());
                    jsonObject = new JSONObject(response.toString());
                    JsonAll = jsonObject.getJSONArray("result");

                    if(JsonAll.length()==0){

                        noMateriales.setVisibility(View.VISIBLE);
                    }


                    final ArrayList<String> Keys = new ArrayList<String>();
                    for(int t = 0; t < JsonAll.length(); t ++){
                        Keys.add(String.valueOf(t));
                    }

                    mater_codiA = new String[Keys.size()];
                    mater_detaA = new String[Keys.size()];
                    mater_fechaA = new String[Keys.size()];
                    mater_fileA = new String[Keys.size()];
                    mater_tituA = new String[Keys.size()];

                    for (i = 0; i < JsonAll.length(); i++) {
                        final int Index = i;
                        final JSONObject Object = JsonAll.getJSONObject(i);

                        mater_codiA[i] = Object.getString("mater_codi");
                        mater_detaA[i] = Object.getString("mater_deta");
                        mater_fechaA[i]  = Object.getString("mater_fech_regi");
                        mater_fileA[i] = Object.getString("mater_file");
                        mater_tituA[i] = Object.getString("mater_titu");

                        fechaClases = Object.getString("mater_fech_regi");
                        stringIni = fechaClases.split(":");
                        String newDateIni = (stringIni[1]);
                        newDateIni = newDateIni.substring(1, newDateIni.length() - 3);
                        mater_fechaA[i] = newDateIni;

                    }

                    AdapterMateriales adapter = new AdapterMateriales(getActivity(), mater_tituA, mater_detaA, mater_fechaA,nomCol,periodo,mater_fileA);
                    list=(ListView)getActivity().findViewById(R.id.listViewMateriales);
                    list.setAdapter(adapter);

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ErrorMateriales", error.toString());
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


}
