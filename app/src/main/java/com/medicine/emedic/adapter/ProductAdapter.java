package com.medicine.emedic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.medicine.emedic.R;
import com.medicine.emedic.model.Product;
import com.medicine.emedic.utils.Utils;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onProductItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        itemClickListener = listener;
    }

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

        holder.textViewProductPrice.setText(Utils.formatPrice(product.getProductPrice()));

        Glide.with(context)
                .load(product.getProductImage())
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
        public TextView description;
        public ImageView imageViewProduct;
        public CardView mainCardView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            mainCardView = (CardView) itemView.findViewById(R.id.main_product_cardview);
            textViewProductName = (TextView) itemView.findViewById(R.id.textView_product_name);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textView_product_price);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textView_product_price);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageView_product);

            mainCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(itemClickListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            itemClickListener.onProductItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
