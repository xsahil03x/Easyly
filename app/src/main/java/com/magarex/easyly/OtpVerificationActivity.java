package com.magarex.easyly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.magarex.easyly.Common.Common;
import com.magarex.easyly.Interface.EasylyApi;

import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpVerificationActivity extends AppCompatActivity {

    private Button btnVerify;
    private EditText txtCode;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        txtCode = findViewById(R.id.txtCode);
        btnVerify = findViewById(R.id.btnVerify);
        progressDialog = new SpotsDialog(this);
//        progressDialog.setMessage("Loading");
//        progressDialog.setTitle("Wait");

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(Common.FILE_NAME, getApplicationContext().MODE_PRIVATE);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressDialog.show();
                VerifyMobile();
            }
        });

    }

    private void VerifyMobile() {

        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(RegisterActivity.mVerificationId, txtCode.getText().toString().trim());

        signInWithPhoneAuthCredentials(credentials);

    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Verification Done", Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Common.KEY, RegisterActivity.mobNumber);
                            editor.apply();

                            Retrofit retrofit = Common.retrofit;
                            EasylyApi client = retrofit.create(EasylyApi.class);
                            loadUserData(client,RegisterActivity.mobNumber.substring(3));

                            Intent intent = new Intent(OtpVerificationActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            //progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Invalid Verification Try Again", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OtpVerificationActivity.this,RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void loadUserData(EasylyApi easylyApi, String phone) {
        Call<String> call = easylyApi.getUser(Common.requestTypes[2], phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resp = response.body();
                if (!resp.equals("")) {
                    String[] data = resp.split("#");
                    SplashScreen.id = Long.parseLong(data[0]);
                    SplashScreen.name = data[1];
                    SplashScreen.address = data[2];
                    SplashScreen.email = data[5];
                    SplashScreen.gender = data[4];

                    Toast.makeText(getBaseContext(),"Welcome Back Again",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(OtpVerificationActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (resp.equals("")) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(OtpVerificationActivity.this, ProfileActivity.class);
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
