package com.magarex.easyly.Interface;

import com.magarex.easyly.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by HP on 12/22/2017.
 */

public interface EasylyApi {

    @FormUrlEncoded
    @POST("Customer")
    Call<String> saveUser(@Field("requestType") String requestType,
                          @Field("name") String name,
                          @Field("phoneNumber") String phoneNumber,
                          @Field("address") String address,
                          @Field("email") String email,
                          @Field("gender") String gender);

    @FormUrlEncoded
    @POST("Customer")
    Call<String> getUser(@Field("requestType") String requestType,
                         @Field("phoneNumber") String phoneNumber);

    @FormUrlEncoded
    @POST("Customer")
    Call<String> updateUser(@Field("requestType") String requestType,
                            @Field("name") String name,
                            @Field("address") String address,
                            @Field("gender") String gender,
                            @Field("email") String email,
                            @Field("id") long id);

    @FormUrlEncoded
    @POST("ServerStatus")
    Call<String> getStatus(@Field("clientVersionCode") String clientVersionCode);

    @FormUrlEncoded
    @POST("Order")
    Call<String> getOrders(@Field("requestType") String requestType,
                           @Field("customerId") long customerId);

}
