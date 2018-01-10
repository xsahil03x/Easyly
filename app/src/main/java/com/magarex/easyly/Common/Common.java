package com.magarex.easyly.Common;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by HP on 12/25/2017.
 */

public class Common {

    public static final String BASE_URL = "http://192.168.1.9:8080/Easyly/";

    public static final String FILE_NAME = "MyEasylyFile";
    public static final String KEY = "mobNumber";

    public static final String[] requestTypes = {"register", "updateProfile", "fetchByPhoneNumber", "fetchByCustomerId"};

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
