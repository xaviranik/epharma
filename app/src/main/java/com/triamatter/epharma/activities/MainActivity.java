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
import com.triamatter.epharma.model.Category;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CATEGORY_SPAN_COUNT = 4;

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter categoryAdapter;

    private List<Category> categoryList;

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

    private void closeKeyboard() {
        View currentFocus = this.getCurrentFocus();
        if (currentFocus!=null)
        {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)this.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
