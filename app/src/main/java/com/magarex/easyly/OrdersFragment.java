package com.magarex.easyly;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.magarex.easyly.Adapters.OrderAdapter;
import com.magarex.easyly.Common.Common;
import com.magarex.easyly.Interface.EasylyApi;
import com.magarex.easyly.Models.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    private View view;
    private RecyclerView RvOrders;
    private ArrayList<Order> MyOrders = new ArrayList<Order>();
    private OrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_orders, container, false);
        RvOrders = view.findViewById(R.id.RvOrders);

        long id = SplashScreen.id;

        //init RetrofitClient
        Retrofit retrofit = Common.retrofit;

        EasylyApi client = retrofit.create(EasylyApi.class);

        Call<String> call = client.getOrders(Common.requestTypes[3], Long.parseLong("1"));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String resp = response.body();
                if (!resp.equals("")) {
                    String[] Orders = resp.split("%");
                    for (String Order1 : Orders) {
                        String[] Order = Order1.split("#");
                        Order o = new Order(Long.parseLong(Order[0]), Long.parseLong(Order[1]), Long.parseLong(Order[2]), Order[3], Order[4], Order[5], Order[6], Order[7], Order[8], Order[9], Order[10]);
                        MyOrders.add(o);
                    }
                    adapter = new OrderAdapter(getActivity(), MyOrders);
                    RvOrders.setAdapter(adapter);
                    RvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));

                } else {
                    Toast.makeText(getActivity(), "Something went wrong try again", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Unable to connect with the server", Toast.LENGTH_LONG).show();
            }
        });

        return view;

    }

}
