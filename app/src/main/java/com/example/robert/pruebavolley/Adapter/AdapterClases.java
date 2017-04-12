package com.example.robert.pruebavolley.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.Clases.ImageStorage;
import com.example.robert.pruebavolley.R;
import com.example.robert.pruebavolley.activity.ClasesFragment;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class AdapterClases extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] materia;
    private final String[] numMateriales;
    private final String[] imagenes;
    //private final Bitmap[] imageProfesor;
    SharedPreferences settings;
    Bitmap b;

    public AdapterClases(Activity context, String[] materia, String[] numMateriales, String[] imagenes) {
        super(context, R.layout.listagenda, materia);
        this.context = context;
        this.materia = materia;
        this.numMateriales = numMateriales;
        this.imagenes = imagenes;
        //this.imageProfesor = imageProfesor;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());



        View rowView = inflater.inflate(R.layout.listaclases, null, true);
        TextView txtMateria = (TextView) rowView.findViewById(R.id.materia);
        TextView txtNumMater = (TextView) rowView.findViewById(R.id.materialesCount);
        ImageView icono = (ImageView) rowView.findViewById(R.id.imgMateria);

        txtMateria.setText(materia[position].toUpperCase());
        txtNumMater.setText(numMateriales[position]);

        //imageLoader
        // UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        Drawable myIcon = getContext().getResources().getDrawable( R.drawable.defaultclase);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getContext()));

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(myIcon)
                .showImageOnFail(myIcon)
                .showImageOnLoading(myIcon).build();

//download and display image from url
        imageLoader.displayImage(imagenes[position], icono, options);


        return rowView;
    }
}