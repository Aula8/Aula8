package unegdevelop.paintfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.util.Base64;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

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
    final static String PUERTO = "1234";

    static public String URL;
    static Socket socket;

    public static String getURLServidor()
    {
        return URL;
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
        String numero = "100";
        String prueba = "http://192.168.1."+numero+":1234";
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;

        for (int i=0;i<=INTENTOS_CONEXION ; i++)
        {

            if(i>10)
                numero = "1"+i;
            else
                numero = "10"+i;

            prueba = "http://192.168.1."+numero+":"+PUERTO;

            try
            {
                socket = IO.socket(prueba, opts);
                socket.connect();
                anadirEventosSocket();
                esperar(ESPERA_ENTRE_CONEXIONES);
                if(socket.connected())
                {
                    URL = prueba;
                    break;
                }
            }
            catch (URISyntaxException e)
            {

            }

        }
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
