package com.example.fossien.Garanty.cart;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.fossien.Garanty.DatabaseHandler;
import com.example.fossien.Garanty.R;
import com.example.fossien.Garanty.RechercheActivity;
import com.example.fossien.Garanty.brand.BrandActivity;
import com.example.fossien.Garanty.category.CategoryActivity;
import com.example.fossien.Garanty.data.Product;
import com.example.fossien.Garanty.new_item.NewItemActivity;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> mProductList = new ArrayList<>();
    private Button payBtn;
    DatabaseHandler db = new DatabaseHandler(this);

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent mTntent = new Intent(getApplicationContext(), RechercheActivity.class);
            mTntent.putExtra("SEARCH_DATA", query);
            startActivity(mTntent);
        }
    }

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AdO8jbral5sqEbdXtqRLH763z0eG6cH-rlWzPT8cRWwgkWXa4gUtk3IinwiPOVvPje3LbgiZTC5y-W0s");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        handleIntent(getIntent());

        payBtn = (Button) findViewById(R.id.payB);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyPressed(view);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fillWithString();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //fillWithString(/*mProductList*/);
        mProductList = db.getAllproducts();

        // specify an adapter (see also next example)
        mAdapter = new CartAdapter(this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


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
        double somme = 0;
        String pList = "";
        for(Product p: mProductList) {
            somme += p.getPrice();
            pList += p.getName() + " ";
        }
        PayPalPayment payment = new PayPalPayment(new BigDecimal(somme), "USD", pList, PayPalPayment.PAYMENT_INTENT_SALE);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_brand) {
            Intent intent = new Intent(getApplicationContext(), BrandActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_newItems) {
            Intent intent = new Intent(getApplicationContext(), NewItemActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_category) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_contact) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kriasofien@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Garanty App");
            i.putExtra(Intent.EXTRA_TEXT   , "Your App is awesome!");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}