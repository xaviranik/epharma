package com.triamatter.epharma.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.triamatter.epharma.R;

public class MoreOptionsFragment extends Fragment {

    private Button buttonHelpCenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_more_options, container, false);
        buttonHelpCenter = (Button) view.findViewById(R.id.button_call_help_center);
        buttonHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+880172222941"));
                startActivity(intent);
            }
        });

        return view;
    }

}
