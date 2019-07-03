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
import com.carteasy.v1.lib.Carteasy;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.Utils;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public CartAdapter(List<Product> productList, Context context)
    {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position)
    {
        Product product = productList.get(position);
        holder.textViewProductName.setText(product.getProductName());
        holder.textViewProductPrice.setText(Utils.formatPrice(product.getProductPrice()));
        holder.textViewProductQuantity.setText(String.valueOf(product.getProductQuantity()));

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
        public TextView textViewProductQuantity;
        public ImageView imageViewProduct;

        private ImageView buttonAddQuantity;
        private ImageView buttonRemoveQuantity;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewProductName = (TextView) itemView.findViewById(R.id.textView_product_name);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textView_product_price);
            textViewProductQuantity = (TextView) itemView.findViewById(R.id.textView_product_quantity_cart);

            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageView_product);

            buttonAddQuantity = (ImageView) itemView.findViewById(R.id.button_add_quantity);
            buttonRemoveQuantity = (ImageView) itemView.findViewById(R.id.button_minus_quantity_cart);

            buttonAddQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        manageQuantity(position, true);
                    }
                }
            });

            buttonRemoveQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION)
                    {
                        manageQuantity(position, false);
                    }
                }
            });
        }

        private void manageQuantity(int position, boolean addButton)
        {
            Product product = productList.get(position);

            product.setProductQuantity(addButton ? product.getProductQuantity() + 1 : product.getProductQuantity() - 1);
            notifyItemChanged(position);
            updateCartQuantity(String.valueOf(product.getProductID()), product.getProductQuantity());
        }

        private void updateCartQuantity(String productID, int productQuantity)
        {
            Carteasy cart = new Carteasy();

            cart.update(productID, KEYS.PRODUCT_QUANTITY, productQuantity, context);

            Utils.updateCartQuantity(context);
            ((MainActivity)context).updateCartQuantity();
        }
    }
}
