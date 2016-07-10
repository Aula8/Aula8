package unegdevelop.paintfragments;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URISyntaxException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Slaush on 22/05/2016.
 */

public class Servidor
{
    static public Application activity;

    final static int ESPERA_ENTRE_CONEXIONES = 500;
    final static int INTENTOS_CONEXION = 20;
    final static String PUERTO = "3000";

    static public String URL;
    static public Socket socket;
    static public String room;
    static public HttpRequest request;


    public static JSONObject jsonObj;
    public static JSONObject jsonObjUser;
    public static JSONArray jsonObjSubjects;
    public static JSONArray jsonObjSessions;
    public static JSONArray jsonObjSections;
    public static JSONArray jsonObjSubjectsProfessors;

    private static RestAdapter.Builder builder = null;


    private Servidor(){}

    public static String getURLServidor()
    {
        return URL;
    }

    public static JSONObject requestServerPost(String url, HashMap<String, String>... params) throws JSONException {
        JSONObject jsonObj;
        request = HttpRequest.post(URL + url);

        if(params.length > 0)
            for (Map.Entry<String, String> param : params[0].entrySet()) {
                request.part((String) param.getKey(), (String) param.getValue());
            }

        System.setProperty("http.keepAlive", "false");
        String body = request.body().toString();
        request.disconnect();
        jsonObj = new JSONObject(body);
        return  jsonObj;
    }

    public static String requestServer(String myurl) throws IOException {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(myurl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public static void setDataUser(JSONObject json){
        try {
            jsonObj = json;
            jsonObjUser = json.getJSONObject("user");
            jsonObjSubjects = json.getJSONArray("subject");
            jsonObjSections = json.getJSONArray("section");
            jsonObjSubjectsProfessors = json.getJSONArray("professor");
            jsonObjSessions = json.getJSONArray("session");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void  setDataSessionSubject(JSONObject j) throws JSONException {
        jsonObjSessions.put(j.getJSONArray("session"));
    }

    public static JSONObject getDatUser(){
        return jsonObj;
    }


    public static void anadirEventoRecibidoAlSocket(String nombreEvento, Emitter.Listener listener)
    {
        socket.on(nombreEvento, listener);
    }

    public static void enviarEvento(String nombreEvento, JSONObject objetoJSON)
    {
        socket.emit(nombreEvento,objetoJSON);
    }
    public static void enviarEvento(String nombreEvento, String dato)
    {
        socket.emit(nombreEvento, dato);
    }
    public static void enviarEvento(String nombreEvento)
    {
        socket.emit(nombreEvento);
    }

    public static void eliminarEvento(String nombreEvento)
    {
        socket.off(nombreEvento);
    }

    public static void start() {
        String prueba;
        String numero;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;

        for (int i = 0; i <= INTENTOS_CONEXION; i++)
        {
            try
            {

                if(i>10)
                    numero = "1"+i;
                else
                    numero = "10"+i;
                prueba = "http://192.168.1." + numero + ":" + PUERTO;
                socket = IO.socket(prueba, opts);
                socket.connect();
                anadirEventosSocket();
                esperar(ESPERA_ENTRE_CONEXIONES);
                if (socket.connected())
                {
                    URL = prueba;
                    builder = new RestAdapter.Builder()
                            .setEndpoint(URL)
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setClient(new OkClient(new OkHttpClient()));
                    break;
                }
            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }

        /*URL = "http://192.168.1.4:3000/";
        builder = new RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()));
        try {
            socket = IO.socket(URL, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();*/

    }

    public static InetAddress getBroadcastAddress() throws UnknownHostException {
        WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp == null) {
            throw new UnknownHostException();
        }

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++) {
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(quads);
    }


    private static void anadirEventosSocket()
    {

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener()
        {
            @Override
            public void call(Object... args)
            {

            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener()
        {
            @Override
            public void call(Object... args) {
            }
        });

    }

    private static void esperar(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {

        }
    }

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }

}
