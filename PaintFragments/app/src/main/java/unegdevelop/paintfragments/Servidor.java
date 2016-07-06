package unegdevelop.paintfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URISyntaxException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Slaush on 22/05/2016.
 */

public class Servidor
{

    final static int ESPERA_ENTRE_CONEXIONES = 1000;
    final static int INTENTOS_CONEXION = 20;
    final static String PUERTO = "3000";

    static public String URL;
    static public Socket socket;
    static public HttpRequest request;


    public static JSONObject jsonObj;
    public static JSONObject jsonObjUser;
    public static JSONArray jsonObjSubjects;
    public static JSONArray jsonObjSessions;
    public static JSONArray jsonObjSections;
    public static JSONArray jsonObjSubjectsProfessors;


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
        finally {
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                urlConnection.setRequestProperty("Connection", "close");
            }
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

    public static void start()
    {
        String prueba;
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;

        for (int i = 0; i <= INTENTOS_CONEXION; i++) {
            try {
                prueba = "http://192.168.1." + String.valueOf(i) + ":" + PUERTO + "/";
                socket = IO.socket(prueba, opts);
                socket.connect();
                anadirEventosSocket();
                esperar(ESPERA_ENTRE_CONEXIONES);
                if (socket.connected()) {
                    URL = prueba;
                    break;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

            /*URL = "http://192.168.1.3:" + PUERTO + "/";
                try {
                socket = IO.socket(URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
                socket.connect();*/

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

}
