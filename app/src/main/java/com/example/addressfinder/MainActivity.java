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

public class MainActivity extends AppCompatActivity {

    EditText longitude, latitude;
    Button button, buttonTwo, buttonThree;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        button = findViewById(R.id.btn);
        buttonTwo = findViewById(R.id.btn2);
        buttonThree = findViewById(R.id.btn3);
        tvAddress = findViewById(R.id.tv_address);

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
                insert();
            }
        });

        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), view.class);
                startActivity(i);

            }
        });

    }


    public void insert(){
        try {
            String entryOne = longitude.getText().toString();
            String entryTwo = latitude.getText().toString();
            String entryThree = tvAddress.getText().toString();

            SQLiteDatabase db = openOrCreateDatabase("SliteDb", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS records(id INTEGER PRIMARY KEY AUTOINCREMENT, entryOne VARCHAR, entryTwo VARCHAR, entryThree VARCHAR)");

            String sql  = "insert into records(entryOne, entryTwo, entryThree)values(?,?,?)";
            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindString(1,entryOne);
            statement.bindString(2,entryTwo);
            statement.bindString(3,entryThree);
            statement.execute();
            Toast.makeText(this, "Record Added", Toast.LENGTH_LONG).show();

            longitude.setText("");
            latitude.setText("");
            tvAddress.setText("");
            longitude.requestFocus();


        } catch (Exception exception) {
            Toast.makeText(this, "Record Failed", Toast.LENGTH_LONG).show();
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