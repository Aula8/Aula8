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

/**
 * Created by Slaush on 11/07/2016.
 */

public class DownloadManager extends AsyncTask<String, Integer,String>
{


    private HttpURLConnection conn;
    private InputStream stream; //to read
    private ByteArrayOutputStream out; //to write
    private Context mCtx;

    private double fileSize;
    private double downloaded; // number of bytes downloaded
    private int status = DOWNLOADING; //status of current process


    private static final int MAX_BUFFER_SIZE = 1024; //1kb
    private static final int DOWNLOADING = 0;
    private static final int COMPLETE = 1;

    public DownloadManager(Context ctx)
    {

        conn       = null;
        fileSize   = 0;
        downloaded = 0;
        status     = DOWNLOADING;
        mCtx       = ctx;
    }

    public boolean isOnline()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Override
    protected String doInBackground(String... url)
    {
        int count;
        try {
            System.out.println("Downloading");
            URL dirDownload = new URL(url[0]);
            String filename = url[1];
            URLConnection conection = dirDownload.openConnection();
            conection.connect();
            // getting file length
            int lenghtOfFile = conection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(dirDownload.openStream(), 8192);

            // Output stream to write file

            OutputStream output = new FileOutputStream(filename);
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1)
            {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
           return "";
        }

        //Toast.makeText(mCtx, "Se Descargo: "+url[1], Toast.LENGTH_LONG).show();
        return url[1];
    } // end of class DownloadManager()

    @Override
    protected void onProgressUpdate(Integer... changed)
    {
        //Do Nothing?
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