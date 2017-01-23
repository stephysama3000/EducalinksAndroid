package com.example.robert.pruebavolley.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robert.pruebavolley.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robert on 28/10/2015.
 */
public class AdapterColegios extends BaseAdapter{
    private final ArrayList mData;

    public AdapterColegios(Map<String,String> map){
        mData=new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap.Entry<String,String> getItem(int position) {
        return (Map.Entry)mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_adapter_item, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, String> item = getItem(position);

        ImageView pictures = (ImageView)result.findViewById(R.id.imageView1);
        pictures.setBackgroundResource(Integer.parseInt(item.getValue()));


        ((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue());

        return result;
    }


}
