package com.sherif.germanmem;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchData extends AsyncTask<Void,Void,Void> {
    String words="";

    @Override
    protected Void doInBackground(Void... Voids) {

        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL("https://api.predic8.de/shop/products/140");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
             httpURLConnection = (HttpURLConnection) url.openConnection();
             InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

             words=br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    //MainActivity.fullText.setText(words);
    }
}
