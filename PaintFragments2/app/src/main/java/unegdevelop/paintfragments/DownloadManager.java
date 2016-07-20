package unegdevelop.paintfragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLOutput;


public class DownloadManager extends AsyncTask<String, Integer,String>
{

    private Context          mCtx;
    private static final int BYTES_POR_CHUNK = 8192;
    private static final int MAX_BYTE_TO_READ = 1024;
    public DownloadManager(Context ctx)
    {
        mCtx       = ctx;
    }

    @Override
    protected String doInBackground(String... url)
    {
        int count;
        try
        {
            System.out.println("Downloading");
            URL dirDownload = new URL(url[0]);
            String filename = url[1];
            URLConnection connection = dirDownload.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(dirDownload.openStream(), BYTES_POR_CHUNK);

            OutputStream output = new FileOutputStream(filename);
            byte data[] = new byte[MAX_BYTE_TO_READ];

            long total = 0;

            while ((count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
           return "";
        }

        return url[1];
    } 

    @Override
    protected void onProgressUpdate(Integer... changed)
    {

    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected void onPostExecute(String result)
    {
        System.out.println("STRING RESULTANTE:"+ result);
        FilesController.setDownloadState(true);
    }
}