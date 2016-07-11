package unegdevelop.paintfragments;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


import com.golshadi.majid.core.DownloadManagerPro;
import com.golshadi.majid.core.enums.TaskStates;
import com.golshadi.majid.database.elements.Task;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadRequest;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import io.socket.emitter.Emitter;
import okhttp3.internal.Util;


/**
 * Created by Slaush on 16/06/2016.
 */

public class FilesController
{

    private static DownloadManagerPro downloader;

    static public  void uploadFile(File file, String direccionFile, Context context, boolean replicar)
            throws JSONException, FileNotFoundException, MalformedURLException
    {
        String uploadID = UUID.randomUUID().toString();
        BinaryUploadRequest bUPR = new BinaryUploadRequest(context, uploadID, Servidor.getURLServidor()+"/upload");

        if(replicar)
        {
            bUPR.addHeader("room",Servidor.room);
        }

        bUPR.addHeader("file-name", file.getName().trim()).addHeader("file-folder",direccionFile)
                .setFileToUpload(file.getAbsolutePath())
                .setNotificationConfig(getNotificationConfig(file.getName()))
                .setMaxRetries(2)
                .startUpload();
    }

    static int  donwloadFile(String direccionFile, Context context) throws IOException
    {
        downloader = new DownloadManagerPro(context);
        // 16 -- cantidad de Chunks maximo ..
        downloader.init(Utils.getA8Folder(),16,null);
        System.out.println(Servidor.getURLServidor()+"/"+direccionFile);

        System.out.println(direccionFile.substring(direccionFile.lastIndexOf("/")+1));

        int token = downloader.addTask(Utils.getFileName(direccionFile),
                                       Servidor.getURLServidor()+"/"+direccionFile, 16,
                                       Utils.getA8Folder(), true,false);
        downloader.startDownload(token);
        return token;

    }

    static boolean getDownloadState(int token) throws JSONException
    {
        return downloader.singleDownloadStatus(token).toJsonObject().getInt("state") == TaskStates.DOWNLOAD_FINISHED;
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
                .setClearOnAction(true);
    }


}
