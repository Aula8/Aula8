package unegdevelop.paintfragments;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import net.gotev.uploadservice.UploadService;

import unegdevelop.paintfragments.aula8.Chat.Contenedor;

public class MainActivity extends FragmentActivity implements Paint.OnFragmentInteractionListener, Chat.OnFragmentInteractionListener
{
    private Paint vPizarra;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UploadService.NAMESPACE = "com.uneg.aula8";

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
}
