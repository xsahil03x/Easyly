package com.magarex.easyly.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magarex.easyly.Models.Order;
import com.magarex.easyly.R;

import java.util.List;

/**
 * Created by HP on 1/1/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>{

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.fragment_orders_row, parent, false);
            return new OrderViewHolder(view);

    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.order_category.setText(orders.get(position).getCategory());
        holder.order_id.setText(String.valueOf(orders.get(position).getId()));
        holder.order_status.setText(orders.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}

class OrderViewHolder extends RecyclerView.ViewHolder {

    TextView order_category,order_id,order_status;

    public OrderViewHolder(View itemView) {
        super(itemView);

        order_category = itemView.findViewById(R.id.order_category);
        order_id = itemView.findViewById(R.id.order_id);
        order_status = itemView.findViewById(R.id.order_status);

    }
}
