package com.example.ponomarev.automobileguide.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andrey on 14.04.2018.
 */

public class DBGet extends DatabaseHelper {

    //private String from;
    private String where;

    SQLiteDatabase mSQLiteDatabase;
    public DBGet(Context context) {
        super(context);
        //this.from=from;

    }

    public String[] getNames(int positionInDB,String from){
        try {
            updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }


        try {
            mSQLiteDatabase = getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "+from, null);
        cursor.moveToFirst();
        String[] name = new String[cursor.getCount()];

        int i = 0;
        while (!cursor.isAfterLast()) {
            name[i] = cursor.getString(positionInDB);
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return name;
    }

    public String[] getNamesList(int positionInDB, String from){
        try {
            updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }


        try {
            mSQLiteDatabase = getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "+from, null);
        cursor.moveToFirst();
        List<Object> names = new ArrayList<>();

        int i = 0;
        while (!cursor.isAfterLast()) {
            if(!names.contains(cursor.getString(positionInDB)))
            names.add(cursor.getString(positionInDB)) ;
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            names=names.stream().distinct().collect(Collectors.toList());
        }
        String[] s= names.toArray(new String[0]);
        return s;
    }

    public String[] getImages(String folder,String[] name,String extension){
        String[] images=new String[name.length];
        for(int i=0;i<name.length;i++)
        {
            images[i] = folder+"/"+name[i]+"."+extension;
        }
        return images;
    }

    public String[] getImages(String folder,String extension,String from){
        try {
            updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }


        try {
            mSQLiteDatabase = getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * FROM "+from, null);
        cursor.moveToFirst();
        String[] images = new String[cursor.getCount()];

        int i = 0;
        while (!cursor.isAfterLast()) {
            images[i] = folder+"/"+cursor.getString(1)+"."+extension;
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return images;
    }

}
