package unegdevelop.paintfragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;




import net.gotev.uploadservice.BinaryUploadRequest;

import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;


/**
 * Created by Slaush on 16/06/2016.
 */

public class FilesController
{

    private static boolean downloadState;

    static public  void uploadFile(File file, String direccionFile, Context context, boolean replicar)
            throws JSONException, FileNotFoundException, MalformedURLException
    {
        String uploadID = UUID.randomUUID().toString();
        BinaryUploadRequest bUPR = new BinaryUploadRequest(context, uploadID, Servidor.getURLServidor()+"/upload");

        if(replicar)
        {
            bUPR.addHeader("room",Servidor.room);
        }

        bUPR.addHeader("file-name", file.getName().replaceAll("\\s","")).addHeader("file-folder",direccionFile)
                .setFileToUpload(file.getAbsolutePath())
                .setNotificationConfig(getNotificationConfig(file.getName()))
                .setMaxRetries(2)
                .startUpload();
    }

    static public  void uploadFile(File file, String direccionFile,String Subject, String Section, Context context)
            throws JSONException, FileNotFoundException, MalformedURLException
    {
        String uploadID = UUID.randomUUID().toString();
        BinaryUploadRequest bUPR = new BinaryUploadRequest(context, uploadID, Servidor.getURLServidor()+"/uploadMaterial");


        bUPR.addHeader("file-name", file.getName().replaceAll("\\s","")).addHeader("file-folder",direccionFile)
                .addHeader("subject", Subject).addHeader("section", Section).addHeader("session", Servidor.room)
                .setFileToUpload(file.getAbsolutePath())
                .setNotificationConfig(getNotificationConfig(file.getName()))
                .setMaxRetries(2)
                .startUpload();
    }

    static public void donwloadFile(String direccionFile, Context context) throws IOException
    {
        String url = Servidor.getURLServidor()+"/"+direccionFile;
        String dir = Utils.getA8Folder()+Utils.getFileName(direccionFile);
        downloadState = false;
        System.out.println("URL:"+url);
        new DownloadManager(context).execute(url, dir);
    }

    static public boolean getDownloadState()
    {
        return downloadState;
    }

    static void setDownloadState(boolean dState)
    {
        downloadState = dState;
    }

    private static UploadNotificationConfig getNotificationConfig(String title)
    {

        return new UploadNotificationConfig()
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Aula 8")
                .setInProgressMessage("Subiendo:"+title)
                .setCompletedMessage("Se Subio:"+title)
                .setErrorMessage("Hubo un Error")
                .setAutoClearOnSuccess(false)
                .setClearOnAction(true)
                .setRingToneEnabled(false);
    }


}
