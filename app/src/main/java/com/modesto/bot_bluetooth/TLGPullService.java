package com.modesto.bot_bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.modesto.bot_bluetooth.http.Api.Datum;
import com.modesto.bot_bluetooth.http.Api.Sensor;
import com.modesto.bot_bluetooth.http.Api.SensorApi;
import com.modesto.bot_bluetooth.http.ReplyKeyboardMarkup.KeyboardButton;
import com.modesto.bot_bluetooth.http.ReplyKeyboardMarkup.ReplyKeyboardMarkup;
import com.modesto.bot_bluetooth.http.Stickers.Sticker;
import com.modesto.bot_bluetooth.http.Stickers.Stickers;
import com.modesto.bot_bluetooth.http.Stickers.Thumb;
import com.modesto.bot_bluetooth.http.canal.ChannelPost;
import com.modesto.bot_bluetooth.http.canal.ChatCanal;
import com.modesto.bot_bluetooth.http.canal.ResultCanal;
import com.modesto.bot_bluetooth.http.canal.TelegramCanalRequest;
import com.modesto.bot_bluetooth.http.Chat.From;
import com.modesto.bot_bluetooth.http.Chat.Message;
import com.modesto.bot_bluetooth.http.Chat.Result;
import com.modesto.bot_bluetooth.http.Chat.TelegramRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TLGPullService extends IntentService {

    //ARDUINO
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String for MAC address
    private static String address = null;

    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;

    //TELEGRAM
    public static final String BROADCAST_ACTION = "modesto.apps.constants.BROADCAST";
    String TOKEN = "1677424373:AAHYrG4NRu8ow57tHZ8jNjwuIx_i-XjoPGg";
    String URL = "https://api.telegram.org/bot"+TOKEN+"/";

    //Variables para el Chat unico
    TelegramRequest t = new TelegramRequest();
    Message mensaje =new Message();
    List<Result> resultado = new ArrayList<>();



    //Variables para el Canal con el Bot
    TelegramCanalRequest telegramCanal = new TelegramCanalRequest();
    ChannelPost postCanal = new ChannelPost();
    List<ResultCanal> resultCanal = new ArrayList<>();

    //Variable de la API Conexion
    Sensor sensor = new Sensor();


    String offset="0";

    public TLGPullService() {
        super("TLGPullService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        address = intent.getDataString();
        createComunicacion();
        Consulta();
        Intent localIntent = new Intent(BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private void createComunicacion(){
        //create device and set the MAC address
        Log.i("Modesto", "adress : " + address);
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
        Log.i("Modesto", "createComunicacion: "+mConnectedThread);
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    //METODOS TELEGRAM

    public void Consulta() {
        while (true) {
            String rest = "";

            rest = RevisarUpdateId(rest,offset);

            if (!rest.equals("") && !rest.contains("channel_post")) {
                Gson gson = new Gson();
                t = gson.fromJson(rest, TelegramRequest.class);
                resultado = t.getResult();
                String update_id="";
                    if(resultado != null){
                        VerificarMensajesChat(update_id);
                    }
            }else{/*
                if(rest.contains("channel_post")){
                    Gson gson = new Gson();
                    telegramCanal = gson.fromJson(rest, TelegramCanalRequest.class);
                    resultCanal = telegramCanal.getResultCanal();
                    String update_id="";

                    if(resultCanal != null) {
                        VerificarMensajesCanal(update_id);
                    }
                }*/

            }
        }
    }

    private void VerificarMensajesChat(String update_id) {
            for(Result result : resultado) {
                update_id=result.getUpdateId();
                mensaje = result.getMessage();
            }

        Log.d("MENSAJE", "VerificarMensajesChat: "+mensaje.getText());

            if(Integer.parseInt(update_id) > (Integer.parseInt(offset) - 1)) {
                offset = String.valueOf(Integer.parseInt(update_id) + 1);
            }

            From f = mensaje.getFrom();

        if(mensaje.getText().equals("¿Cuantas personas han entrado a mi casa hoy?")|| mensaje.getText().equals("6")){
            Datos_API("hoy");
        }else if(mensaje.getText().equals("¿Cuantas personas han entrado a mi casa esta semana?")|| mensaje.getText().equals("7")){
            Datos_API("semana");
        }else
        if(mensaje.getText().equals("¿Cuándo fue la última ves que alguien entró a mi casa?")|| mensaje.getText().equals("8")){
            Datos_API("fecha_ultima");
        }else
        if(mensaje.getText().equals("Gracias")|| mensaje.getText().equals("5")){
            MandarMessage("De nada "+f.getFirstName()+" estoy para servirte <3");
        }else
        if(mensaje.getText().equals("Adios") || mensaje.getText().equals("Adiós")|| mensaje.getText().equals("4")){
            MandarMessage("Hasta pronto "+f.getFirstName()+"!");
        }else
        if(mensaje.getText().equals("Hola") || mensaje.getText().equals("1")){
            MandarMessage("Hola "+f.getFirstName()+"!");
        } else
        if(mensaje.getText().equals("Opciones")){
            String mens = "Hola "+f.getFirstName()+"!\n "+
                    "Las opciones que tengo son:\n " +
                    "1. Hola,\n " +
                    "2. encender\n "+
                    "3. apagar\n "+
                    "4. Adios,\n " +
                    "5. Gracias,\n " +
                    "6. ¿Cuantas personas han entrado a mi casa hoy?,\n " +
                    "7. ¿Cuantas personas han entrado a mi casa esta semana?,\n " +
                    "8. ¿Cuándo fue la última ves que alguien entró a mi casa?,\n " +
                    "Trata de escribirlas tal cual si no, no podre entender lo que quieres :(";
            MandarMessage(mens);
        }else if(mensaje.getText().equals("Encender")  || mensaje.getText().equals("Enciende")|| mensaje.getText().equals("2")){
            if(mConnectedThread.write("1")){
                MandarMessage("Encendiendo comunicación Arduino");
            }else{
                MandarMessage("Ups! hubo un error al comunicarse con el modulo BT de arduino, verifique si selecciono el dispositivo correcto :(");
            }
        }
        else if(mensaje.getText().equals("Apagar")  || mensaje.getText().equals("Apaga")|| mensaje.getText().equals("3")){
            if(mConnectedThread.write("0")){
                MandarMessage("Apagando comunicacion Arduino");
            }else{
                MandarMessage("Ups! hubo un error al comunicarse con el modulo BT de arduino, verifique si selecciono el dispositivo correcto :(");
            }
        }else{

            MandarMessage("Ups!! creo que escribiste mal el mensaje ya que no entiendo lo que me pides, intentalo de nuevo :(");
        }

        resultado.clear();
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    /*private void VerificarMensajesCanal(String update_id){
        for (ResultCanal result : resultCanal) {
            update_id = result.getUpdateId();
            postCanal = result.getChannelPost();
        }

        if (Integer.parseInt(update_id) > (Integer.parseInt(offset) - 1)) {
            offset = String.valueOf(Integer.parseInt(update_id) + 1);
        }

        ChatCanal c = postCanal.getChatCanal();

        if(postCanal.getText().equals("¿Cuantas personas han entrado a mi casa?")){

            int valor = (int) (Math.random()*10);
            MandarMessageCanal("Hola "+c.getUsername()+" el numero de personas que ha entrado a tu casa es: "+ valor );
        }else
        if(postCanal.getText().equals("¿Cuantas veces he salido de casa?")){
            int valor = (int) (Math.random()*10);
            MandarMessageCanal("Hola "+c.getUsername()+" el numero de veces que has salido es: "+ valor );
        }else
        if(postCanal.getText().equals("¿Cuál fué la última ves que salí de casa?")){
            long rangebegin = Timestamp.valueOf("2021-03-10 00:00:00").getTime();
            long rangeend = Timestamp.valueOf("2021-04-21 00:58:00").getTime();
            long diff = rangeend - rangebegin + 1;
            Timestamp rand = new Timestamp(rangebegin + (long)(Math.random() * diff));
            MandarMessageCanal(String.format("Hola " + c.getUsername() + ", la última ves que saliste de casa fué: " + rand.toString() ) );
        }else
        if(postCanal.getText().equals("¿Cuándo fué la última ves que alguien entró a casa?")){
            long rangebegin = Timestamp.valueOf("2021-03-10 00:00:00").getTime();
            long rangeend = Timestamp.valueOf("2021-04-21 00:58:00").getTime();
            long diff = rangeend - rangebegin + 1;
            Timestamp rand = new Timestamp(rangebegin + (long)(Math.random() * diff));
            MandarMessageCanal("Hola "+c.getUsername()+" la última ves que alguien entró a casa fué: " + rand.toString() );
        }else
        if(postCanal.getText().equals("Gracias")){
            MandarMessageCanal("De nada "+c.getUsername()+" estoy para servirte <3");
        }else
        if(postCanal.getText().equals("Adios")){
            MandarMessageCanal("Hasta pronto "+c.getUsername()+"!");
        }else
        if(postCanal.getText().equals("Adiós")){
            MandarMessageCanal("Hasta pronto "+c.getUsername()+"!");
        }else
        if(postCanal.getText().equals("Hola")){
            MandarMessageCanal("Hola "+c.getUsername()+"!");
        } else
        if(postCanal.getText().equals("Opciones")){
            MandarMessageCanal("Hola "+c.getUsername()+"! \n" +
                    "Las opciones que tengo son: \n" +
                    "1. Hola,\n " +
                    "2. Adios,\n " +
                    "3. Gracias,\n " +
                    "4. ¿Cuantas veces he salido de casa? y\n " +
                    "5. ¿Cuantas personas han entrado a mi casa?,\n " +
                    "6. ¿Cuál fué la última ves que salí de casa?,\n " +
                    "7. ¿Cuándo fué la última ves que alguien entró a casa?,\n " +
                    "Trata de escribirlas tal cual si no, no podre entender lo que quieres :(");
        }else{
            MandarMessageCanal("Hola"+c.getUsername()+" has escrito: "+postCanal.getText());
        }

        resultCanal.clear();
    }*/
    ///////////////////////////////////////////////////////////////////////////////////////////

    private String RevisarUpdateId(String rest, String offset){
        try {
            URL url = new URL(URL + "getUpdates" + "?offset=" + offset + "&timeout=" + "100");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
                InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(reader);

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                rest = sb.toString();
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rest;
    }

    public String MandarMessage(String men) {
        String rest="";
        try {
            URL ur = new URL(URL+"sendMessage?chat_id="+mensaje.getChat().getId()+"&text="+men);
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
        return rest;
    }

    public String MandarMessageCanal(String men) {

        String rest="";
        try {
            URL ur = new URL("https://api.telegram.org/bot1677424373:AAHYrG4NRu8ow57tHZ8jNjwuIx_i-XjoPGg/sendMessage?chat_id=-1001478095574&text="+men);
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
            e.printStackTrace();
        }
        return rest;
    }

    public void Datos_API(String tiempo) {

        // Create a new object from HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Add Interceptor to HttpClient
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl("http://ec2-18-119-66-249.us-east-2.compute.amazonaws.com:10004/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SensorApi sensorApi = retrofit.create(SensorApi.class);
        Call<Sensor> call = sensorApi.getdata();
        call.enqueue(new Callback<Sensor>() {
            @Override
            public void onResponse(Call<Sensor> call, Response<Sensor> response) {
                try{
                    if(response.isSuccessful()){
                        sensor = response.body();
                        List<Datum> datum = sensor.getData();

                        if(tiempo.equals("hoy")){
                            int c =DatosFechaDia(datum);
                            new Envio_Mensaje().execute("Hola "+mensaje.getFrom().getFirstName()+"! el numero de usuarios que ha entrado hoy es de: "+c,mensaje.getChat().getId());
                        }

                        if(tiempo.equals("semana")){
                            int c =DatosFechaSemana(datum);
                            new Envio_Mensaje().execute("Hola "+mensaje.getFrom().getFirstName()+"! el numero de usuarios que ha entrado esta semana es de: "+c,mensaje.getChat().getId());
                        }

                        if(tiempo.equals("fecha_ultima")){
                            String fecha_ultima = UltimaFecha(datum);
                            new Envio_Mensaje().execute("Hola "+mensaje.getFrom().getFirstName()+"! La ultima vez que alguien entro a tu casa fue "+fecha_ultima,mensaje.getChat().getId());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Sensor> call, Throwable t) {
                new Envio_Mensaje().execute("Hola "+mensaje.getFrom().getFirstName()+"! al parecer hay un error en la conexion intentalo de nuevo mas tarde :(",mensaje.getChat().getId());
            }
        });

    }

    private int DatosFechaDia(List<Datum> datum) {
        int c=0;
        DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
        String date=df1.format(Calendar.getInstance().getTime());

        Log.d("FECHA", "DatosFechaDia: "+date);
        for(Datum dato : datum){
            if(dato.getCreatedAt().substring(0,10).equals(date)){
                c++;
            }
        }

        return c;
    }

    private int DatosFechaSemana(List<Datum> datum) {
        int c=0;
        DateFormat df1=new SimpleDateFormat("yyyy/MM/dd");
        String date=df1.format(Calendar.getInstance().getTime());

        //Sacamos los datos String
        String dayString = date.substring(8,10);
        String monthString = date.substring(5,7);
        String yearString = date.substring(0,4);

        //Los convertimos a enteros
        int day = Integer.parseInt(dayString)-7;
        int month = Integer.parseInt(monthString);
        int year = Integer.parseInt(yearString);

        //Verificamos los dias del mes si es que se acaba el mes
        if(day < 0){
            month-= 1;
            day = numeroDeDiasMes(month,year,day);
        }else if(day == 0){
            month-=1;
            day = numeroDeDiasMes(month,year,0);
        }

        for(Datum dato : datum){
            if(dato.getCreatedAt().substring(0,4).equals(yearString) &&
               dato.getCreatedAt().substring(5,7).equals(monthString) &&
               Integer.parseInt(dato.getCreatedAt().substring(8,10)) >= day){
                c++;
            }
        }

        return c;
    }

    private String UltimaFecha(List<Datum> datum) {
        String fecha = "el "+datum.get(datum.size()-1).getCreatedAt().substring(0,10)+" a las "+datum.get(datum.size()-1).getCreatedAt().substring(11,20);
        return fecha;
    }

    public static int numeroDeDiasMes(int mes, int anio, int negativo) {

        int numeroDias = -1;
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numeroDias = 31 + negativo;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numeroDias = 30+ negativo;
                break;
            case 2:

                if (esBisiesto(anio)) {
                    numeroDias = 29+ negativo;
                } else {
                    numeroDias = 28+ negativo;
                }
                break;

        }

        return numeroDias;
    }


    public static boolean esBisiesto(int anio) {

        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;

    }


}
