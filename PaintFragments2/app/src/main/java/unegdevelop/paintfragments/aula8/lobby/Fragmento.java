package unegdevelop.paintfragments.aula8.lobby;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;

public class Fragmento extends Fragment
{
    private RecyclerView               reciclerView;
    private RecyclerView.LayoutManager lmanager;
    private RecyclerView.Adapter       adaptador;
    ArrayList<Sala>                    datos;

    public Fragmento() {}

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = (View) inflater.inflate(R.layout.fragmento, container, false);

        reciclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        reciclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false));

        datos = new ArrayList<Sala>(); //crea una lista de objetos de tipo Sala para adaptarla al fragmento
        try 
        {
            for (int i=0; i<Servidor.jsonObjSubjects.length(); i++)
            {
                datos.add(new Sala(Servidor.jsonObjSubjects.getJSONObject(i).optInt("_id", 0),
                                   Servidor.jsonObjSections.getJSONObject(i).optInt("number",0), 34, 40,
                                   Servidor.jsonObjSubjectsProfessors.getJSONObject(i).getString("name"),
                                   Servidor.jsonObjSubjects.getJSONObject(i).getString("name"),
                                   Servidor.jsonObjSubjects.getJSONObject(i).getString("description")
                        )
                );

                System.out.println(Servidor.jsonObjUser);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adaptador = new AdaptadorSala(datos);
        reciclerView.setAdapter(adaptador);

        return rootView;
    }
}
