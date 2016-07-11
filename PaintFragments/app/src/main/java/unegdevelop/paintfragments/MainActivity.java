package unegdevelop.paintfragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.gotev.uploadservice.UploadService;

import unegdevelop.paintfragments.aula8.Chat.Contenedor;

public class MainActivity extends FragmentActivity implements Paint.OnFragmentInteractionListener, chat.OnFragmentInteractionListener 
{

    Paint Vent_Pizarra;

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

       /* FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Paint Vent_Pizarra = new Paint();
        fragmentTransaction.add(R.id.fragment_container, Vent_Pizarra);
        fragmentTransaction.commit();*/

        if (findViewById(R.id.fragment_container1) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Crear un nuevo fragmento para ser colocado en el activity layout
            Vent_Pizarra = new Paint();

            // Añadir el fragmento  'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container1, Vent_Pizarra).commit();

             Contenedor Vent_Chat = new Contenedor();

            //  Añadir el fragmento 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container2, Vent_Chat).commit();

        }

    }

    public void onFragmentInteraction(Uri uri){
        //se puede dejar vacio
    }

    public void paintClicked(View view){
        //use chosen color
        //Vent_Pizarra.paintClicked(view);
    }
}
