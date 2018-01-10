package com.magarex.easyly;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    private CountryCodePicker spCountryCode;
    private EditText txtPhone;
    private Button btnRegister;
    public static String mobNumber;
    public FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    public static String mVerificationId;
    private SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spCountryCode = findViewById(R.id.spCountryCode);
        txtPhone = findViewById(R.id.txtPhone);
        btnRegister = findViewById(R.id.btnRegister);
        progressDialog = new SpotsDialog(this);
//        progressDialog.setMessage("Loading");
//        progressDialog.setTitle("Wait");

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //Toast.makeText(getBaseContext(),"Verification Completeed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                //progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Verification Failed", Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                   // progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Invalid Phone Number", Toast.LENGTH_LONG).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Code has been send", Toast.LENGTH_LONG).show();
                mVerificationId = verificationId;
                Intent intent = new Intent(getBaseContext(), OtpVerificationActivity.class);
                startActivity(intent);
                finish();
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterMobile();
            }
        });

    }

    private void RegisterMobile() {

        spCountryCode.registerCarrierNumberEditText(txtPhone);
        mobNumber = "+" + spCountryCode.getFullNumber();

        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobNumber, 60, TimeUnit.SECONDS, this, mCallbacks);

    }
}
