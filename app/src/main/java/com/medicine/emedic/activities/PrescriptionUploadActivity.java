package com.medicine.emedic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.medicine.emedic.R;
import com.medicine.emedic.network.NetworkSingleton;
import com.medicine.emedic.network.web.API;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.GLOBAL;
import com.medicine.emedic.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class PrescriptionUploadActivity extends AppCompatActivity implements View.OnClickListener {
    private String user_id;

    private Button chooseImageButton;
    private Button uploadImageButton;
    private ImageView choosedImageview;

    private Bitmap bitmap;

    private final int IMG_REQUEST = 1;

    private LinearLayout loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_upload);

        checkForAuth();
        choosedImageview = (ImageView) findViewById(R.id.upload_imageView);
        chooseImageButton = (Button) findViewById(R.id.button_choose_image);
        uploadImageButton = (Button) findViewById(R.id.button_upload_prescription);

        loadingView = (LinearLayout) findViewById(R.id.loading_view);

        chooseImageButton.setOnClickListener(this);
        uploadImageButton.setOnClickListener(this);
    }

    private void selectImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    private void uploadImage()
    {
        loadingView.setVisibility(View.VISIBLE);
        final long orderID = generateOrderID();
        final String imageName = "image_" + orderID;

        String url = API.POST_IMAGE_UPLOAD;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String res = jsonObject.getString("response");
                            if(res.equals("success"))
                            {
                                checkout(orderID);
//                                Utils.makeToast(getApplicationContext(), "Image uploaded successfully!");
                            }
                            else
                            {
                                Utils.makeToast(getApplicationContext(), "Image uploading failed!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Utils.makeToast(getApplicationContext(), "Please check your internet connection!");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(KEYS.USER_ID, user_id);
                params.put(KEYS.ORDER_ID, String.valueOf(orderID));
                params.put(KEYS.IMAGE_NAME, imageName);
                params.put(KEYS.IMAGE, imageToString(bitmap));

                return params;
            }
        };

        NetworkSingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    private String imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private long generateOrderID()
    {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    private void checkout(long orderID)
    {
        Intent i = new Intent(PrescriptionUploadActivity.this, ThankYouActivity.class);
        i.putExtra(KEYS.ORDER_ID, orderID);
        i.putExtra("pres","true");

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                choosedImageview.setImageBitmap(bitmap);
                uploadImageButton.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.button_upload_prescription:
            {
                uploadImage();
                break;
            }
            case R.id.button_choose_image:
            {
                selectImage();
                break;
            }
        }
    }

    private void checkForAuth()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        boolean isAuthenticated  = prefs.getBoolean(GLOBAL.AUTH_STATUS, false);
        if (isAuthenticated)
        {
            Utils.makeToast(getApplicationContext(), "You are logged in!");
            checkForProfile();
        }
        else
        {
            Utils.makeToast(getApplicationContext(), "You are not logged in!");
            Intent i = new Intent(PrescriptionUploadActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    private void checkForProfile()
    {
        SharedPreferences prefs = getSharedPreferences(GLOBAL.AUTH_PREF, MODE_PRIVATE);
        user_id = prefs.getString(KEYS.USER_ID, "");
    }
}
