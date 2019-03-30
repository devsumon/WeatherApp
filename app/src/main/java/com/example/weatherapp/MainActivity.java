package com.example.weatherapp;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new asyncTask().execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String fetchDataFromUrl(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return response.body().string();
        }
    }
    class asyncTask extends AsyncTask {


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                data = fetchDataFromUrl("https://api.openweathermap.org/data/2.5/forecast/daily?APPID=c13159d2d9b7d01343afbc8acde7572b&q=Dhaka,BD&mode=json&units=metric&cnt=7&fbclid=IwAR0VwzDVFGqDENTlKE40XPIuIdsB3xahIHWMvPbe1EimfpZRWZSEaYOxXbw");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            // Telling Gson to form data from "data" like "MyContacts.class" format
            Days myDays = new Gson().fromJson(data, Days.class);
            recyclerView.setAdapter(new MyAdapter(MainActivity.this, myDays.getList()));
        }
    }
}
