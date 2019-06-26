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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.CategoryAdapter;
import com.triamatter.epharma.model.Category;
import com.triamatter.epharma.network.API;
import com.triamatter.epharma.network.Keys;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryFragment extends Fragment implements CategoryAdapter.OnItemClickListener{

    private String categoryName;
    private int categoryID;

    private TextView textViewCategoryName;

    private RecyclerView categoryRecyclerView;
    private RecyclerView.Adapter categoryAdapter;
    private List<Category> categoryList;
    private RequestQueue categoryRequestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        init(view);
        ((MainActivity)getActivity()).setAppTitle("E-Pharma");
        return view;
    }

    private void init(View view)
    {
        textViewCategoryName = (TextView) view.findViewById(R.id.textView_category_name);
        categoryRecyclerView = (RecyclerView) view.findViewById(R.id.subcategory_recyclerview);

        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if(getArguments() != null)
        {
            categoryName = getArguments().getString(Keys.CATEGORY_NAME);
            categoryID = getArguments().getInt(Keys.CATEGORY_ID);
            textViewCategoryName.setText(categoryName);
        }

        setupCategoryRecyclerView();
    }

    private void setupCategoryRecyclerView()
    {
        categoryList = new ArrayList<>();

        categoryAdapter = new CategoryAdapter(categoryList, getActivity());
        ((CategoryAdapter) categoryAdapter).setOnItemClickListener(SubCategoryFragment.this);
        categoryRecyclerView.setAdapter(categoryAdapter);

        parseJSON();
    }

    private void parseJSON()
    {
        categoryRequestQueue = Volley.newRequestQueue(getActivity());
        String url = API.GET_SUBCATEGORY + "?category_id=" + categoryID;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
                Utils.makeToast(getActivity(), "Connection Error: " + error.getMessage());
            }
        });

        categoryRequestQueue.add(request);
    }

    @Override
    public void onCategoryItemClick(int position)
    {
        Category category = categoryList.get(position);

        Utils.makeToast(getActivity(), "" + category.getCategoryId());
    }
}
