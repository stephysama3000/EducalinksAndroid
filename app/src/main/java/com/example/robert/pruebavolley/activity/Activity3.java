package com.example.robert.pruebavolley.activity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.robert.pruebavolley.Clases.AppController;
import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.Services.AlarmReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Activity3 extends AppCompatActivity {

    //Defining Variables
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    //alumnos
    String URL_alumnos = "http://demo.educalinks.com.ec/mobile/main.php";
    final Map<String, String> params = new HashMap<String, String>();
    final Map<String, String> paramsUpdate = new HashMap<String, String>();
    String codigo, nombre, apellido, key;
    private JSONArray JsonAll;
    HashMap<String, String> alumnos = new HashMap<>();
    HashMap<String, String> alumnosCopy = new HashMap<>();
    ArrayAdapter<String> adapterAlumnos;
    Spinner AlumnoSpinner;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private AlarmManager alarmMgr;
    private PendingIntent pendingIntent;
    TextView nombrePadre;
    Fragment fragment = null;
    ImageView image;
    String nomCol = "";
    String imageUrl;
    String nombreRepre, apellidoRepre;
    Bitmap bmImg;
    HttpURLConnection urlConnection = null;
    private int count = 0;
    Boolean imagen = false;
    Fragment fragmentNew;
    Boolean in = false;
    Bitmap b;
    ActionBarDrawerToggle actionBarDrawerToggle;
    String notificacion;
    ProgressDialog pDialog;
    URLConnection conn;
    private Menu menu;
    String cursoCodiAlum, cursoCodi,cursoAlum ,paralAlum, distipo,periodo,repr_app;
    String[] periodoS, cursoCodiAlumS, cursoCodiS, distipoS;
    String reprcodi, colegio ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        Log.d("Ejecutando", "Activity3");
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        if (getIntent().hasExtra("fragment")) {

            notificacion = pref.getString("notificacion", "null");

                if(notificacion.equals("agenda")){
                    fragment = new AgendaFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                    in = true;
                }else if(notificacion.equals("mensajes")){
                    fragment = new MensajesFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                    editor.putString("opcionFragment","2");
                    in = true;
                }else if(notificacion.equals("actualizacion")){
                    fragment = new HomeFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                    in = true;
                }

        }

            AlumnoSpinner = (Spinner) findViewById(R.id.spinner);
            image = (ImageView) findViewById(R.id.profile_image);

            pref = PreferenceManager.getDefaultSharedPreferences(this);
            String nombreRep = pref.getString("nombreRep", "null");
            String apelRep = pref.getString("apelRep", "null");
            colegio = pref.getString("colegio", "null");
            String usuario = pref.getString("usuario", "null");
            String password = pref.getString("password", "null");
            reprcodi = pref.getString("codigo", "null");
            String pericodi = pref.getString("periodo", "null");
            String alumnocodi = pref.getString("alumnocodi", "null");




            periodo = pericodi;
            params.put("colegio", colegio);
            params.put("username", usuario);
            params.put("password", password);
            params.put("tipo_usua", "2");
            params.put("reprcodi", reprcodi);
            params.put("pericodi", pericodi);
            params.put("opcion", "listar_alumnos");

            ShowAlumnosSpinner(URL_alumnos, params);
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
                    Toast.makeText(getApplicationContext(), "Su institución no se encuentra en el directorio", Toast.LENGTH_SHORT).show();
            }
            List<String> listApe = new ArrayList<String>();
            List<String> listNom = new ArrayList<String>();
            String[] nombres = nombreRep.split("\\s+");
            String[] apellidos = apelRep.split("\\s+");
            String[] apl = new String[2];

            for (int j = 0; j < nombres.length; j++) {
                listNom.add(nombres[j]);
            }
            for (int j = 0; j < apellidos.length; j++) {
                listApe.add(apellidos[j]);
            }

        if(!listNom.get(0).equals("")){
                String s1 = listNom.get(0).substring(0, 1).toUpperCase();
                nombreRepre = s1 + listNom.get(0).substring(1).toLowerCase();}
        else{
            nombreRepre = "";
        }
        if(!listApe.get(0).equals("")) {
            String s2 = listApe.get(0).substring(0, 1).toUpperCase();
            apellidoRepre = s2 + listApe.get(0).substring(1).toLowerCase();
        }
        else{
            apellidoRepre = "";
        }


            AlumnoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int i;
                    Set set = alumnosCopy.entrySet();
                    Iterator iter = set.iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        for (i = 0; i < alumnosCopy.size(); i++) {
                            if (parent.getItemAtPosition(position).equals(entry.getValue().toString())) {
                                key = entry.getKey().toString();
                                editor = pref.edit();
                                editor.putString("alumnocodi", key);
                                editor.putString("periodoAlum", periodoS[position]);
                                editor.putString("cursoCodiAlum", cursoCodiAlumS[position]);
                                editor.putString("cursoCodi", cursoCodiS[position]);
                                editor.putString("distipo", distipoS[position]);

                                editor.commit();
                                if (!nomCol.equals("")) {
                                    imageUrl = "http://demo.educalinks.com.ec/fotos/" + nomCol + "/alumnos/" + periodo + "/" + key + ".jpg";
                                    Log.d("Ejecutando", "ya tengo le URL");
                                    editor = pref.edit();

                                    editor.putString("urlfotos", imageUrl + ".jpg");
                                    editor.commit();
                                    if (in == false) {
                                        Home();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "Su institución no se encuentra en el directorio", Toast.LENGTH_SHORT).show();
                                }
                                new GetImages(imageUrl, image, key).execute();
                                break;
                            }
                        }
                    }
                    in = false;
                    drawerLayout.closeDrawers();

                }



                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
            });

            AlumnoSpinner.getBackground().setColorFilter(getResources().getColor(R.color.list_item_title), PorterDuff.Mode.SRC_ATOP);

            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("");


            navigationView = (NavigationView) findViewById(R.id.navigation_view);
            if(colegio.equals("26") || colegio.equals("27")){
                navigationView.getMenu().findItem(R.id.factura).setVisible(false);
            }else
            {
                navigationView.getMenu().findItem(R.id.factura).setVisible(true);
            }

             int[][] states = new int[][] {
                    new int[] { android.R.attr.state_enabled}, // enabled
                    new int[] {-android.R.attr.state_enabled}, // disabled
                    new int[] {-android.R.attr.state_checked}, // unchecked
                    new int[] { android.R.attr.state_pressed}  // pressed
            };

            int[] colors = new int[] {
                    Color.WHITE,
                    Color.RED,
                    Color.GREEN,
                    Color.BLUE
            };

            ColorStateList myList = new ColorStateList(states, colors);

        navigationView.setItemTextColor(myList);




            navigationView.setItemIconTintList(null);
            //navigationView.setBackgroundColor(getResources().getColor(R.color.azulmenu));
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    if (menuItem.isChecked()) menuItem.setChecked(false);
                    else menuItem.setChecked(true);

                    drawerLayout.closeDrawers();


                    switch (menuItem.getItemId()) {

                        case R.id.inicio:
                            Home();
                            return true;
                        case R.id.agenda:
                            fragment = new AgendaFragment();
                            FragmentManager fragmentManager2 = getFragmentManager();
                            fragmentManager2.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagAgenda").commit();
                            return true;
                        case R.id.materiales:
                            fragment = new ClasesFragment();
                            FragmentManager fragmentManager3 = getFragmentManager();
                            fragmentManager3.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagMater").commit();
                            return true;
                        case R.id.mensajes:
                            fragment = new MensajesFragment();
                            editor.putString("opcionfragment", "0");
                            editor.commit();
                            //fragment = new MensajesEntFragment();
                            FragmentManager fragmentManager4 = getFragmentManager();
                            fragmentManager4.beginTransaction().replace(R.id.frame, fragment).addToBackStack("Mensajes").commit();
                            return true;
                        case R.id.calificaciones:
                            fragment = new notasFragment();
                            FragmentManager fragmentManager5 = getFragmentManager();
                            fragmentManager5.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagCalific").commit();
                            return true;
                        case R.id.factura:
                            fragment = new FacturaFragment();
                            FragmentManager fragmentManager6 = getFragmentManager();
                            fragmentManager6.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagFact").commit();
                            return true;
                        case R.id.notificaciones:
                            fragment = new NotificacionesFragment();
                            FragmentManager fragmentManager7 = getFragmentManager();
                            fragmentManager7.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagNoti").commit();
                            return true;
                        case R.id.acerca:
                            fragment = new AcercadeFragment();
                            FragmentManager fragmentManager8 = getFragmentManager();
                            fragmentManager8.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagAcerca").commit();
                            return true;
                        case R.id.cerrar:
                            logoutUser();
                            Toast.makeText(getApplicationContext(), "Hasta Luego", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            break;


                    }
                    return true;
                }

            });

            if (fragment == null) {
                Home();
            }


            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);



            //drawerLayout.setBackgroundColor(getResources().getColor(R.color.azulmenu));
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    actionBarDrawerToggle.syncState();
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    actionBarDrawerToggle.syncState();
                }
            };
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            actionBarDrawerToggle.syncState();

    }

    public HashMap<String, String> ShowAlumnos(HashMap<String, String> alumnos) {
        Map<String, String> map = new TreeMap<String, String>(alumnos);
        Collection<String> vals = map.values();
        String[] array = vals.toArray(new String[vals.size()]);
        adapterAlumnos = new ArrayAdapter<String>(Activity3.this, R.layout.spinner_item_alumno, array);
        adapterAlumnos.setDropDownViewResource(R.layout.spinner_drop_down_agenda);
        AlumnoSpinner.setAdapter(adapterAlumnos);
        return alumnos;
    }


    public void ShowAlumnosSpinner(String url, final Map<String, String> json) {
        final RequestQueue queue = Volley.newRequestQueue(Activity3.this);
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.d("TAG", "Login Response: " + response.toString());
                    JSONObject jresponse = new JSONObject(response);
                    JsonAll = jresponse.getJSONArray("result");
                    periodoS = new String[JsonAll.length()];
                    cursoCodiAlumS = new String[JsonAll.length()];
                    cursoCodiS = new String[JsonAll.length()];
                    distipoS = new String[JsonAll.length()];
                    for (int i = 0; i < JsonAll.length(); i++) {
                        JSONObject Object = JsonAll.getJSONObject(i);
                        codigo = Object.getString("codigo");
                        nombre = Object.getString("nombre");
                        apellido = Object.getString("apellido");
                        //periodo = jresponse.getString("periodo");
                        cursoCodiAlum = Object.getString("alumCursCodi");
                        cursoCodi = Object.getString("cursCodi");
                        cursoAlum = Object.getString("curso");
                        paralAlum = Object.getString("paralelo");
                        distipo = Object.getString("distipo");
                        repr_app = Object.getString("repr_app");

                        if(repr_app.equals("0")){
                            paramsUpdate.put("opcion", "update_app_repr");
                            paramsUpdate.put("reprcodi", reprcodi);
                            paramsUpdate.put("colegio",colegio);
                            updateApp(URL_alumnos, paramsUpdate);
                        }

                        String[] nombres = nombre.split("\\s+");
                        String[] apellidos = apellido.split("\\s+");
                        String[] nmb = new String[2];
                        String[] apl = new String[apellidos.length];

                        String s1 = nombres[0].substring(0, 1).toUpperCase();
                        String nombreAlumno = s1 + nombres[0].substring(1).toLowerCase();

                        for (int j = 0; j < apellidos.length; j++) {

                            String s2 = apellidos[j].substring(0, 1).toUpperCase();
                            apl[j] = s2 + apellidos[j].substring(1).toLowerCase();
                        }

                        periodoS[i] = periodo;
                        cursoCodiAlumS[i] = cursoCodiAlum;
                        cursoCodiS[i] = cursoCodi;
                        distipoS[i] = distipo;

                        alumnos.put(codigo, nombreAlumno + " " + apl[0] + " " + apl[1]);
                    }
                    ShowAlumnos(alumnos);
                    Log.d("Ejecutando", "show alumnos");

                    alumnosCopy = ShowAlumnos(alumnos);


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

    public void logoutUser() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        editor.clear();
        editor.commit();
        alarmMgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarm = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarm, 0);
        alarmMgr.cancel(pendingIntent);
        Intent intent = new Intent(Activity3.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    class downloadImage extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... fileUrl) {
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(fileUrl[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("error en file:", e.toString());
            }
            try {
                urlConnection = (HttpURLConnection) myFileUrl.openConnection();
                InputStream in = urlConnection.getInputStream();
                bmImg = BitmapFactory.decodeStream(in);
                return bmImg;
            } catch (IOException e) {
                bmImg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
                return bmImg;
            }

        }

        protected void onPostExecute(Bitmap bit) {
            int START_X = 0;
            int START_Y = 200;
            int WIDTH_PX = 100;
            int HEIGHT_PX = 200;
            image = (ImageView) findViewById(R.id.profile_image);
            image.setImageBitmap(bit);
        }
    }

    public void Home() {
        fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("tagHome").commit();
    }

    //esto va en mensajes


    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void doIncrease() {
        count++;
        invalidateOptionsMenu();
    }

    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap;
        private FileOutputStream fos;

        private GetImages(String requestUrl, ImageView view, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                conn = url.openConnection();
                InputStream is = conn.getInputStream();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeStream(is, null, options);
                options.inSampleSize = calculateInSampleSize(options, 100, 100);
                options.inJustDecodeBounds = false;
                is.close();
                is = getHTTPConnectionInputStream(requestUrl);
                bitmap = BitmapFactory.decodeStream(is, null,options);
                is.close();
                return bitmap;
            } catch (Exception ex) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
                Log.d("error en getimages:",ex.toString());
                return bitmap;
            }
        }

        /*@Override
        protected void onPostExecute(Object o) {
            if (!ImageStorage.checkifImageExists(imagename_)) {
                view.setImageBitmap(bitmap);
                ImageStorage.saveToSdCard(bitmap, imagename_);
            }
        }*/

        @Override
        protected void onPostExecute(Object o) {
                view.setImageBitmap(bitmap);
            Log.d("postexecute","entro");
        }

    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromStream(URLConnection conn, int reqWidth, int reqHeight) throws IOException {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(conn.getInputStream(), null, options);
        //BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(conn.getInputStream(), null, options);
    }

    public InputStream getHTTPConnectionInputStream(String url1) {
        URL url;
        InputStream is = null;
        try {
            url = new URL(url1);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            is = connection.getInputStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;

    }
    //mImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));


    public void updateApp(String url, final Map<String,String> json){
        StringRequest post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    Log.d("TAG", "app Updated ");

                }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("otro error:", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", error.toString());
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

}


