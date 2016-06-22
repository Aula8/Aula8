package unegdevelop.paintfragments.aula8.lobby;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import unegdevelop.paintfragments.R;

public class Fragmento extends Fragment {
    private RecyclerView reciclerView;
    private RecyclerView.LayoutManager lmanager;
    private RecyclerView.Adapter adaptador;
    ArrayList<Sala> datos;
    public JSONObject jsonObj;
    public JSONObject jsonObjUser;
    public JSONObject jsonObjSubjects;
    public JSONObject jsonObjSubjectsProfessors;

    public Fragmento() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = (View) inflater.inflate(R.layout.fragmento, container, false);

        reciclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        reciclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL, false));

        try {
            jsonObj = new JSONObject(getArguments().getString("json"));
            jsonObjUser = jsonObj.getJSONObject("user");
            jsonObjSubjects = jsonObj.getJSONObject("subject");
            jsonObjSubjectsProfessors = jsonObj.getJSONObject("professor");
            long x = jsonObj.getLong("subject");
            System.out.println(x);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        datos = new ArrayList<Sala>(); //creo una lista de objetos de tipo sala para adaptarla al fragmento

        for (int i=0;i<9;i++) // creo 9 objetos con la informacion aleatoria de los salones de clases
            datos.add(new Sala()); //el nuevo objeto creado lo agrego a la lista

        adaptador = new AdaptadorSala(datos);
        reciclerView.setAdapter(adaptador);

        return rootView;
    }
}
