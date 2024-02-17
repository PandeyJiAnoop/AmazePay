package com.akp.amazepay.Util;
/**
 * Created by Anoop Pandey on 9696381023.
 */
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton singleton;
    private RequestQueue queue;
    private static Context context;

    public VolleySingleton(Context context){
        this.context = context;
        queue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context){
        if(singleton==null){
            singleton = new VolleySingleton(context);
        }
        return singleton;
    }

    private RequestQueue getRequestQueue() {

        if (queue==null){
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    public void addRequest(Request request){
        getRequestQueue().add(request);
    }


}
