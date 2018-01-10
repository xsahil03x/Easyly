//package com.magarex.easyly.Common;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//public class BroadcastDialogActivity extends AppCompatActivity {
//
//    private static final int WIFI_ENABLE_REQUEST = 100;
//    private AlertDialog mInternetDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Internet Disabled!");
//        builder.setMessage("No active Internet connection found.");
//        builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
//                startActivityForResult(gpsOptionsIntent, WIFI_ENABLE_REQUEST);
//            }
//        }).setNegativeButton("No, Just Exit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        mInternetDialog = builder.create();
//        mInternetDialog.show();
//
//    }
//}
