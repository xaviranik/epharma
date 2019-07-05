package com.triamatter.epharma.network.requests;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.triamatter.epharma.activities.MainActivity;
import com.triamatter.epharma.model.Product;
import com.triamatter.epharma.network.NetworkSingleton;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ProductRequest {
    private Context context;
    private List<Product> list;
    private RecyclerView.Adapter adapter;
    private String url;

    public ProductRequest(Context context, List<Product> list, RecyclerView.Adapter adapter, String url)
    {
        this.context = context;
        this.list = list;
        this.adapter = adapter;
        this.url = url;
    }

    public void parseJSON()
    {
        ((MainActivity)context).setLoadingView(true);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for(int i=0; i<response.length(); i++)
                    {
                        JSONObject hit = response.getJSONObject(i);

                        int productID = hit.getInt(KEYS.PRODUCT_ID);
                        String productName = hit.getString(KEYS.PRODUCT_NAME);
                        String productPriceString = hit.getString(KEYS.PRODUCT_PRICE);
                        float productPrice = Float.valueOf(productPriceString.replace("Tk", ""));

                        list.add(new Product(productID, productName, productPrice));
                    }
                    ((MainActivity)context).setLoadingView(false);
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    Log.i("networkerror", "" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                error.printStackTrace();
                ((MainActivity)context).setLoadingView(false);
                Log.i("networkerror", "" + error.getMessage());
                Utils.responseErrorHandler(context, error);
            }
        })
        {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        NetworkSingleton.getInstance(context).addToRequestQueue(request);

    }
}
