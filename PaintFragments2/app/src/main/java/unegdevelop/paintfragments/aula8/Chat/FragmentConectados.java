package unegdevelop.paintfragments.aula8.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import unegdevelop.paintfragments.R;

/**
 * Created by xavier on 24/06/16.
 */
public class FragmentConectados extends android.support.v4.app.Fragment
{
    private View rootView;
    private Activity activity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<AdaptadorConectados.AdaptadorViewHolder> adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragmento_conectados, container, false);
        //Se inicializan los usuarios
        List items = new ArrayList<>();

        items.add(new Usuario(R.drawable.foto1,"Isrrael Maita", "isrrael@correo.com"));
        items.add(new Usuario(R.drawable.foto1,"Xavier Moreno", "xavier@correo.com"));
        items.add(new Usuario(R.drawable.foto1,"Acilio Simoes", "acilio@correo.com"));

        /*recyclerView = (RecyclerView) rootView.findViewById(R.id.reciclador);
        recyclerView.setHasFixedSize(true);

        lManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(lManager);

        adapter = new AdaptadorConectados(items);
        recyclerView.setAdapter(adapter);*/


        return rootView;
    }
}
