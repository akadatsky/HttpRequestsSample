package com.test.testhttprequests;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.testhttprequests.model.WeatherResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText cityView;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityView = (EditText) findViewById(R.id.city);
        resultView = (TextView) findViewById(R.id.result);
    }

    public void btnGoClick(View view) {
        new MakeRequestTask().execute();
    }

    private WeatherResponse parseWeather(String weatherJson) {
        Gson gson = new GsonBuilder().create();
        WeatherResponse result = gson.fromJson(weatherJson, WeatherResponse.class);
        return result;
    }

    private String getWeatherResponse() {
        String city = cityView.getText().toString();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city;
        Map<String, String> headers = new HashMap<>();
        headers.put("x-api-key", "9f5afe53fe98733ba4d66e116f374ddd");
        String response = HttpUtil.getResponse(url, headers);
        Log.i("MainActivityLogTag", "response: " + response);
        return response;
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, WeatherResponse> {

        @Override
        protected void onPreExecute() {
            resultView.setText("Pending...");
        }

        @Override
        protected WeatherResponse doInBackground(Void... voids) {
            String response = getWeatherResponse();
            WeatherResponse result = parseWeather(response);
            return result;
        }

        @Override
        protected void onPostExecute(WeatherResponse weatherResponse) {
            resultView.setText(String.valueOf(weatherResponse));
        }
    }

}
