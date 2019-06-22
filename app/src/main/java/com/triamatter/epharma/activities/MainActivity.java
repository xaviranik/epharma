package com.triamatter.epharma.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.triamatter.epharma.R;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CATEGORY_SPAN_COUNT = 4;

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private List<Category> categoryList;

    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeKeyboard();

        init();
    }

    private void init()
    {
        categoryRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, CATEGORY_SPAN_COUNT));

        productRecyclerView = (RecyclerView) findViewById(R.id.trending_recyclerView);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        setupCategoryRecyclerView();
        setupTrendingRecyclerView();
    }

    private void setupTrendingRecyclerView()
    {
        productList =  new ArrayList<>();

        Product product_1 = new Product("Test Name of medicine", 150.25f);
        Product product_2 = new Product("Test 2 Name of Medicine", 1.25f);

        productList.add(product_1);
        productList.add(product_2);

        productAdapter = new ProductAdapter(productList, getApplicationContext());
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

        categoryAdapter = new CategoryAdapter(categoryList, getApplicationContext());
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    private void closeKeyboard()
    {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus!=null)
        {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)this.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
