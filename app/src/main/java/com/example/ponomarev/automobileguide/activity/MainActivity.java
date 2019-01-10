package com.example.ponomarev.automobileguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.ponomarev.automobileguide.R;
import com.example.ponomarev.automobileguide.adapter.RVAdapter;
import com.example.ponomarev.automobileguide.helper.DBGet;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , RVAdapter.RVClickListener{
    String[] brand;
    String[] brandImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        DBGet dbGet=new DBGet(getApplicationContext());
        brand= dbGet.getNames(0,"brands");
        brandImage=dbGet.getImages("png_logo",brand,"png");

    }

    @Override
    protected void onResume(){
        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        RVAdapter adapter;
        rv.setLayoutManager(new GridLayoutManager(this,2));
        adapter = new RVAdapter(getApplicationContext(),brand,brandImage,R.layout.adapter_grid,"center");
        rv.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        super.onResume();
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent=new Intent(MainActivity.this,modelsInBrand.class);
        intent.putExtra("parent",brand[position]);
        startActivity(intent);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_brands) {
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
