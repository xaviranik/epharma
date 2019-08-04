package com.triamatter.epharma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triamatter.epharma.R;
import com.triamatter.epharma.fragments.CartFragment;
import com.triamatter.epharma.fragments.HomeFragment;
import com.triamatter.epharma.fragments.OrderFragment;
import com.triamatter.epharma.network.web.KEYS;
import com.triamatter.epharma.utils.GLOBAL;
import com.triamatter.epharma.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView appTitle;
    private TextView cartQuantity;

    private LinearLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //checkForAuth();
        init(savedInstanceState);
        updateCartQuantity();
    }

    private void init(Bundle savedInstanceState)
    {
        appTitle = (TextView) findViewById(R.id.app_title);
        cartQuantity = (TextView) findViewById(R.id.cart_quantity_text);

        loadingView = (LinearLayout) findViewById(R.id.loading_view);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        initCartQuantity();
    }

    private void initCartQuantity()
    {
        Log.i("GLOBAL INIT", "" + GLOBAL.CART_QUANTITY);
        Utils.updateCartQuantity(this);
        cartQuantity.setText("(" + GLOBAL.CART_QUANTITY + ")");
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {

                    Fragment selectedFragment = null;
                    switch (item.getItemId())
                    {
                        case R.id.nav_home:
                        {
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (f instanceof HomeFragment)
                            {
                                break;
                            }
                            else
                            {
                                selectedFragment = new HomeFragment();
                                break;
                            }
                        }
                        case R.id.nav_myOrders:
                        {
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (f instanceof OrderFragment)
                            {
                                break;
                            }
                            else
                            {
                                selectedFragment = new OrderFragment();
                                break;
                            }
                        }
                        case R.id.nav_cart:
                        {
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (f instanceof CartFragment)
                            {
                                break;
                            }
                            else
                            {
                                selectedFragment = new CartFragment();
                                break;
                            }
                        }
                        case R.id.nav_more:
                            break;
                    }

                    if(selectedFragment != null)
                    {
                        replaceFragments(selectedFragment);
                    }
                    return true;
                }
            };

    public void setLoadingView(boolean value)
    {
        if (value)
        {
            loadingView.setVisibility(View.VISIBLE);
        }
        else
        {
            loadingView.setVisibility(View.GONE);
        }
    }

    public void updateCartQuantity()
    {
        Log.i("GLOBAL", "" + GLOBAL.CART_QUANTITY);
        cartQuantity.setText("(" + GLOBAL.CART_QUANTITY + ")");
    }

    public void setAppTitle(String title)
    {
        appTitle.setText(title);
    }

    public void replaceFragments(Fragment fragment)
    {
        try
        {
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
