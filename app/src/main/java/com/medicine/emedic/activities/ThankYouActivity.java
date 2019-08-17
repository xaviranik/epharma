package com.medicine.emedic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.medicine.emedic.R;
import com.medicine.emedic.network.web.KEYS;
import com.medicine.emedic.utils.Utils;

public class ThankYouActivity extends AppCompatActivity {

    private long orderID;

    private TextView textViewOrderID;
    private Button continueShoppingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        textViewOrderID = (TextView) findViewById(R.id.textView_order_id);
        continueShoppingButton = (Button) findViewById(R.id.button_continue_shopping);

        Utils.makeSuccessAlert(ThankYouActivity.this, "Your order has been placed successfully!", null, R.drawable.ic_check_order_confirmation);

        orderID = getIntent().getLongExtra(KEYS.ORDER_ID, 0);
        if(orderID != 0)
        {
            textViewOrderID.setText("Order ID: #" + orderID);
        }

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(ThankYouActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
    }
}
