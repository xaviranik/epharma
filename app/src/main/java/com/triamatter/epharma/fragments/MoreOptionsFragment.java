package com.triamatter.epharma.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.triamatter.epharma.R;
import com.triamatter.epharma.activities.CheckoutActivity;
import com.triamatter.epharma.activities.LoginActivity;
import com.triamatter.epharma.activities.ShowProfileActivity;
import com.triamatter.epharma.utils.GLOBAL;
import com.triamatter.epharma.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class MoreOptionsFragment extends Fragment {

    TextView textViewProfile;
    TextView textViewCallHelpLine;
    TextView textViewLogout;

    ImageView accountImageview;
    ImageView logoutImageview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_more_options, container, false);
        textViewCallHelpLine = (TextView) view.findViewById(R.id.call_helpline_textview);
        textViewProfile = (TextView) view.findViewById(R.id.user_profile_textview);
        textViewLogout = (TextView) view.findViewById(R.id.logout_textview);

        accountImageview = (ImageView) view.findViewById(R.id.imageview_account_profile);
        logoutImageview = (ImageView) view.findViewById(R.id.logout_imageview);

        checkForAuth();

        textViewCallHelpLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+880172222941"));
                startActivity(intent);
            }
        });

        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), ShowProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void checkForAuth()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        boolean isAuthenticated  = prefs.getBoolean(GLOBAL.AUTH_STATUS, false);
        if (isAuthenticated)
        {
            textViewProfile.setEnabled(true);
            textViewLogout.setEnabled(true);

            accountImageview.setAlpha(1f);
            logoutImageview.setAlpha(1f);

            textViewProfile.setTextColor(getResources().getColor(R.color.textColorDark));
            textViewLogout.setTextColor(getResources().getColor(R.color.textColorDark));

        }
        else
        {
            textViewProfile.setEnabled(false);
            textViewLogout.setEnabled(false);

            accountImageview.setAlpha(0.5f);
            logoutImageview.setAlpha(0.5f);

            textViewProfile.setTextColor(Color.argb(50,0,0,0));
            textViewLogout.setTextColor(Color.argb(50,0,0,0));
        }
    }

}
