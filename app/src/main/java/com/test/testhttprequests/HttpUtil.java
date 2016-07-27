package com.test.testhttprequests;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

    public static String getResponse(@NonNull String url, @Nullable Map<String, String> headers) {
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
