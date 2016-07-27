package com.test.testhttprequests;

import android.os.AsyncTask;
import android.os.Bundle;
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

        String response = getResponse(url, headers);
        Log.i("MainActivityLogTag", "response: " + response);

        return response;
    }

    public static String getResponse(String url, Map<String, String> headers) {
        String result = null;
        HttpURLConnection urlConnection = null;
        try {
            URL request = new URL(url);
            urlConnection = (HttpURLConnection) request.openConnection();
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(20000);

            // GET is default value
            // urlConnection.setRequestMethod("GET");

            // trigger that request have body.
            // The same effect like setRequestMethod("POST")
            // but setRequestMethod("GET") will be ignored after setDoOutput(true)
            // urlConnection.setDoOutput(true);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    urlConnection.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }


            int status = urlConnection.getResponseCode();
            Log.i("HTTP util", "status code:" + status);

            if (status == HttpURLConnection.HTTP_OK) {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = getStringFromStream(in);
            }
        } catch (Exception e) {
            Log.e("HTTP util", "getResponse failed", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }

    public static String getStringFromStream(InputStream inputStream) {
        int numberBytesRead;
        StringBuilder out = new StringBuilder();
        byte[] bytes = new byte[4096];

        try {
            while ((numberBytesRead = inputStream.read(bytes)) != -1) {
                out.append(new String(bytes, 0, numberBytesRead));
            }
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

}
