package com.example.addressfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class edit extends AppCompatActivity {

    EditText longitude, latitude, id;
    Button button, buttonTwo, buttonThree, buttonFour;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        id = findViewById(R.id.id);
        button = findViewById(R.id.btn);
        buttonTwo = findViewById(R.id.btn2);
        buttonThree = findViewById(R.id.btn3);
        buttonFour = findViewById(R.id.btn4);
        tvAddress = findViewById(R.id.tv_address);

        Intent i = getIntent();

        String t1 = i.getStringExtra("id").toString();
        String t2 = i.getStringExtra("entryOne").toString();
        String t3 = i.getStringExtra("entryTwo").toString();
        String t4 = i.getStringExtra("entryThree").toString();

        id.setText(t1);
        longitude.setText(t2);
        latitude.setText(t3);
        tvAddress.setText(t4);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double lon = Double.parseDouble(longitude.getText().toString());
                double lat = Double.parseDouble(latitude.getText().toString());

                GeoLocation geoLocation = new GeoLocation();
                geoLocation.getAddress(lon, lat, getApplicationContext(), new GeoHandler());

            }
        });

        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Edit();
            }
        });


        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete();
            }
        });

        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

    }



    public void Edit(){
        try {
            String entryOne = longitude.getText().toString();
            String entryTwo = latitude.getText().toString();
            String entryThree = tvAddress.getText().toString();
            String entryFour = id.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);

            String sql  = "update records set entryOne=?, entryTwo=?, entryThree=? where id=?";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,entryOne);
            statement.bindString(2,entryTwo);
            statement.bindString(3,entryThree);
            statement.bindString(4,entryFour);
            statement.execute();
            Toast.makeText(this, "Record Updated", Toast.LENGTH_LONG).show();

            longitude.setText("");
            latitude.setText("");
            tvAddress.setText("");
            longitude.requestFocus();


        } catch (Exception exception) {
            Toast.makeText(this, "Record Update Failed", Toast.LENGTH_LONG).show();
        }
    }


    public void Delete(){
        try {

            String entryFour = id.getText().toString();


            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);

            String sql  = "delete from records where id = ?";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1,entryFour);
            statement.execute();
            Toast.makeText(this, "Record Deleted", Toast.LENGTH_LONG).show();

            longitude.setText("");
            latitude.setText("");
            tvAddress.setText("");
            longitude.requestFocus();


        } catch (Exception exception) {
            Toast.makeText(this, "Record Delete Failed", Toast.LENGTH_LONG).show();
        }
    }


    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            String address;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    break;
                default:
                    address = null;
            }
            tvAddress.setText(address);

        }
    }

}