package unegdevelop.paintfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import net.gotev.uploadservice.UploadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;

import io.socket.emitter.Emitter;
import unegdevelop.paintfragments.aula8.Chat.Contenedor;

public class MainActivity extends FragmentActivity implements Paint.OnFragmentInteractionListener, Chat.OnFragmentInteractionListener
{
    private Paint vPizarra;
    private boolean keyCod = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UploadService.NAMESPACE = "com.uneg.aula8";

        Servidor.anadirEventoRecibidoAlSocket("closedSession", closedSession);

        try {
            AudioStream.startReceiver();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        if (findViewById(R.id.fragment_container1) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Crear un nuevo fragmento para ser colocado en el activity layout
            vPizarra = new Paint();

            // Añadir el fragmento  'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container1, vPizarra).commit();

             Contenedor vChat = new Contenedor();

            //  Añadir el fragmento 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container2, vChat).commit();

        }

    }

    public void onFragmentInteraction(Uri uri){
    }

    public void paintClicked(View view){
    }

    @Override
    public void onBackPressed() {
        if(Servidor.haveAccess()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir De La Sesión")
                    .setMessage("Está seguro que desea salir? \nLa Sesión se cerrará y se les notificará a los usuarios conectados.")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JSONObject data = new JSONObject();
                            try {
                                data.put("session", Servidor.room);
                                data.put("subject", Servidor.getActualSubject());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Servidor.socket.emit("closeSession", data);
                            finish();
                        }

                    })
                    .setNegativeButton("Permanecer", null)
                    .show();
        }else{
            finish();
        }
    }

    Emitter.Listener closedSession = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            /*new AlertDialog.Builder(mycontext)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir De La Sesión")
                    .setMessage("La Sesion Ha Finalizado. Desea Salir o Permanecer en el AULA?")
                    .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("Permanecer", null)
                    .show();
            Toast.makeText(mycontext, "La Sesión Ha Termina", Toast.LENGTH_LONG).show();*/

        }
    };
}
