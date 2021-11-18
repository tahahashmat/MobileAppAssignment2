package com.example.addressfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class view extends AppCompatActivity {

    ListView lst1;
    ArrayList<String> titles = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);

        lst1 = findViewById(R.id.lst1);
        final Cursor c = db.rawQuery("select * from records", null);
        int id = c.getColumnIndex("id");
        int entryOne = c.getColumnIndex("entryOne");
        int entryTwo = c.getColumnIndex("entryTwo");
        int entryThree = c.getColumnIndex("entryThree");
        titles.clear();

        arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, titles);
        lst1.setAdapter(arrayAdapter);

        final ArrayList<Locations> location = new ArrayList<Locations>();

        if (c.moveToFirst())
        {
            do {
                Locations loc = new Locations();
                loc.id = c.getString(id);
                loc.entryOne = c.getString(entryOne);
                loc.entryTwo = c.getString(entryTwo);
                loc.entryThree = c.getString(entryThree);

                location.add(loc);

                titles.add(c.getString(id) + " \t " + c.getString(entryOne) + " \t " + c.getString(entryTwo) + " \t " + c.getString(entryThree) );

            } while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
            lst1.invalidateViews();
        }

        lst1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String aa = titles.get(position).toString();

                Locations lc = location.get(position);
                Intent i = new Intent(getApplicationContext(), edit.class);

                i.putExtra("id", lc.id);
                i.putExtra("entryOne", lc.entryOne);
                i.putExtra("entryTwo", lc.entryTwo);
                i.putExtra("entryThree", lc.entryThree);
                startActivity(i);






            }
        });



    }
}