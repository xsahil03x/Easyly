package com.magarex.easyly;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.magarex.easyly.Common.Common;
import com.magarex.easyly.Interface.EasylyApi;

import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.magarex.easyly.Common.Common.FILE_NAME;

public class SplashScreen extends AppCompatActivity {

    //private String versionName = "";
    private String versionCode;
    private AlertDialog alertDialog;
    public static String name, email, address, gender;
    public static long id;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        final String phoneNo = sharedPreferences.getString(Common.KEY, "N/A");

        progressDialog = new SpotsDialog(this);

//        progressDialog.setMessage("Loading");
//        progressDialog.setTitle("Wait");
//        progressDialog.show();

        getVersion();

        Retrofit retrofit = Common.retrofit;

        final EasylyApi client = retrofit.create(EasylyApi.class);

        Call<String> call = client.getStatus(versionCode);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                String resp = response.body();

                if (resp.equals("Access Granted")) {
                    if (!phoneNo.equals("N/A")) {
                        loadUserData(client, phoneNo.substring(3));
                    } else {
                        splashThreadtoRegister();
                    }

                } else if (resp.equals("New Version Available")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Update Availabe");
                    builder.setMessage("Click OK To Update");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            //Copy App URL from Google Play Store.
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.youtube"));
                            startActivity(intent);
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            finish();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                builder.setTitle("Oops");
                builder.setMessage("Unable to connect with the Server");
                builder.setCancelable(false);
                builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SplashScreen.this.recreate();
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void getVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = String.valueOf(packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void splashThreadtoRegister() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private void splashThreadtoMain() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private void loadUserData(EasylyApi easylyApi, String phone) {
        Call<String> call = easylyApi.getUser(Common.requestTypes[3], phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resp = response.body();
                if (!resp.equals("")) {
                    String[] data = resp.split("#");
                    id = Long.parseLong(data[0]);
                    name = data[1];
                    address = data[2];
                    email = data[5];
                    gender = data[4];

                    splashThreadtoMain();
                } else if (resp.equals("")) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashScreen.this, ProfileActivity.class);
                           // intent.putExtra("YES","YES");
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);
                } else {
                    Toast.makeText(getBaseContext(), "Something went wrong try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getBaseContext(), "cannot connect to the server try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
