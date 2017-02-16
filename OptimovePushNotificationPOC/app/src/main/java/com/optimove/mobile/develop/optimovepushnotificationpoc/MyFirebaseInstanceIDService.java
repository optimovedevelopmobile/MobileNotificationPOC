package com.optimove.mobile.develop.optimovepushnotificationpoc;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * Created by yossi_c on 16/1/2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "Opti-FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        String msg = getString(R.string.msg_token_fmt, refreshedToken);
        Log.d(TAG, "Refreshed token: " + msg);


        // Retrieve my other app.
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        FirebaseInstanceId secondery  = FirebaseInstanceId.getInstance(app);
        String senderId = "1055457349234";
        String refreshedTokenSecondery = null;
        try {
            refreshedTokenSecondery = secondery.getToken(senderId, "FCM");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //(1055457349234, "FCM");
        // Log and toast
        String msgSecondery = getString(R.string.msg_token_fmt, refreshedTokenSecondery);
        Log.d(TAG, "Refreshed Secondery token: " + msgSecondery);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
