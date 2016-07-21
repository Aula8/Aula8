package unegdevelop.paintfragments.aula8.lobby;

import android.content.pm.ActivityInfo;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Utils;

public class MainActivityLobby extends AppCompatActivity 
{
    private Button bAsignar;
    private Bundle b;

    protected void onCreate(Bundle savedInstanceState) 
    {
        //orientacion de la pantalla bloqueada solo horizontal
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        FragmentManager     fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragmento           fragment = new Fragmento();
        transaction.add(R.id.layout_principal, fragment).commit();
    }
}
