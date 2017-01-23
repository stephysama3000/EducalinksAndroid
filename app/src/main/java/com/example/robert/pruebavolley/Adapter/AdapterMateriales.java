package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AdapterMateriales extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] titulo;
    private final String[] detalle;
    private final String[] fecha;
    private final String[] file;
    private final String colegio;
    private final String periodo;
    //private final Bitmap[] imageProfesor;
    SharedPreferences settings;
    Bitmap b;
    ImageButton descargar;
    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    public AdapterMateriales(Activity context, String[] titulo, String[] detalle, String[] fecha, String colegio, String periodo,String[] file) {
        super(context, R.layout.listamateriales, titulo);
        this.context = context;
        this.titulo = titulo;
        this.detalle = detalle;
        this.fecha = fecha;
        this.colegio = colegio;
        this.periodo = periodo;
        this.file = file;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.listamateriales, null, true);
        TextView txtTitulo = (TextView) rowView.findViewById(R.id.tituloMateriales);
        TextView txtMateriales = (TextView) rowView.findViewById(R.id.detalleMateriales);
        TextView txtFecha = (TextView) rowView.findViewById(R.id.fechaMateriales);
        descargar = (ImageButton) rowView.findViewById(R.id.imgdown);

        txtTitulo.setText(titulo[position].toUpperCase());
        txtMateriales.setText(detalle[position]);
        txtFecha.setText(fecha[position]);

        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
        } else {
            rowView.setBackgroundColor(Color.parseColor("#e6e7e8"));
            //rowView.setAlpha(0.2f);
        }

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("url","http://" + colegio + ".educalinks.com.ec/files/" + colegio + "/" + periodo + "/" + file[position]);
                String URL = "http://" + colegio + ".educalinks.com.ec/files/" + colegio + "/" + periodo + "/" + file[position];
                new DownloadFileFromURL().execute(URL, file[position]);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));

            }
        });

        return rowView;
    }


    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(getContext());
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);
                // Output stream
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString()
                        + "/" + f_url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

        }

    }


}