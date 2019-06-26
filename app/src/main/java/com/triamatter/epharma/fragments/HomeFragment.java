package com.triamatter.epharma.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.API;
import com.triamatter.epharma.network.Keys;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductAdapter.OnItemClickListener, CategoryAdapter.OnItemClickListener {

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private List<Category> categoryList;
    private RequestQueue categoryRequestQueue;

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
        productRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recyclerView);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        setupCategoryRecyclerView();
        setupTrendingRecyclerView();

        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
    }

    private void setupTrendingRecyclerView()
    {
        productList =  new ArrayList<>();

        Product product = new Product("test product name", 25f);
        productList.add(product);
        productAdapter = new ProductAdapter(productList, getActivity());
        ((ProductAdapter) productAdapter).setOnItemClickListener(HomeFragment.this);
        productRecyclerView.setAdapter(productAdapter);
    }

    private void setupCategoryRecyclerView()
    {
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        ((CategoryAdapter) categoryAdapter).setOnItemClickListener(HomeFragment.this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        parseJSON();
    }

    private void parseJSON()
    {
        categoryRequestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API.GET_CATEGORY, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
            try
            {
                for(int i=0; i<response.length(); i++)
                {
                    JSONObject hit = response.getJSONObject(i);

                    int categoryId = hit.getInt(Keys.CATEGORY_ID);
                    String categoryName = hit.getString(Keys.CATEGORY_NAME);

                    categoryList.add(new Category(categoryId, categoryName));
                }
                categoryAdapter.notifyDataSetChanged();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Utils.makeToast(getActivity(), "Connection Error!");
            }
        });

        categoryRequestQueue.add(request);
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

    @Override
    public void onProductItemClick(int position)
    {
        Product product = productList.get(position);
        Utils.makeToast(getActivity(), "" + product.getProductName() + " " + product.getProductPrice());
    }

    @Override
    public void onCategoryItemClick(int position)
    {
        Category category = categoryList.get(position);

        Fragment fragment = new SubCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(Keys.CATEGORY_ID, category.getCategoryId());
        args.putString(Keys.CATEGORY_NAME, category.getCategoryName());
        fragment.setArguments(args);

        ((MainActivity) getActivity()).replaceFragments(fragment);
    }
}
