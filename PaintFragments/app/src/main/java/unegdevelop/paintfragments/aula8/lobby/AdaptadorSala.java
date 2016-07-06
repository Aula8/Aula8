package unegdevelop.paintfragments.aula8.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.MainActivity;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.Sessions;
import unegdevelop.paintfragments.webServices;


/**
 * Created by jarg95 on 09/06/16.
 */
public class AdaptadorSala extends RecyclerView.Adapter<AdaptadorSala.ViewHolderSala> {

    private ArrayList<Sala> lista;
    private View dialogView;

    public AdaptadorSala(ArrayList<Sala> lista) {
        this.lista = lista;
    }

    public ViewHolderSala onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sala, parent, false);
        ViewHolderSala sala = new ViewHolderSala(v);
        return sala;
    }

    public void onBindViewHolder(final ViewHolderSala holder, final int position) {
        //Rellenando las CardView
        final int uc = lista.get(position).getUsuariosConectados();
        final int um = lista.get(position).getUsuariosMaximos();
        holder.tema.setText(lista.get(position).getTema());
        holder.materia.setText(lista.get(position).getMateria());
        holder.seccion.setText("" + lista.get(position).getSeccion());
        holder.owner.setText(lista.get(position).getOwner());
        holder.usuariosConectados.setText("" + uc);
        holder.usuariosMaximos.setText("" + um);

        //Cambia color a TextView usuariosConectados en funcion de la relacion usuariosConectados - capacidadMaxima
        if ((uc * 100 / um) == 100)
            holder.usuariosConectados.setTextColor(Color.parseColor("#FE2E2E"));
        else if ((uc * 100 / um) > 85)
            holder.usuariosConectados.setTextColor(Color.parseColor("#FE9A2E"));
        else if ((uc * 100 / um) > 70)
            holder.usuariosConectados.setTextColor(Color.parseColor("#C8FE2E"));
        else if ((uc * 100 / um) > 55)
            holder.usuariosConectados.setTextColor(Color.parseColor("#BFFF00"));
        else holder.usuariosConectados.setTextColor(Color.parseColor("#00FF00"));

        //metodo OnClick en el TextView "tema"
        holder.tema.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "(Click en Tema) Sala ID: " + lista.get(position).getCodigo(), Toast.LENGTH_SHORT).show();
            }
        });

        //metodo OnLongClick en el TextView "tema"
        holder.owner.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "(LongClick en owner) El profesor de " + lista.get(position).getMateria() + " es " + lista.get(position).getOwner(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //metodo OnLongClick para la CardView COMPLETA (incluye todos los view dentro de la misma)
        holder.tarjeta.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                //new getSessions(holder, v, v.getContext()).execute((Void) null);

                RestAdapter retrofit = new RestAdapter.Builder()
                        .setEndpoint(Servidor.URL)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setClient(new OkClient(new OkHttpClient()))
                        .build();
                webServices apiService = retrofit.create(webServices.class);
                apiService.getSessions(holder.materia.getText().toString() ,new Callback<List<Sessions>>() {
                    @Override
                    public void success(List<Sessions> session, Response response) {
                        String[] elementos;
                        ArrayList<String> array = new ArrayList<String>();

                        for(int i=0; i<session.size(); i++){
                            array.add(session.get(i).getTheme());
                        }

                        elementos = array.toArray(new String[array.size()]);

                        AlertDialog.Builder sub = new AlertDialog.Builder(holder.tarjeta.getContext());
                        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        dialogView = inflater.inflate( R.layout.subject_dialog, null );

                        TextView subject = (TextView) dialogView.findViewById(R.id.materia);
                        subject.setText(holder.materia.getText());
                        TextView theme = (TextView) dialogView.findViewById(R.id.tema);
                        theme.setText(holder.tema.getText());

                        ListView sessions = (ListView) dialogView.findViewById(R.id.sessions);
                        ArrayAdapter<String> stringArray = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_expandable_list_item_1, elementos);
                        sessions.setAdapter(stringArray);

                        sub.setView(dialogView);
                        AlertDialog alertDialog = sub.create();
                        alertDialog.show();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(v.getContext(), error.toString() ,Toast.LENGTH_LONG).show();
                    }
                });


                //Toast.makeText(v.getContext(), "(Click en CardView) Usuarios conectados: " + uc + "/" + um, Toast.LENGTH_SHORT).show();
            }
        });

        holder.botonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "has entrado en la sala: " + lista.get(position).getMateria(), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                ((Activity) v.getContext()).startActivity(myIntent);
            }
        });

    }


    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolderSala extends RecyclerView.ViewHolder {
        CardView tarjeta;
        TextView tema, materia, seccion, owner, usuariosConectados, usuariosMaximos;
        Button botonEntrar;


        public ViewHolderSala(final View itemView) {
            super(itemView);
            tarjeta = (CardView) itemView.findViewById(R.id.tarjeta);
            tema = (TextView) itemView.findViewById(R.id.tema);
            materia = (TextView) itemView.findViewById(R.id.materia);
            seccion = (TextView) itemView.findViewById(R.id.seccion);
            owner = (TextView) itemView.findViewById(R.id.owner);
            usuariosConectados = (TextView) itemView.findViewById(R.id.usuariosConectados);
            usuariosMaximos = (TextView) itemView.findViewById(R.id.usuariosMaximos);
            botonEntrar = (Button) itemView.findViewById(R.id.botonEntrar);


            /*
            //metodo OnClick para la CardView COMPLETA (incluye todos los view dentro de la misma)
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "(Click en CardView) : " + materia.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    //Toast.makeText(v.getContext(), "LongClick en CardView: " +  materia.getText().toString(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            */

        }
    }

}
