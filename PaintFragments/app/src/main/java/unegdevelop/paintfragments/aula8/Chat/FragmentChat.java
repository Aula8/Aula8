package unegdevelop.paintfragments.aula8.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.emitter.Emitter;
import io.socket.client.Socket;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.TextBoxEdit;


public class FragmentChat extends android.support.v4.app.Fragment {
    private static final String NEW_MESSAGE_EVENT = "nuevo mensaje";
    private static final String NEW_USER = "usuario unido";
    private static final String USER_TYPING = "escribiendo";
    private static final String DISCONNECT_USER = "usuario desconectado";
    private static final String USER_NOT_TYPING = "no escribiendo";
    private static final String SEND_IMAGE = "enviar imagen";
    private static final int TYPING_TIMER_LENGTH = 600;

    private Activity activity;
    private View rootView;
    private EditText inputMensajeView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager lManager;
    private RecyclerView.Adapter<AdaptadorPreguntas.AdaptadorViewHolder> adapter;
    private List items = new ArrayList<>();

    private boolean estaEscribiendo = false;
    private String strUserName;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        addEnventsToClient();

        items.add(new Usuario(R.mipmap.ic_launcher, "Aula8", "Bienvenido a una nueva Sesión de Clase", "A8id"));
        //items.add(new Usuario(R.drawable.foto1, "Xavier Moreno", "profesor, no entiendo el valor de la transformada de furier realizada en el inciso anterior, cabe destacar que el valor preliminar no concuerda con los establecidos en la tabla, y su resultado variara mediante los requerimientos presentados en la clase anterior, y entoces se asentuan las dudas en el ejercicio en cuestion y no se puede avanzar asi con esas dudas profesor, no entiendo el valor de la transformada de furier realizada en el inciso anterior, cabe destacar que el valor preliminar no concuerda"));
        //items.add(new Usuario(R.drawable.foto1, "Acilio Simoes", "acilio@correo.com"));

        try {
            strUserName = Servidor.jsonObjUser.getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) rootView.findViewById(R.id.preguntas);
        recyclerView.setHasFixedSize(true);

        lManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(lManager);

        adapter = new AdaptadorPreguntas(items);
        recyclerView.setAdapter(adapter);

        inputMensajeView = (EditText) rootView.findViewById(R.id.message_input);


        ImageButton sendButton = (ImageButton) rootView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptSend();
                Intent myIntent = new Intent(getActivity(), Chat_EditBox.class);
                startActivityForResult(myIntent, 1);
            }
        });


        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("text");
                Servidor.enviarEvento(NEW_MESSAGE_EVENT, result);
            }
        }
    }

    private void attemptSend(String msj) {

        estaEscribiendo = false;

        //String msj = inputMensajeView.getText().toString();
        /*if (TextUtils.isEmpty(msj)) {
            inputMensajeView.requestFocus();
            return;
        }*/

        inputMensajeView.setText("");

        //repSend();

        //addMensaje(strUserName, msj, true);


        Servidor.enviarEvento(NEW_MESSAGE_EVENT, msj);
    }

    private void addMensaje(String usr, String msj, String id, boolean isLocal) {

        items.add(new Usuario(R.drawable.foto1, usr, msj, id));

        adapter.notifyItemInserted(items.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(recyclerView.getChildCount() - 1);
    }

    private Runnable onEscribiendoTimeout = new Runnable() {
        @Override
        public void run() {
            if (!estaEscribiendo) return;

            estaEscribiendo = false;
            Servidor.enviarEvento(USER_NOT_TYPING);
        }
    };


    private void addEnventsToClient() {
        Servidor.anadirEventoRecibidoAlSocket(Socket.EVENT_CONNECT_ERROR, onConnectError);
        Servidor.anadirEventoRecibidoAlSocket(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        Servidor.anadirEventoRecibidoAlSocket(NEW_MESSAGE_EVENT, onNuevoMensaje);
        Servidor.anadirEventoRecibidoAlSocket(NEW_USER, onUsuarioUnido);
        Servidor.anadirEventoRecibidoAlSocket(USER_TYPING, onEscribiendo);
        Servidor.anadirEventoRecibidoAlSocket(DISCONNECT_USER, onUsuarioDesconectado);
        Servidor.anadirEventoRecibidoAlSocket(USER_NOT_TYPING, onNoEscribiendo);
        Servidor.anadirEventoRecibidoAlSocket(SEND_IMAGE, onEnviarImagen);

    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity.getApplicationContext(),
                            R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onNuevoMensaje = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;
                    String mensaje;
                    final String q_id;
                    System.out.println(data);
                    //Toast.makeText(getContext(), data.toString(), Toast.LENGTH_LONG).show();
                    try {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        //Aqui hay que conseguir el nombre de usuario ..
                        mensaje = data.getString("mensaje");
                        q_id = data.getString("id_question");
                        //Servidor.setHQuestions(q_id, data);
                        //Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "Error al recibir msj", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    //removeEscribiendo("otro user");

                    //repGet();
                    addMensaje(nombre_Usuario, mensaje, q_id, false);
                }
            });
        }
    };

    private Emitter.Listener onUsuarioUnido = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;
                    int numUsuarios;
                    try {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        numUsuarios = data.getInt("numUsuarios");
                    } catch (JSONException e) {
                        return;
                    }

                    //addLog(getResources().getString(R.string.message_user_joined, nombre_Usuario));
                    //addParticipantesLog(numUsuarios);
                }
            });
        }
    };

    private Emitter.Listener onUsuarioDesconectado = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;
                    int numUsuarios;
                    try {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        numUsuarios = data.getInt("numUsuarios");
                    } catch (JSONException e) {
                        return;
                    }

                    //addLog(getResources().getString(R.string.message_user_left, nombre_Usuario));
                    //addParticipantesLog(numUsuarios);
                    //removeEscribiendo(nombre_Usuario);
                }
            });
        }
    };

    private Emitter.Listener onEscribiendo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario = "otro user";
                    //addEscribiendo(nombre_Usuario);
                }
            });
        }
    };

    private Emitter.Listener onNoEscribiendo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;

                    nombre_Usuario = "otro user";

                    //removeEscribiendo(nombre_Usuario);
                }
            });
        }
    };

    private Emitter.Listener onEnviarImagen = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String imgCodificada;
                    String nombre_Usuario;
                    try {


                        imgCodificada = data.getString("img_Codificada");

                        //repGet();

                        //addFile("otro user", decodeImage(imgCodificada), false);
                    } catch (JSONException e) {

                    }
                }
            });
        }
    };
}
