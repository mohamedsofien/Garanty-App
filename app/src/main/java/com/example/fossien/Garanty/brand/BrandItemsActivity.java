package com.example.fossien.Garanty.brand;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.fossien.Garanty.R;
import com.example.fossien.Garanty.RechercheActivity;
import com.example.fossien.Garanty.cart.CartActivity;
import com.example.fossien.Garanty.category.CategoryActivity;
import com.example.fossien.Garanty.data.Product;
import com.example.fossien.Garanty.new_item.MyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BrandItemsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Product> mProductList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String brand = getIntent().getStringExtra("BRAND");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("products");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final ProgressDialog myProgress = new ProgressDialog(this);
        myProgress.setMessage("loading");
        myProgress.show();
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //fillWithString(/*mProductList*/);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
        
        myRef.orderByChild("createdAt");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                myProgress.dismiss();
                for (DataSnapshot mySnap: dataSnapshot.getChildren()) {
                    Product prod = mySnap.getValue(Product.class);
                    if (prod.getMarque().equals(brand)) {
                        mProductList.add(prod);
                        Log.d("TAG", "Value is: " + prod.toString());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

    }
    private void fillWithString(/*List<Product> myList*/) {
        for(int i = 0; i < 10; i++){
            Product prod = new Product("" + 1, "product" + 1, 1, "http://www.runnersworld.com/sites/runnersworld.com/files/styles/slideshow-desktop/public/nike-zoom-span_m_400.jpg");
            prod.setCategory("Furniture");
            myRef.push().setValue(prod);

            prod = new Product("" + 2, "product" + 2, 2, "http://www.runnersworld.com/sites/runnersworld.com/files/styles/slideshow-desktop/public/nike-zoom-span_m_400.jpg");
            prod.setCategory("Multimedia");
            myRef.push().setValue(prod);

            prod = new Product("" + 3, "product" + 3, 3, "http://www.runnersworld.com/sites/runnersworld.com/files/styles/slideshow-desktop/public/nike-zoom-span_m_400.jpg");
            prod.setCategory("Clothes");
            myRef.push().setValue(prod);

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
            Intent intent = new Intent(getApplicationContext(), BrandItemsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_category) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_cart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
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