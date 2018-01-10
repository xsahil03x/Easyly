//package com.magarex.easyly;
//
//import android.app.AlertDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.*;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.magarex.easyly.Common.BroadcastDialogActivity;
//
//public class InternetReceiver extends BroadcastReceiver {
//
//    private static final String LOG_TAG = "NetworkChangeReceiver";
//    private boolean isConnected = false;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        isNetworkAvailable(context);
//    }
//
//    private boolean isNetworkAvailable(Context context) {
//        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivity != null) {
//            NetworkInfo info = connectivity.getActiveNetworkInfo();
//            if (info != null) {
//                if (info.getState() == NetworkInfo.State.CONNECTED) {
//                    if (!isConnected) {
//                        Log.v(LOG_TAG, "Now you are connected to Internet!");
//                        //Toast.makeText(context, "hurray", Toast.LENGTH_LONG).show();
//                        isConnected = true;
//                    }
//                    return true;
//                }
//            }
//        }
//
//        Log.v(LOG_TAG, "You are not connected to Internet!");
//        Toast.makeText(context, "!hurray", Toast.LENGTH_LONG).show();
//        isConnected = false;
//        Intent intent = new Intent(context, BroadcastDialogActivity.class);
//        intent.putExtra("isConnected", isConnected);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        return false;
//    }
//}
