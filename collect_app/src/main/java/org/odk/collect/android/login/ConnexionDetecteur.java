package org.odk.collect.android.login;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by gener on 03/12/2016.
 */

public class ConnexionDetecteur {

    Context context;

    public ConnexionDetecteur(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivity = (ConnectivityManager)
                context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivity !=null){
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info !=null){
                if(info.getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }
        return false;
    }

}
