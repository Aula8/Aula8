package unegdevelop.paintfragments.aula8.Chat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import unegdevelop.paintfragments.FileChooser;
import unegdevelop.paintfragments.FilesController;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;

/**
 * Created by xavier on 12/07/16.
 */
public class AdaptadorPreguntas extends RecyclerView.Adapter<AdaptadorPreguntas.AdaptadorViewHolder> {

    private static final String ENVIAR_RESPUESTA = "nueva respuesta";

    private Context context;
    private List<Usuario> items;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView.Adapter<AdaptadorRespuesta.AdaptadorViewHolder> adapter;
    public HashMap<String, List> respuestas = new HashMap<>();
    String idPregunta;
    String key;


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
            //imagenUsuario = (ImageView) v.findViewById(R.id.imagenUsuario); ELiminado
            nombreUsuario = (TextView) v.findViewById(R.id.nombreUsuario);
            preguntaUsuario = (TextView) v.findViewById(R.id.preguntaUsuario);
            preguntaUsuario.setMovementMethod(new ScrollingMovementMethod());
            //botonRespuesta = (Button) v.findViewById(R.id.botonRespuesta); Eliminado
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
        Servidor.anadirEventoRecibidoAlSocket(ENVIAR_RESPUESTA, onNewResponse);
        context = v.getContext();
        return new AdaptadorViewHolder(v);
    }

    public Emitter.Listener onNewResponse = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            final JSONObject data = (JSONObject) args[0];
            //final View dialog  = LayoutInflater.from(context).inflate(R.layout.pregunta_expandida, null);
            String nombre_Usuario;
            String mensaje = "";
            String idPregunta = "";
            try {
                nombre_Usuario = data.getString("user_response");
                mensaje = data.getString("response");
                idPregunta = data.getString("id_question");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println(data);
            //adapter.notifyItemInserted(respuestas.get(key).size() - 1);

            for(int i = 0 ; i < items.size() ; i ++){
                if (items.get(i).getIDPregunta().compareTo(idPregunta) == 0){
                    items.get(i).setPreguntas(new Respuesta(mensaje));
                    break;
                }
            }
        }
    };



    public void onBindViewHolder(final AdaptadorViewHolder viewHolder, final int i) {
        final int imagen = items.get(i).getImagen();
        //viewHolder.imagenUsuario.setImageResource(items.get(i).getImagen()); ELIMINADO
        viewHolder.nombreUsuario.setText(items.get(i).getNombre());
        viewHolder.preguntaUsuario.setText(String.valueOf(items.get(i).getCorreo()));


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
                        //ImageView imageView = (ImageView)dialog.findViewById(R.id.imagenUsuario); NOOOOOO
                        //imageView.setImageResource(imagen); NOOOOOOOOOOOOOOO
                        TextView textView = (TextView)dialog.findViewById(R.id.nombreUsuario);
                        textView.setText(viewHolder.nombreUsuario.getText().toString());
                        TextView textViewPregunta = (TextView)dialog.findViewById(R.id.preguntaUsuario);
                        textViewPregunta.setText(viewHolder.preguntaUsuario.getText().toString());

                        final ScrollView scroll = (ScrollView) dialog.findViewById(R.id.scrollRespuestas);
                        scroll.fullScroll(View.FOCUS_FORWARD);

                        final AlertDialog alertDialog = alert.create();

                        Button botonCerrar = (Button)dialog.findViewById(R.id.botonCerrar);
                        botonCerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });



                        final EditText respuesta = (EditText) dialog.findViewById(R.id.editBox);
                        final Button botonRespuesta = (Button)dialog.findViewById(R.id.btnResponder);
                        final ImageButton botonEnviar = (ImageButton) dialog.findViewById(R.id.send_button);

                        if (items.get(i).getPreguntas().size() > 0){
                            adapter = new AdaptadorRespuesta(items.get(i).getPreguntas());
                            recyclerView = (RecyclerView) dialog.findViewById(R.id.respuestasCard);
                            recyclerView.setHasFixedSize(true);
                            lManager = new LinearLayoutManager(dialog.getContext());
                            recyclerView.setLayoutManager(lManager);
                            recyclerView.setAdapter(adapter);
                        }

                        botonRespuesta.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //respuestas.add(new Respuesta("Respuesta numero 5"));
                                //adapter = new AdaptadorRespuesta(respuestas);
                                //recyclerView.setAdapter(adapter);
                                botonRespuesta.setVisibility(View.GONE); //AQUIIIIIIIIIIIIIIIIIIIII
                                botonEnviar.setVisibility(View.VISIBLE);
                                respuesta.setVisibility(View.VISIBLE);
                                respuesta.requestFocus();
                                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(dialog, InputMethodManager.SHOW_IMPLICIT);
                                //Intent myIntent = new Intent(v.getContext() , Chat_EditBox.class);
                                //((Activity)v.getContext() ).startActivityForResult(myIntent, 1);
                            }
                        });

                        botonEnviar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                botonEnviar.setVisibility(View.GONE); //AQUIIIIIIIIIIIIIIIIIIIII
                                botonRespuesta.setVisibility(View.VISIBLE);

                                JSONObject r = new JSONObject();
                                try {
                                    r.put("response", respuesta.getText().toString());
                                    r.put("user_response", Servidor.jsonObjUser.getString("username"));
                                    idPregunta = items.get(i).getIDPregunta();
                                    r.put("id", idPregunta);
                                    Servidor.enviarEvento(ENVIAR_RESPUESTA, r);
                                    items.get(i).setPreguntas(new Respuesta(respuesta.getText().toString()));
                                    //r.put("question", );
                                } catch (JSONException e) {
                                    Toast.makeText(v.getContext(), "Error al enviar al servidor", Toast.LENGTH_SHORT).show();
                                }

                                //respuestas.put(idPregunta, new ArrayList());
                                //respuestas.add(new HashMap<String, List>(){{put(idPregunta, new ArrayList<Respuesta>());}});

                                /*Iterator iterator = respuestas.keySet().iterator();
                                while (iterator.hasNext()){
                                    key = ((String) iterator.next());
                                    if ( key == idPregunta  ){
                                        respuestas.get(key).add(new Respuesta(respuesta.getText().toString()));
                                    }
                                }*/

                                adapter = new AdaptadorRespuesta(items.get(i).getPreguntas());
                                recyclerView = (RecyclerView) dialog.findViewById(R.id.respuestasCard);
                                recyclerView.setHasFixedSize(true);
                                lManager = new LinearLayoutManager(dialog.getContext());
                                recyclerView.setLayoutManager(lManager);
                                recyclerView.setAdapter(adapter);

                                //adapter.notifyItemInserted(items.get(i).getPreguntas(idPregunta).size() - 1);


                                respuesta.setText("");
                                respuesta.setVisibility(View.GONE); //AQUIIIIIIIIIIIIIIIIIIIIIIIII
                            }
                        });

                        alertDialog.show();

                        break;
                }

            }
        });

    }
}
