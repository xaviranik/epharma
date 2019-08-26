package com.medicine.emedic.adapter;

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
import com.medicine.emedic.R;
import com.medicine.emedic.model.Product;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.EmptyRecyclerView;
import com.medicine.emedic.utils.Utils;

import java.util.List;

public class CartAdapter extends EmptyRecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnClearListener clearListener;

    public interface OnItemClickListener {
        void onAddRemoveButtonClick(int position, List<Product> productList);
    }

    public interface OnClearListener {
        void onClearProductClick(int position, List<Product> productList);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        itemClickListener = listener;
    }

    public void setOnClearListener(OnClearListener listener)
    {
        clearListener = listener;
    }

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
        return productList == null ? 0 : productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewProductName;
        public TextView textViewProductPrice;
        public TextView textViewProductQuantity;
        public ImageView imageViewProduct;

        private ImageView buttonAddQuantity;
        private ImageView buttonRemoveQuantity;
        private ImageView buttonClearProduct;

        private Carteasy cart = new Carteasy();

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            textViewProductName = (TextView) itemView.findViewById(R.id.textView_product_name);
            textViewProductPrice = (TextView) itemView.findViewById(R.id.textView_product_price);
            textViewProductQuantity = (TextView) itemView.findViewById(R.id.textView_product_quantity_cart);

            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageView_product);

            buttonAddQuantity = (ImageView) itemView.findViewById(R.id.button_add_quantity);
            buttonRemoveQuantity = (ImageView) itemView.findViewById(R.id.button_minus_quantity_cart);
            buttonClearProduct = (ImageView) itemView.findViewById(R.id.button_clear_product);

            buttonAddQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(itemClickListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            manageQuantity(position, true);
                        }
                    }
                }
            });

            buttonRemoveQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(itemClickListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            manageQuantity(position, false);
                        }
                    }

                }
            });

            buttonClearProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if(clearListener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            Product product = productList.get(position);
                            removeItemFromCart(String.valueOf(product.getProductID()), position);
                            clearListener.onClearProductClick(position, productList);
                        }
                    }

                }
            });
        }

        private void manageQuantity(int position, boolean addButton)
        {
            Product product = productList.get(position);

            product.setProductQuantity(addButton ? product.getProductQuantity() + 1 : product.getProductQuantity() - 1);
            updateCartQuantity(String.valueOf(product.getProductID()), product.getProductQuantity(), position);
            notifyItemChanged(position);
            Utils.updateCartQuantity(context);
            itemClickListener.onAddRemoveButtonClick(position, productList);
        }

        private void updateCartQuantity(String productID, int productQuantity, int position)
        {
            if(productQuantity == 0)
            {
                removeItemFromCart(productID, position);
            }
            else
            {
                cart.update(productID, KEYS.PRODUCT_QUANTITY, productQuantity, context);
            }
        }

        private void removeItemFromCart(String productID, int position)
        {
            productList.remove(position);
            cart.RemoveId(productID, context);
            notifyDataSetChanged();
            Utils.updateCartQuantity(context);
        }
    }
}
