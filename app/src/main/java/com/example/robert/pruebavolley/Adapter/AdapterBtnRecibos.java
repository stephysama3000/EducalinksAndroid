
package com.example.robert.pruebavolley.Adapter;

        import android.app.Activity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.net.Uri;
        import android.preference.PreferenceManager;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;

        import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 6/6/16.
 */
public class AdapterBtnRecibos extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] numeroRecibo;
    private final String nomCol;

    SharedPreferences settings;
    Button descargarRecibo;

    public AdapterBtnRecibos(Activity context, String[] numeroRecibo, String nomCol) {
        super(context, R.layout.lista_btn_recibos, numeroRecibo);
        this.context = context;
        this.numeroRecibo = numeroRecibo;
        this.nomCol = nomCol;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        View rowView = inflater.inflate(R.layout.lista_btn_recibos, null, true);
        descargarRecibo = (Button) rowView.findViewById(R.id.btnRecibos);

        descargarRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("url","https://" + nomCol + ".educalinks.com.ec/admin/recibo_pagos/demo/r_pago_app.php?id=" + numeroRecibo[position] );
                String URL = "https://" + nomCol + ".educalinks.com.ec/admin/recibo_pagos/demo/r_pago_app.php?id=" + numeroRecibo[position];
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
            }
        });

        return rowView;
    }


}
