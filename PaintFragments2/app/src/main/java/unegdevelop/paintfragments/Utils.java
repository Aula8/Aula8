package unegdevelop.paintfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Slaush on 16/06/2016.
 */

public class Utils
{
    public  static String folderName = "Aula8";
    public  static String encodeFile(File file)
    {
        try
        {
            byte[] bytes;
            bytes = loadFile(file);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        catch (IOException e)
        {
           return "";
        }

    }

    private static byte[] loadFile(File file) throws IOException
    {
        InputStream is = new FileInputStream(file);

        long length = file.length();

        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length)
        {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    public static void decodeFile(String nombre, String data) throws IOException
    {
        createA8Folder();

        byte[] b = Base64.decode(data,Base64.DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory(),
                        File.separator+folderName+File.separator+nombre);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(b);
        fos.close();

    }

    public static void createA8Folder()
    {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + folderName);
        if (!folder.exists())
        {
            folder.mkdir();
        }
    }

    public static String getA8Folder()
    {
        createA8Folder();
        return Environment.getExternalStorageDirectory() +
                File.separator + folderName + File.separator;
    }

    public static String getFileName(String direccionFile)
    {
        return direccionFile.substring(direccionFile.lastIndexOf("/")+1);
    }

    public static String reemplazarSlash(String dir)
    {
        String retorno = "";
        for (int i = 0; i< dir.length(); i++)
        {
            if(dir.charAt(i) != '\\')
                retorno += dir.charAt(i);
            else
                retorno += "/";
        }
        return retorno;

    }

}
