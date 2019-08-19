package com.medicine.emedic.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.medicine.emedic.R;
import com.medicine.emedic.activities.MainActivity;
import com.medicine.emedic.adapter.CategoryAdapter;
import com.medicine.emedic.adapter.ProductAdapter;
import com.medicine.emedic.model.Category;
import com.medicine.emedic.model.Product;
import com.medicine.emedic.network.NetworkSingleton;
import com.medicine.emedic.network.requests.ProductRequest;
import com.medicine.emedic.network.web.API;
import com.medicine.emedic.network.requests.CategoryRequest;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private AutoCompleteTextView searchBarTextView;
    private ArrayAdapter<String> searchAdapter;
    private List<Product> searchedProductList;
    private ArrayList<String> searchList = new ArrayList<String>();
    private String searchString = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("Emedic");
        return view;
    }

    private void init(View view)
    {
        searchBarTextView = (AutoCompleteTextView) view.findViewById(R.id.searchbar2);
        setupSearchBar();

        textViewCategoryName = (TextView) view.findViewById(R.id.textView_category_name);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.subcategory_recyclerview);
        productRecyclerView = (RecyclerView) view.findViewById(R.id.product_recyclerView_cart);

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
        args.putString(KEYS.PRODUCT_IMAGE, product.getProductImage());
        fragment.setArguments(args);

        ((MainActivity) getActivity()).replaceFragments(fragment);
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
                    Log.e("putextracheck",product.getProductImage()+"");
                    args.putString(KEYS.PRODUCT_IMAGE, product.getProductImage());
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
                searchString = editable.toString();
                if(searchString.isEmpty())
                {
                    return;
                }
                String url = API.GET_SEARCH + "?search_string=" + searchString;

                parseJSONForSearch(url);
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
                        String productImage = hit.getString(KEYS.PRODUCT_IMAGE);
                        Log.e("seachimagedata",productImage+ " "+productName);

                        try {
                            float productPrice = Float.valueOf(productPriceString.replace("Tk", ""));

                            searchedProductList.add(new Product(productID, productName, productPrice,productImage+""));
                            Log.e("sadasd",searchedProductList.get(i).getProductImage()+"   "+productName);
                        }catch (Exception ex){

                        }


                        newSuggestionList.add(productName);
                        Log.e("newsugg",newSuggestionList.get(i));

                    }
                    if(getContext() != null)
                    {
                        searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, newSuggestionList);
                        searchBarTextView.setAdapter(searchAdapter);
                        searchAdapter.notifyDataSetChanged();

                    }
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
