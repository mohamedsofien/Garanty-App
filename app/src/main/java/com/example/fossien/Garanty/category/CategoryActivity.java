package com.example.fossien.Garanty.category;

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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fossien.Garanty.RechercheActivity;
import com.example.fossien.Garanty.cart.CartActivity;
import com.example.fossien.Garanty.new_item.NewItemActivity;
import com.example.fossien.Garanty.data.Product;
import com.example.fossien.Garanty.R;
import com.example.fossien.Garanty.RecyclerTouchListener;
import com.example.fossien.Garanty.SpaceItemDecoration;
import com.example.fossien.Garanty.brand.BrandActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = CategoryActivity.class.getSimpleName();

    private ProgressDialog progressDialog;

    private RecyclerView mRecyclerView;
    private CategoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
            //use the query to search your data somehow
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        List<Product> productList = new ArrayList<Product>();
        // fillProductItems(productList);
        readProductList(productList);

    }

    private void initRecyclerViewProduct(final List<Product> productList) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(2, 12, false));

        // specify an adapter (see also next example)
        mAdapter = new CategoryAdapter(productList, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(CategoryActivity.this, CategoryItemsActivity.class);
                TextView mTextView = view.findViewById(R.id.name_category);
                intent.putExtra("CATEGORY", mTextView.getText().toString());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    /**
     * Fetch product list from Firebase
     *
     * @param productList
     */
    private void readProductList(final List<Product> productList) {
        Product product = new Product("","Furniture", 0,"https://d30y9cdsu7xlg0.cloudfront.net/png/132427-200.png");
        productList.add(product);
        product = new Product("","Multimedia", 0,"https://cdn4.iconfinder.com/data/icons/proglyphs-computers-and-development/512/iMac-512.png");
        productList.add(product);
        product = new Product("","Vehicle", 0,"https://cdn3.iconfinder.com/data/icons/car-icons-front-views/480/Sports_Car_Front_View-512.png");
        productList.add(product);
        product = new Product("","Entertainment", 0,"https://cdn0.iconfinder.com/data/icons/huge-basic-icons-part-3/512/Joystick.png");
        productList.add(product);
        product = new Product("","Clothes", 0,"https://d30y9cdsu7xlg0.cloudfront.net/png/23041-200.png");
        productList.add(product);
        initRecyclerViewProduct(productList);
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

        } else if (id == R.id.nav_category) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);

        }  else if (id == R.id.nav_contact) {

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
