package unegdevelop.paintfragments.aula8.lobby;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.internal.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.emitter.Emitter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import unegdevelop.paintfragments.MainActivity;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.Sessions;
import unegdevelop.paintfragments.SessionsAdapter;
import unegdevelop.paintfragments.Utils;
import unegdevelop.paintfragments.aula8.StudyMaterial.AdaptadorMaterial;
import unegdevelop.paintfragments.aula8.StudyMaterial.DialogFileDownload;
import unegdevelop.paintfragments.aula8.StudyMaterial.Material;
import unegdevelop.paintfragments.webServices;

/**
 * Created by wuilkysb on 07/07/16.
 */
public class DialogDetailsCard extends DialogFragment
{
    private View dialogView;
    public  View myV;
    public  List<Sessions> session;
    public List material;

    final private String GET_FILES_SUBJECT = "getFilesSubject";

    AdaptadorSala.ViewHolderSala holder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final AlertDialog.Builder sub = new AlertDialog.Builder(getActivity());

        Servidor.anadirEventoRecibidoAlSocket(GET_FILES_SUBJECT, filesSubject);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        dialogView = inflater.inflate( R.layout.subject_dialog, null );

        TextView subject = (TextView) dialogView.findViewById(R.id.materia);
        subject.setText(holder.materia.getText());

        TextView theme = (TextView) dialogView.findViewById(R.id.tema);
        theme.setText(holder.tema.getText());

        final ListView sessions = (ListView) dialogView.findViewById(R.id.sessions);
        SessionsAdapter stringArray = new SessionsAdapter(getActivity(),
                R.layout.listview_sessions,
                session);
        sessions.setAdapter(stringArray);
        sessions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                TextView tv = (TextView) view.findViewById(R.id.session_tv);
                Servidor.enviarEvento("room", tv.getText().toString());
                Servidor.room = tv.getText().toString();
                Servidor.setActualSubject(holder.materia.getText().toString());
                Servidor.setActualSection(holder.seccion.getText().toString());
                Intent myIntent = new Intent( dialogView.getContext() ,
                        MainActivity.class);
                dialogView.getContext().startActivity(myIntent);

            }
        });

        sub.setView(dialogView);


        FloatingActionButton fab = (FloatingActionButton) dialogView
                .findViewById(R.id.new_sessions);
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

        FloatingActionButton download = (FloatingActionButton) dialogView
                .findViewById(R.id.dowloadFiles);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = new JSONObject();
                try {
                    data.put("subject", holder.materia.getText().toString());
                    data.put("section", holder.seccion.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Servidor.enviarEvento(GET_FILES_SUBJECT, data);
            }
        });

        if (!Servidor.haveAccess())
            fab.setVisibility(View.GONE);

        return sub.create();
    }


    public Emitter.Listener filesSubject = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final JSONObject data = (JSONObject) args[0];
            try {
                JSONArray dataResult = data.getJSONArray("result");
                String dataLink = data.get("link").toString();
                FragmentActivity activity = (FragmentActivity)(dialogView.getContext());
                FragmentManager fm = activity.getSupportFragmentManager();
                DialogFileDownload newSubject = new DialogFileDownload();
                material = new ArrayList<>();

                for(int i=0; i < dataResult.length(); i++){
                    material.add(Utils.getFileName(dataResult.get(i).toString()));
                }

                newSubject.material = material;
                newSubject.link = dataLink;


                newSubject.show(fm, "display_service");
            } catch (JSONException e) {
                System.out.println("[---->>] No se encontraron archivos " );
            }
            //Toast.makeText(dialogView.getContext(), "Nombre Archivos", Toast.LENGTH_SHORT).show();
        }
    };

}
