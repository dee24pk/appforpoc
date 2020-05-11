package com.example.demo;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class TokenService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh(){
        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Divice Token", DeviceToken);
    }

}
