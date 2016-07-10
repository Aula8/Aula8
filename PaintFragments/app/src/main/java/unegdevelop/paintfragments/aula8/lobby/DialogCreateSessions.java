package unegdevelop.paintfragments.aula8.lobby;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import unegdevelop.paintfragments.MainActivity;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.webServices;

/**
 * Created by wuilkysb on 07/07/16.
 */
public class DialogCreateSessions extends DialogFragment{
    private View dialogView;
    public String subjec;

    public DialogCreateSessions() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate( R.layout.sessions_create_new, null );
        dialog.setTitle("Crear Una Nueva Sala de Clase");
        dialog.setView(dialogView);

        FloatingActionButton fab = (FloatingActionButton) dialogView.findViewById(R.id.fab);
        final AutoCompleteTextView tv = (AutoCompleteTextView) dialogView.findViewById(R.id.new_session_txt);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webServices apiService = Servidor.createService(webServices.class);
                apiService.CreateSession(subjec, tv.getText().toString(),new Callback<JSONObject>() {
                    @Override
                    public void success(JSONObject tasks, Response response) {
                        // here you do stuff with returned tasks
                        Servidor.enviarEvento("room", tv.getText().toString());
                        Intent myIntent = new Intent( dialogView.getContext() , MainActivity.class);
                        dialogView.getContext().startActivity(myIntent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // you should handle errors, too
                    }
                });
            }
        });

        return dialog.create();
    }

}
