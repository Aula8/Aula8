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

import unegdevelop.paintfragments.R;

public class MainActivityLobby extends AppCompatActivity {
    private Button bAsignar;
    private Bundle b;
    public JSONObject jsonObj;

    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //orientacion de la pantalla bloqueada solo horizontal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        bAsignar = (Button)findViewById(R.id.bAsignar);

        b = getIntent().getExtras();
        try {
            jsonObj = new JSONObject(b.getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){ //Asignacion del fragment mediante codigo a la layout principal
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragmento fragment = new Fragmento();
                fragment.setArguments(b);
                transaction.add(R.id.layout_principal, fragment).commit();
            }
        });

    }
}
