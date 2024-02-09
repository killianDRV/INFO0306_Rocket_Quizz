package com.example.projetquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * La class contenant les informations sur le créateur.
 */
public class CreateurActivity extends AppCompatActivity {

    /**
     * The Network receiver.
     */
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createur);
        networkReceiver = new NetworkReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intent = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }

    /**
     * getNetworkName.
     * Récupère le no du newtork.
     */
    private String getNetworkName() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) return networkInfo.getTypeName();

        return "";
    }

    /**
     * Load network.
     * Pour charger le network (le site).
     *
     * @param v View
     */
    public void loadNetwork(View v) {
        if (getNetworkName() != "") {
            new DownloadWebpageTask().execute("https://cours.univ-reims.fr/login/index.php");
        }
    }

    private String downloadUrl(String myurl) throws IOException {

        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                is = conn.getInputStream();

                int response = conn.getResponseCode();
                Log.d("HTTP response", "The response is: " + response);

                // Convert the InputStream into a string
                String contentAsString = readIt(is);
                String contentAsString2 = readIt(is, len);
                return contentAsString;
            }

            conn.disconnect();

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    /**
     * Read it string.
     *
     * @param stream the stream
     * @return the string
     * @throws IOException the io exception
     */
    public String readIt(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    /**
     * Read it string.
     *
     * @param stream the stream
     * @param len    the len
     * @return the string
     * @throws IOException                  the io exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
// Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException,
            UnsupportedEncodingException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "URL invalide !!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            WebView webview = (WebView) findViewById(R.id.webview);
            webview.loadDataWithBaseURL(null, result, "text/html", "UTF-8",
                    null);
        }
    }
}