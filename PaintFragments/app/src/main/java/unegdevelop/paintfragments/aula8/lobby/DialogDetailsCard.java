package unegdevelop.paintfragments.aula8.lobby;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import unegdevelop.paintfragments.MainActivity;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.Sessions;
import unegdevelop.paintfragments.SessionsAdapter;
import unegdevelop.paintfragments.webServices;

/**
 * Created by wuilkysb on 07/07/16.
 */
public class DialogDetailsCard extends DialogFragment {
    private View dialogView;
    AdaptadorSala.ViewHolderSala holder;
    public View myV;
    public List<Sessions> session;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /*AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate( R.layout.sessions_create_new, null );
        dialog.setTitle("Crear Una Nueva Sala de Clase");
        dialog.setView(dialogView);*/

        final AlertDialog.Builder sub = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate( R.layout.subject_dialog, null );

        TextView subject = (TextView) dialogView.findViewById(R.id.materia);
        subject.setText(holder.materia.getText());
        TextView theme = (TextView) dialogView.findViewById(R.id.tema);
        theme.setText(holder.tema.getText());

        final ListView sessions = (ListView) dialogView.findViewById(R.id.sessions);
        SessionsAdapter stringArray = new SessionsAdapter(getActivity(), R.layout.listview_sessions, session);
        sessions.setAdapter(stringArray);
        sessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv = (TextView) view.findViewById(R.id.session_tv);
                Toast.makeText(getActivity(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                Servidor.enviarEvento("room", tv.getText().toString());
                Intent myIntent = new Intent( dialogView.getContext() , MainActivity.class);
                dialogView.getContext().startActivity(myIntent);

            }
        });

        sub.setView(dialogView);
        //AlertDialog alertDialog = sub.create();
        //alertDialog.show();

        FloatingActionButton fab = (FloatingActionButton) dialogView.findViewById(R.id.new_sessions);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = (FragmentActivity)(dialogView.getContext());
                FragmentManager fm = activity.getSupportFragmentManager();
                DialogCreateSessions newSubject = new DialogCreateSessions();
                newSubject.subjec = holder.materia.getText().toString();
                newSubject.show(fm, "display_service");
            }
        });

        return sub.create();
    }

}
