package com.example.addressfinder;
import android.content.Context;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GeoLocation {
    public void getAddress(double lon, double lat, final Context context, Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List myList = geocoder.getFromLocation(lat, lon, 1);
                    Address address = (Address) myList.get(0);
                    String addressStr = "";
                    addressStr += address.getAddressLine(0);
                    result = addressStr;

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        message.setData(bundle);
                    }
                    message.sendToTarget();
                }


            }
        };
        thread.start();
    }
}

