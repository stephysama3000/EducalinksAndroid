package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.robert.pruebavolley.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by redlinks on 28/11/16.
 */
public class AdapterPagosRealizados extends ArrayAdapter {
    Context context;
    private final String[] totalPago;
    private final String[] formaPago;
    private final String[] codigoPago;
    Button VerPagos;
    TextView totalPagotxt,formaPagotxt;
    SharedPreferences settings;
    String colegio;
    Button pdf;
    String nomCol;

    public AdapterPagosRealizados(Context context, String[] totalPago, String[] formaPago, String[] codigoPago)
    {
        super(context, R.layout.lista_pagos_realizados,totalPago);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.totalPago = totalPago;
        this.formaPago = formaPago;
        this.codigoPago = codigoPago;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.lista_pagos_realizados, parent, false);
         totalPagotxt = (TextView) convertView.findViewById(R.id.totalPago);
         formaPagotxt = (TextView) convertView.findViewById(R.id.formaPago);
         pdf = (Button) convertView.findViewById(R.id.VerPagos);

        settings = PreferenceManager.getDefaultSharedPreferences((Activity)context);
        colegio = settings .getString("colegio", "null");

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
                Log.d("none", "Su institución no se encuentra en el directorio");
        }

        totalPagotxt.setText(totalPago[position]);
        formaPagotxt.setText(formaPago[position]);

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#e6e7e8"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#eeeeee"));
            //rowView.setAlpha(0.2f);
        }


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            //https://moderna.educalinks.com.ec/finan/PDF/imprimir/pago/1492
            public void onClick(View v) {
                Log.d("url","https://" + nomCol + ".educalinks.com.ec/admin/recibo_pagos/app/r_pago_app.php?id=" + codigoPago[position] );
                String URL = "https://" + nomCol + ".educalinks.com.ec/admin/recibo_pagos/app/r_pago_app.php?id=" + codigoPago[position];
                new DownloadFileFromURL().execute(URL, codigoPago[position]);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
            }
        });
        return convertView;
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
