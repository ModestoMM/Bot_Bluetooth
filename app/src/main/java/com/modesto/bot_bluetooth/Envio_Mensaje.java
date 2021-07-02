package com.modesto.bot_bluetooth;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class Envio_Mensaje extends AsyncTask<String, Void, Void> {

    String TOKEN = "1677424373:AAHYrG4NRu8ow57tHZ8jNjwuIx_i-XjoPGg";
    String URL = "https://api.telegram.org/bot"+TOKEN+"/";

    @Override
    protected Void doInBackground(String... men) {
        Log.d("MENSAJE", "doInBackground: "+men[1]);
        Log.d("DATA2", "onResponse: "+men[0]);
        String rest="";
        try {
            URL ur = new URL(URL+"sendMessage?chat_id="+men[1]+"&text="+men[0]);
            HttpURLConnection conn = (HttpURLConnection) ur.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-length", "0");
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.connect();

            int status = conn.getResponseCode();

            if (status == 200) {
                rest = "Message Send as BOT";
            }

            conn.disconnect();


        }catch (Exception e){
            Log.d("ERROR", "MandarMessage: "+e.getCause());
            e.printStackTrace();
        }
        return null;
    }
}
