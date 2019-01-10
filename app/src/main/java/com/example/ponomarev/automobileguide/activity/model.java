package com.example.ponomarev.automobileguide.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ponomarev.automobileguide.R;
import com.example.ponomarev.automobileguide.adapter.ExpandableRecyclerAdapter;
import com.example.ponomarev.automobileguide.helper.DBGet;
import com.example.ponomarev.automobileguide.helper.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class model extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static String[] model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DBGet dbGet=new DBGet(getApplicationContext());
        model= dbGet.getNamesList(1,"model WHERE parent = \"" + getIntent().getStringExtra("parent") + "\"");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        this.setTitle(getIntent().getStringExtra("parent"));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (model.length > 3) tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemSelectedListener{
        ArrayList<String> infoHeader;
        ArrayList<ArrayList<ArrayList<String>>> information;

        private static final String ARG_SECTION_NUMBER = "section_number";
        private SQLiteDatabase mSQLiteDatabase;
        private Spinner spinner;
        private int positionInSpinner=0;
        RecyclerView recycler;
        String[] mod;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_model, container, false);

            DBGet dbGet=new DBGet(getContext());
            mod=dbGet.getNames(2,"model WHERE name=\""+model[getArguments().getInt(ARG_SECTION_NUMBER)-1]+"\"");
            spinner=(Spinner)rootView.findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, mod);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
            recycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);

            return rootView;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            positionInSpinner=position;
            getData(getContext(),mod[positionInSpinner]);

            recycler.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recycler.setLayoutManager(layoutManager);
            recycler.setAdapter(new ExpandableRecyclerAdapter(infoHeader,information));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }

        private void getData(Context context,String mod){
            infoHeader=new ArrayList<>();
            infoHeader.add("Общие сведения");
            infoHeader.add("Двигатель");
            infoHeader.add("Трансмиссия");
            infoHeader.add("Эксплуатационные показатели");
            infoHeader.add("Объем и масса");
            infoHeader.add("Размеры");
            infoHeader.add("Подвеска и тормоза");
            int[] infoCount={10,7,2,4,4,5,4};

            information=new ArrayList<>();
            ArrayList<ArrayList<String>> item=new ArrayList<>();
            DatabaseHelper databaseHelper=new DatabaseHelper(context);
            try {
                databaseHelper.updateDataBase();
            } catch (IOException mIOException) {
                throw new Error("UnableToUpdateDatabase");
            }
            try {
                mSQLiteDatabase = databaseHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                throw mSQLException;
            }
            Cursor cursor1 = mSQLiteDatabase.rawQuery("SELECT * FROM model WHERE name=\"prop\"", null);
            Cursor cursor2 = mSQLiteDatabase.rawQuery("SELECT * FROM model WHERE mod=\""+mod+"\"", null);
            cursor1.moveToFirst();
            cursor2.moveToFirst();

            int j=3;
            for(int i=0;i<infoCount.length;i++) {
                item=new ArrayList<>();
                ArrayList<String> itemHeader=new ArrayList<>();
                ArrayList<String> itemDesc=new ArrayList<>();
                for(int z = 0; z < infoCount[i]; z++, j++) {
                    itemHeader.add(cursor1.getString(j));
                    itemDesc.add(cursor2.getString(j));
                }
                item.add(itemHeader);
                item.add(itemDesc);
                information.add(item);
            }
            cursor1.close();
            cursor2.close();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            if (model != null) return model.length;
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0) return model[position];
            return null;
        }
    }
}
