package com.triamatter.epharma.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.adapter.OrderAdapter;
import com.triamatter.epharma.model.Order;
import com.triamatter.epharma.network.NetworkSingleton;
import com.triamatter.epharma.network.web.API;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.EmptyRecyclerView;
import com.triamatter.epharma.utils.GLOBAL;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {
    private String user_id;

    private EmptyRecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ((MainActivity)getActivity()).setAppTitle("My Orders");

        getUserID();

        orderRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.order_status_recyclerview);
        textViewEmpty = (TextView) view.findViewById(R.id.emptyView2);

        setupOrderStatusRecyclerView();

        return view;
    }

    private void getUserID()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        user_id = prefs.getString(KEYS.USER_ID, "");
    }

    private void setupOrderStatusRecyclerView()
    {
        orderList =  new ArrayList<>();

        orderAdapter = new OrderAdapter(orderList, getContext());

        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.setAdapter(orderAdapter);

        orderRecyclerView.setEmptyView(textViewEmpty);


        getOrderStatus();
    }

    private void getOrderStatus()
    {
        ((MainActivity)getActivity()).setLoadingView(true);
        String url = API.POST_ORDER_TRACK;

        StringRequest shippingRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String order_id = jsonObject.getString("order_id");
                                String order_status = jsonObject.getString("order_status");
                                String order_date = jsonObject.getString("order_Date");
                                orderList.add(new Order(order_id, order_status, order_date));
                            }

                            ((MainActivity)getActivity()).setLoadingView(false);
                            orderAdapter.notifyDataSetChanged();


                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.USER_ID, user_id);
                return params;
            }
        };

        NetworkSingleton.getInstance(getContext()).addToRequestQueue(shippingRequest);
    }
}
