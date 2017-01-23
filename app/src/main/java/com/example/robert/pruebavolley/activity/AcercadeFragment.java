package com.example.robert.pruebavolley.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.robert.pruebavolley.R;


public class AcercadeFragment extends Fragment {

Button link,correo;

    public AcercadeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview= inflater.inflate(R.layout.fragment_acercade2, container, false);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        Toast.makeText(getActivity(), Integer.toString(widthPixels), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), Integer.toString(heightPixels), Toast.LENGTH_SHORT).show();*/
        link = (Button)rootview.findViewById(R.id.buttonLink);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.educalinks.com.ec"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        correo = (Button)rootview.findViewById(R.id.buttonCorreo);
        correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@redlinks.com.ec" });
                startActivity(Intent.createChooser(intent, ""));
            }
        });



        return rootview;
    }

}
