package com.example.fossien.Garanty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fossien.Garanty.data.Product;
import com.example.fossien.Garanty.new_item.NewItemActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class ItemDetailActivity extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AdO8jbral5sqEbdXtqRLH763z0eG6cH-rlWzPT8cRWwgkWXa4gUtk3IinwiPOVvPje3LbgiZTC5y-W0s");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_ITEM_ID, getIntent().getParcelableExtra("PRODUCT"));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
        }

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product mProd = getIntent().getParcelableExtra("PRODUCT");
                db.addProduct(mProd);
                Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
            //    onBuyPressed(view);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, NewItemActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    public void onBuyPressed(View pressed) {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.

        PayPalPayment payment = new PayPalPayment(new BigDecimal("111"), "USD", "name",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }
}
