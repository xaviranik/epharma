package com.triamatter.epharma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.triamatter.epharma.R;
import com.triamatter.epharma.fragments.CartFragment;
import com.triamatter.epharma.fragments.HomeFragment;
import com.triamatter.epharma.fragments.OrderFragment;

public class MainActivity extends AppCompatActivity {

    TextView appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState)
    {
        appTitle = (TextView) findViewById(R.id.app_title);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
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
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_myOrders:
                            selectedFragment = new OrderFragment();
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new CartFragment();
                            break;
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
