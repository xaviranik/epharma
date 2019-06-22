package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final int CATEGORY_SPAN_COUNT = 4;

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        closeKeyboard();
        init(view);
        return view;
    }

    private void init(View view)
    {
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), CATEGORY_SPAN_COUNT));

        productRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recyclerView);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupCategoryRecyclerView();
        setupTrendingRecyclerView();

        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
    }

    private void setupTrendingRecyclerView()
    {
        productList =  new ArrayList<>();

        Product product_1 = new Product("Test Name of medicine", 150.25f);
        Product product_2 = new Product("Test 2 Name of Medicine", 1.25f);

        productList.add(product_1);
        productList.add(product_2);

        productAdapter = new ProductAdapter(productList, getActivity());
        productRecyclerView.setAdapter(productAdapter);
    }

    private void setupCategoryRecyclerView()
    {
        categoryList = new ArrayList<>();

        Category category_1 = new Category("First Category", R.drawable.ic_shopping_cart);
        Category category_2 = new Category("Test Category", R.drawable.ic_shopping_cart);

        categoryList.add(category_1);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);
        categoryList.add(category_2);

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void closeKeyboard()
    {
        View currentFocus = getActivity().getCurrentFocus();
        if (currentFocus!=null)
        {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
