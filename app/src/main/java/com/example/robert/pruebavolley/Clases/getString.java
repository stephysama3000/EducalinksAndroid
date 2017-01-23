package com.example.robert.pruebavolley.Clases;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.robert.pruebavolley.Clases.AppController;

public class getString {

    String tag_string_req = "string_raq";
    String url = "http://desarrollo.educalinks.com.ec/mobile/colegios_clientes.php";
    String result="";

    public String get_String(final VolleyCallback callback) {
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result=response;
                callback.onSuccess(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return result;
    }


    public interface VolleyCallback{
        void onSuccess(String result);
    }

}


