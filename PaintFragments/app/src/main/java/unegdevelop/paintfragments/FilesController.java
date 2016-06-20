package unegdevelop.paintfragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.socket.emitter.Emitter;
import okhttp3.internal.Util;


/**
 * Created by Slaush on 16/06/2016.
 */

public class FilesController
{
    static public void uploadFile(File file, String direccionFile) throws JSONException
    {
        String fileString = Utils.encodeFile(file);
        JSONObject jObj = crearJSON(fileString,file.getName(),direccionFile);
        Servidor.enviarEvento("enviar archivo", jObj);
    }

    static public void donwloadFile(String direccionFile)
    {
        Servidor.anadirEventoRecibidoAlSocket("archivo enviado",downloadFileListener);
        Servidor.enviarEvento("bajar archivo", direccionFile);
    }

    static private JSONObject crearJSON(String data,String name, String direccion) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("data",data);
        obj.put("nombre",name);
        obj.put("direccion",direccion);
        return obj;
    }

    static Emitter.Listener downloadFileListener = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            JSONObject obj = (JSONObject) args[0];
            try
            {
                String archivo = obj.getString("archivo");
                String nombre  = obj.getString("nombre");
                Utils.decodeFile(nombre,archivo);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };



}
