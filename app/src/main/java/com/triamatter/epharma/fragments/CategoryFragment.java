package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.requests.ProductRequest;
import com.triamatter.epharma.network.web.API;
import com.triamatter.epharma.network.requests.CategoryRequest;
import com.triamatter.epharma.network.web.KEYS;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements ProductAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener{

    private String categoryName;
    private int categoryID;

    private TextView textViewCategoryName;

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private List<Category> categoryList;

    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productAdapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
        return view;
    }

    private void init(View view)
    {
        textViewCategoryName = (TextView) view.findViewById(R.id.textView_category_name);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.subcategory_recyclerview);
        productRecyclerView = (RecyclerView) view.findViewById(R.id.subcategory_product_recyclerview);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));

        getArgumentsFromPreviousFragment();

        setupCategoryRecyclerView();
        setupProductRecyclerView();
    }

    private void setupProductRecyclerView()
    {
        productList =  new ArrayList<>();

        productAdapter = new ProductAdapter(productList, getActivity());
        ((ProductAdapter) productAdapter).setOnItemClickListener(CategoryFragment.this);
        productRecyclerView.setAdapter(productAdapter);

        String url = API.GET_PRODUCT + "?category_id=" + categoryID;
        ProductRequest request = new ProductRequest(getActivity(), productList, productAdapter, url);
        request.parseJSON();
    }

    private void setupCategoryRecyclerView()
    {
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        ((CategoryAdapter) categoryAdapter).setOnItemClickListener(CategoryFragment.this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        String url = API.GET_SUBCATEGORY + "?category_id=" + categoryID;
        CategoryRequest request = new CategoryRequest(getActivity(), categoryList, categoryAdapter, url);
        request.parseJSON();
    }

    private void getArgumentsFromPreviousFragment()
    {
        if(getArguments() != null)
        {
            categoryName = getArguments().getString(KEYS.CATEGORY_NAME);
            categoryID = getArguments().getInt(KEYS.CATEGORY_ID);

            textViewCategoryName.setText(categoryName);
        }
    }

    @Override
    public void onCategoryItemClick(int position)
    {
        Category category = categoryList.get(position);

        Fragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(KEYS.CATEGORY_ID, category.getCategoryId());
        args.putString(KEYS.CATEGORY_NAME, category.getCategoryName());
        fragment.setArguments(args);

        ((MainActivity) getActivity()).replaceFragments(fragment);
    }

    @Override
    public void onProductItemClick(int position)
    {
        Product product = productList.get(position);

        Fragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt(KEYS.PRODUCT_ID, product.getProductID());
        args.putString(KEYS.PRODUCT_NAME, product.getProductName());
        args.putFloat(KEYS.PRODUCT_PRICE, product.getProductPrice());
        fragment.setArguments(args);

        ((MainActivity) getActivity()).replaceFragments(fragment);
    }
}
