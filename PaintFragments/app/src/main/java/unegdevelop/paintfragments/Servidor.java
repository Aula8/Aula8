package unegdevelop.paintfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        HttpRequest request = HttpRequest.post(URL + url);

        if(params.length > 0)
            for (Map.Entry<String, String> param : params[0].entrySet()) {
                request.part((String) param.getKey(), (String) param.getValue());
            }

        String body = request.body().toString();
        jsonObj = new JSONObject(body);
        return  jsonObj;
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

    public static void  setDataSessionSubject(String j) throws JSONException {
        jsonObjSessions = new JSONObject(j).getJSONArray("session");
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
        /*String numero = "100";
        String prueba = "http://192.168.1."+numero+":1234";
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;*/
        String prueba;

        //for (int i = 0; i <= INTENTOS_CONEXION; i++) {
                URL = "http://192.168.1.2:" + PUERTO + "/";
        try {
            socket = IO.socket(URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

            /*try
            {
                socket = IO.socket(prueba, opts);
                socket.connect();
                anadirEventosSocket();
                esperar(ESPERA_ENTRE_CONEXIONES);
                if(socket.connected())
                {
                    URL = prueba;
                }
            }*/
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
