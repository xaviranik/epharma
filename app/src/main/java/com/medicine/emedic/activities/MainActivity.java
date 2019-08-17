package com.medicine.emedic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.medicine.emedic.R;
import com.medicine.emedic.fragments.CartFragment;
import com.medicine.emedic.fragments.HomeFragment;
import com.medicine.emedic.fragments.MoreOptionsFragment;
import com.medicine.emedic.fragments.OrderFragment;
import com.medicine.emedic.utils.GLOBAL;
import com.medicine.emedic.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView appTitle;
    private TextView cartQuantity;

    private LinearLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
        updateCartQuantity();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel=new NotificationChannel("enoti","enoti", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


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
                        {
                            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if (f instanceof MoreOptionsFragment)
                            {
                                break;
                            }
                            else
                            {
                                selectedFragment = new MoreOptionsFragment();
                                break;
                            }
                        }
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
