package com.example.ponomarev.automobileguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ponomarev.automobileguide.R;
import com.example.ponomarev.automobileguide.adapter.RVAdapter;
import com.example.ponomarev.automobileguide.helper.DBGet;

public class modelsInBrand extends AppCompatActivity implements RVAdapter.RVClickListener{

    private String[] model;
    private String[] modelImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models_in_brand);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getIntent().getStringExtra("parent"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DBGet dbGet=new DBGet(getApplicationContext());
        //model= dbGet.getNames(1,"models_in_brand WHERE parent = \""+ Normalize.ToParentString(getIntent().getStringExtra("parent"))+"\"");
        model= dbGet.getNames(1,"models_in_brand WHERE parent = \""+ getIntent().getStringExtra("parent")+"\"");
        modelImage=dbGet.getImages("image/model_in_brand",model,"png");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void onResume() {
        RecyclerView rv = (RecyclerView) findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        RVAdapter adapter;
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RVAdapter(getApplicationContext(), model, modelImage, R.layout.adapter_grid);
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(this);
        super.onResume();
    }

    @Override
    public void onItemClick(int position, View v) {
        Intent intent=new Intent(modelsInBrand.this, model.class);
        intent.putExtra("parent",model[position]);
        startActivity(intent);
    }

}
