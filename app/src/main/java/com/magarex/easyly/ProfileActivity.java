package com.magarex.easyly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ldoublem.loadingviewlib.view.LVBlock;
import com.magarex.easyly.Common.Common;
import com.magarex.easyly.Interface.EasylyApi;
import com.magarex.easyly.Models.User;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.magarex.easyly.Common.Common.FILE_NAME;

public class ProfileActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private EditText txtUsername, txtEmail, txtAddress, txtPhone;
    private Spinner spGender;
    private Button btnSave;
    private SpotsDialog progressDialog;
    private GoogleSignInButton btnGoogle;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        spGender = findViewById(R.id.spGender);
        btnSave = findViewById(R.id.btnSave);
        btnGoogle = findViewById(R.id.btnGoogle);
        mAuth = FirebaseAuth.getInstance();

        final SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        final String phone = sharedPreferences.getString(Common.KEY, "N/A");

        progressDialog = new SpotsDialog(this);
//        String check = getIntent().getExtras().getString("YES");

        //setting mobNo on txtPhone
        txtPhone.setText(phone.substring(3));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                signIn();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txtUsername.getText().toString().trim();
                String phone = txtPhone.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String gender = spGender.getSelectedItem().toString().trim();

                if (name.equals("") && address.equals("")) {
                    txtUsername.setError("Name is Mandatory");
                    txtAddress.setError("Address is Mandatory");
                }

                User user = new User(name, phone, address, email, gender);
                saveUser(user);

            }
        });
    }

    private void fetchData(FirebaseUser user) {
        txtUsername.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());
        txtPhone.setText(RegisterActivity.mobNumber);
    }

    private void saveUser(final User user) {

//        progressDialog.setTitle("Saving Information");
//        progressDialog.show();

        //init RetrofitClient
        Retrofit retrofit = Common.retrofit;

        final EasylyApi client = retrofit.create(EasylyApi.class);

        Call<String> call = client.saveUser(Common.requestTypes[0], user.getName(), user.getPhoneNumber(), user.getAddress(), user.getEmail(), user.getGender());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                String resp = response.body();
                if (resp.equals("Registration Successful")) {

                    loadUserData(client, user.getPhoneNumber());

                    Toast.makeText(ProfileActivity.this, resp, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();

                } else if (resp.equals("Already Registered")) {
                    loadUserData(client, user.getPhoneNumber());

                    Toast.makeText(ProfileActivity.this, resp, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();

                } else if (resp.equals("Registration Failed")) {
                    Toast.makeText(ProfileActivity.this, resp + "Try Again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Unable to connect with server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseAuthWithGoogle(account);
        } else {
            progressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Sign in Success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            fetchData(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), "Sign in Fails", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loadUserData(EasylyApi easylyApi, String phone) {
        Call<String> call = easylyApi.getUser(Common.requestTypes[3], phone);
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


