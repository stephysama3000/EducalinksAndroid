package com.example.robert.pruebavolley.activity;

import com.orm.SugarContext;

/**
 * Created by redlinks on 17/1/17.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
