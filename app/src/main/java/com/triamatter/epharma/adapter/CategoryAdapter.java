package com.triamatter.epharma.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triamatter.epharma.R;
import com.triamatter.epharma.model.Category;

import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categoryList;
    private Context context;

    public CategoryAdapter(List<Category> categoryList, Context context)
    {
        this.categoryList = categoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Category category = categoryList.get(position);

        holder.textViewCategoryName.setText(category.getCategoryName());
        holder.textViewCategoryName.setEllipsize(TextUtils.TruncateAt.END);
        holder.textViewCategoryName.setMaxLines(2);
        holder.imageViewCategoryBackground.setColorFilter(getRandomColor(), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @Override
    public int getItemCount()
    {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewCategoryName;
        public ImageView imageViewCategoryIcon;
        public ImageView imageViewCategoryBackground;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textViewCategoryName = (TextView) itemView.findViewById(R.id.textView_category_title);
            imageViewCategoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);
            imageViewCategoryBackground = (ImageView) itemView.findViewById(R.id.category_bg);
        }

    }

    private int getRandomColor()
    {
        int[] categoryColors = context.getResources().getIntArray(R.array.categoryColors);
        return categoryColors[new Random().nextInt(categoryColors.length)];
    }
}
