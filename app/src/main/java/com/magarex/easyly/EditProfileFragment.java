package com.magarex.easyly;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.magarex.easyly.Common.Common;
import com.magarex.easyly.Interface.EasylyApi;
import com.magarex.easyly.Models.User;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.magarex.easyly.Common.Common.FILE_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private View view;
    private EditText txtUsername, txtEmail, txtAddress, txtPhone;
    private Button btnSave;
    private RadioGroup rgGender;
    private SpotsDialog progressDialog;
    private RadioButton rbMale,rbFemale;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        getActivity();
        final SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtPhone = view.findViewById(R.id.txtPhone);
        btnSave = view.findViewById(R.id.btnSave);
        rgGender = view.findViewById(R.id.rgGender);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);
        progressDialog = new SpotsDialog(getActivity());
//        progressDialog.setMessage("Loading");
//        progressDialog.setTitle("Wait");
//        progressDialog.show();

        final String phone = sharedPreferences.getString(Common.KEY, "N/A");
        txtPhone.setText(phone.substring(3));

        //setting Previous Data
        txtUsername.setText(SplashScreen.name);
        txtEmail.setText(SplashScreen.email);
        txtAddress.setText(SplashScreen.address);

        //initRetrofitClient
        Retrofit retrofit = Common.retrofit;
        final EasylyApi client = retrofit.create(EasylyApi.class);

        disablePhoneField();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txtUsername.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String address = txtAddress.getText().toString().trim();

                int id = rgGender.getCheckedRadioButtonId();
                // If nothing is selected from Radio Group, then it return -1
                String gender = null;
                if (id != -1) {
                    RadioButton selectedRadioButton = getActivity().findViewById(id);
                    gender = selectedRadioButton.getText().toString();
                }

                User user = new User(name, phone, address, email, gender);
                saveData(user, client);
            }
        });

        return view;
    }

    private void disablePhoneField() {
        txtPhone.setEnabled(false);
        txtPhone.setFocusable(false);
    }


    private void saveData(final User user, final EasylyApi client) {
        progressDialog.show();
        Call<String> call = client.updateUser(Common.requestTypes[1], user.getName(), user.getAddress(), user.getGender(), user.getEmail(), SplashScreen.id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
                String resp = response.body();
                if (resp.equals("Update Successful")) {
                    loadUserData(client, user.getPhoneNumber());
                    Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();

                } else if (resp.equals("Update Failed")) {
                    Toast.makeText(getActivity(), "Error, Try Again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Cannot Connect to the server, Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserData(EasylyApi easylyApi, String phone) {
        Call<String> call = easylyApi.getUser(Common.requestTypes[2], phone.substring(3));
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
                    Toast.makeText(getActivity(), "Something went wrong try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "cannot connect to the server try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}
