package unegdevelop.paintfragments.aula8.Chat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import unegdevelop.paintfragments.R;

/**
 * Created by xavier on 12/07/16.
 */
public class AdaptadorPreguntas extends RecyclerView.Adapter<AdaptadorPreguntas.AdaptadorViewHolder> {

    private List<Usuario> items;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView.Adapter<AdaptadorRespuesta.AdaptadorViewHolder> adapter;
    private List respuestas = new ArrayList<>();


    public static class AdaptadorViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenUsuario;
        private TextView nombreUsuario;
        private TextView preguntaUsuario;
        //private Button botonRespuesta;
        private Button botonExpandir;
        private FragmentManager fm;
        private Context context;

        public AdaptadorViewHolder(View v) {
            super (v);
            imagenUsuario = (ImageView) v.findViewById(R.id.imagenUsuario);
            nombreUsuario = (TextView) v.findViewById(R.id.nombreUsuario);
            preguntaUsuario = (TextView) v.findViewById(R.id.preguntaUsuario);
            preguntaUsuario.setMovementMethod(new ScrollingMovementMethod());
            //botonRespuesta = (Button) v.findViewById(R.id.botonRespuesta);
            botonExpandir = (Button) v.findViewById(R.id.expandir);

        }

    }

    public AdaptadorPreguntas(List<Usuario> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AdaptadorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pregunta_card,viewGroup,false);
        return new AdaptadorViewHolder(v);
    }



    public void onBindViewHolder(final AdaptadorViewHolder viewHolder, int i) {
        final int imagen = items.get(i).getImagen();
        viewHolder.imagenUsuario.setImageResource(items.get(i).getImagen());
        viewHolder.nombreUsuario.setText(items.get(i).getNombre());
        viewHolder.preguntaUsuario.setText(String.valueOf(items.get(i).getCorreo()));


        /*viewHolder.botonRespuesta.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.botonRespuesta:
                        viewHolder.botonRespuesta.setText("Respondido");
                        viewHolder.botonRespuesta.setBackgroundColor(Color.BLUE);
                        break;

                }
            }
        });*/

        viewHolder.botonExpandir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(final View v)
            {
                switch (v.getId())
                {
                    case R.id.expandir:
                        AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                        final View dialog  = LayoutInflater.from(v.getContext()).inflate(R.layout.pregunta_expandida, null);
                        alert.setView(dialog);
                        ImageView imageView = (ImageView)dialog.findViewById(R.id.imagenUsuario);
                        imageView.setImageResource(imagen);
                        TextView textView = (TextView)dialog.findViewById(R.id.nombreUsuario);
                        textView.setText(viewHolder.nombreUsuario.getText().toString());
                        TextView textViewPregunta = (TextView)dialog.findViewById(R.id.preguntaUsuario);
                        textViewPregunta.setText(viewHolder.preguntaUsuario.getText().toString());
                        Button botonCerrar = (Button)dialog.findViewById(R.id.botonCerrar);
                        final AlertDialog alertDialog = alert.create();
                        botonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });


                        //respuestas.add(new Respuesta("Respuesta numero 1"));
                        //respuestas.add(new Respuesta("Respuesta numero 2"));
                        //respuestas.add(new Respuesta("Respuesta numero 3"));
                        //respuestas.add(new Respuesta("Respuesta numero 4"));
                        //respuestas.add(new Respuesta("Respuesta numero 5"));

                        recyclerView = (RecyclerView) dialog.findViewById(R.id.respuestasCard);
                        recyclerView.setHasFixedSize(true);
                        lManager = new LinearLayoutManager(v.getContext());
                        recyclerView.setLayoutManager(lManager);
                        if (respuestas.size() > 0) {
                            adapter = new AdaptadorRespuesta(respuestas);
                            recyclerView.setAdapter(adapter);
                        }

                        final ScrollView scroll = (ScrollView) dialog.findViewById(R.id.scrollRespuestas);
                        scroll.fullScroll(View.FOCUS_FORWARD);

                        final EditText respuesta = (EditText) dialog.findViewById(R.id.editBox);
                        final Button botonRespuesta = (Button)dialog.findViewById(R.id.btnResponder);
                        final ImageButton botonEnviar = (ImageButton) dialog.findViewById(R.id.send_button);
                        botonRespuesta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //respuestas.add(new Respuesta("Respuesta numero 5"));
                                //adapter = new AdaptadorRespuesta(respuestas);
                                //recyclerView.setAdapter(adapter);
                                botonRespuesta.setVisibility(View.INVISIBLE);
                                botonEnviar.setVisibility(View.VISIBLE);
                                respuesta.setVisibility(View.VISIBLE);
                                respuesta.requestFocus();
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(respuesta, InputMethodManager.SHOW_IMPLICIT);
                                //Intent myIntent = new Intent(v.getContext() , Chat_EditBox.class);
                                //((Activity)v.getContext() ).startActivityForResult(myIntent, 1);
                            }
                        });

                        botonEnviar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                botonEnviar.setVisibility(View.INVISIBLE);
                                botonRespuesta.setVisibility(View.VISIBLE);
                                respuestas.add(new Respuesta(respuesta.getText().toString()));
                                adapter = new AdaptadorRespuesta(respuestas);
                                recyclerView.setAdapter(adapter);
                                respuesta.setText("");
                                respuesta.setVisibility(View.INVISIBLE);
                            }
                        });

                        alertDialog.show();

                        break;
                }
            }
        });


    }
}
