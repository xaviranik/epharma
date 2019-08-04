package com.triamatter.epharma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Order;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.EmptyRecyclerView;
import com.triamatter.epharma.utils.Utils;

import java.util.List;

public class OrderAdapter extends EmptyRecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context context;

    public OrderAdapter(List<Order> orderList, Context context)
    {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_status, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position)
    {
        Order order = orderList.get(position);
        holder.textViewOrderID.setText("Order ID: #" + order.getOrder_id());
        holder.textViewOrderDate.setText(order.getOrder_date());

        switch (order.getOrder_status())
        {
            case "wc-processing":
                holder.textViewOrderStatus.setText("Status: Order on processing");
                break;
            case "wc-on-hold":
                holder.textViewOrderStatus.setText("Status: Order on hold");
                holder.textViewOrderStatus.setBackgroundColor(Color.rgb(189, 165, 34));
                break;
            case "wc-completed":
                holder.textViewOrderStatus.setText("Status: Order completed");
                holder.textViewOrderStatus.setBackgroundColor(Color.rgb(24, 135, 168));
                break;
            case "wc-cancelled":
                holder.textViewOrderStatus.setText("Status: Order cancelled");
                holder.textViewOrderStatus.setBackgroundColor(Color.RED);
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        return orderList == null ? 0 : orderList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewOrderID;
        public TextView textViewOrderStatus;
        public TextView textViewOrderDate;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewOrderID = (TextView) itemView.findViewById(R.id.order_id_textview);
            textViewOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
            textViewOrderDate = (TextView) itemView.findViewById(R.id.order_date_textview);
        }
    }
}
