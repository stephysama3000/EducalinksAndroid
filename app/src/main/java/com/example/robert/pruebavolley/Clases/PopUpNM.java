package com.example.robert.pruebavolley.Clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

/**
 * Created by redlinks on 26/2/16.
 */
public class PopUpNM {

    TextView nombrePadre;
    Spinner profesores;

    public Dialog PopUpNewMessage(Activity activity, View dialoglayout, String padre) {
        nombrePadre = (TextView) dialoglayout.findViewById(R.id.nombrePadre);
        nombrePadre.setText(padre);
        Dialog d = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Nuevo Mensaje")
                .setNegativeButton("Cancel", null)
                .setView(dialoglayout)
                .create();
        d.show();
        return d;
    }

}
/*list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                    });*/
