package com.triamatter.epharma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.activities.PrescriptionUploadActivity;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.adapter.ProductAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.NetworkSingleton;
import com.triamatter.epharma.network.requests.ProductRequest;
import com.triamatter.epharma.network.web.API;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.network.requests.CategoryRequest;
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

    private RecyclerView productRecyclerView;
    private RecyclerView.Adapter productAdapter;
    private List<Product> productList;

    private AutoCompleteTextView searchBarTextView;
    private ArrayAdapter<String> searchAdapter;
    private List<Product> searchedProductList;
    private ArrayList<String> searchList = new ArrayList<String>();

    private String searchString = "";

    private Button uploadPrescriptionButton;


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
        searchBarTextView = (AutoCompleteTextView) view.findViewById(R.id.searchbar);
        setupSearchBar();

        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.category_recyclerview);
        productRecyclerView = (RecyclerView) view.findViewById(R.id.trending_recyclerView);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        uploadPrescriptionButton  = (Button) view.findViewById(R.id.button_upload_prescription);

        setupCategoryRecyclerView();
        setupLatestProductRecyclerView();
        setupUploadPrescriptionButton();

        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
    }

    private void setupUploadPrescriptionButton()
    {
        uploadPrescriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), PrescriptionUploadActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSearchBar()
    {
        String url = API.GET_SEARCH + "?search_string=" + searchString;

        parseJSONForSearch(url);

        searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, searchList);
        searchAdapter.setNotifyOnChange(true);
        searchBarTextView.setAdapter(searchAdapter);

        searchBarTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l)
            {
                closeKeyboard();

                if(searchedProductList.size() > 0)
                {
                    Product product = searchedProductList.get(i);

                    Fragment fragment = new ProductFragment();
                    Bundle args = new Bundle();
                    args.putInt(KEYS.PRODUCT_ID, product.getProductID());
                    args.putString(KEYS.PRODUCT_NAME, product.getProductName());
                    args.putFloat(KEYS.PRODUCT_PRICE, product.getProductPrice());
                    fragment.setArguments(args);

                    ((MainActivity) getActivity()).replaceFragments(fragment);
                }
            }
        });

        searchBarTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                searchString = charSequence.toString();
                if(searchString.isEmpty())
                {
                    return;
                }
                String url = API.GET_SEARCH + "?search_string=" + searchString;

                parseJSONForSearch(url);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void parseJSONForSearch(String url)
    {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                ArrayList<String> newSuggestionList = new ArrayList<>();
                searchedProductList = new ArrayList<>();
                try
                {
                    for(int i=0; i<response.length(); i++)
                    {
                        JSONObject hit = response.getJSONObject(i);

                        int productID = hit.getInt(KEYS.PRODUCT_ID);
                        String productName = hit.getString(KEYS.PRODUCT_NAME);
                        String productPriceString = hit.getString(KEYS.PRODUCT_PRICE);
                        float productPrice = Float.valueOf(productPriceString.replace("Tk", ""));

                        searchedProductList.add(new Product(productID, productName, productPrice));

                        newSuggestionList.add(productName);
                    }
                    searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, newSuggestionList);
                    searchBarTextView.setAdapter(searchAdapter);
                }
                catch (JSONException e)
                {
                    Log.i("searcherror", "" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                Log.i("networkerror", "" + error.getMessage());
                Utils.responseErrorHandler(getContext(), error);
            }
        });

        NetworkSingleton.getInstance(getContext()).addToRequestQueue(request);
    }

    private void setupLatestProductRecyclerView()
    {
        productList =  new ArrayList<>();

        productAdapter = new ProductAdapter(productList, getActivity());
        ((ProductAdapter) productAdapter).setOnItemClickListener(HomeFragment.this);
        productRecyclerView.setAdapter(productAdapter);

        String url = API.GET_LATEST_PRODUCT;
        ProductRequest request = new ProductRequest(getActivity(), productList, productAdapter, url);
        request.parseJSON();
    }

    private void setupCategoryRecyclerView()
    {
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        ((CategoryAdapter) categoryAdapter).setOnItemClickListener(HomeFragment.this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        String url = API.GET_CATEGORY;
        CategoryRequest request = new CategoryRequest(getActivity(), categoryList, categoryAdapter, url);
        request.parseJSON();
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
