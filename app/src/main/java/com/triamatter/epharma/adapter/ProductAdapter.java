package com.triamatter.epharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context)
    {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Product product = productList.get(position);

        holder.textViewProductName.setText(product.getProductName());

        String productPrice = String.valueOf(product.getProductPrice());
        holder.textViewProductPrice.setText("BDT.  " + productPrice);

        Glide.with(context)
                .load("")
                .placeholder(R.drawable.ic_medicine_default)
                .into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount()
    {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewProductName;
        public TextView textViewProductPrice;
        public ImageView imageViewProduct;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textViewProductName = (TextView) itemView.findViewById(R.id.textView_product_name);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textView_product_price);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageView_product);
        }
    }
}
