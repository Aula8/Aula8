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
import java.util.List;

import io.socket.emitter.Emitter;
import io.socket.client.Socket;
import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;
import unegdevelop.paintfragments.TextBoxEdit;


public class FragmentChat extends android.support.v4.app.Fragment
{
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        addEnventsToClient();

        items.add(new Usuario(R.drawable.foto1,"Isrrael Maita", "esto es una tremendo texto que meti aqui para ver si es necesario ponerle un scroll"));
        items.add(new Usuario(R.drawable.foto1,"Xavier Moreno", "profesor, no entiendo el valor de la transformada de furier realizada en el inciso anterior, cabe destacar que el valor preliminar no concuerda con los establecidos en la tabla, y su resultado variara mediante los requerimientos presentados en la clase anterior, y entoces se asentuan las dudas en el ejercicio en cuestion y no se puede avanzar asi con esas dudas profesor, no entiendo el valor de la transformada de furier realizada en el inciso anterior, cabe destacar que el valor preliminar no concuerda"));
        items.add(new Usuario(R.drawable.foto1,"Acilio Simoes", "acilio@correo.com"));

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
            public void onClick(View v)
            {
                //attemptSend();
                Intent myIntent = new Intent(getActivity() , Chat_EditBox.class);
                startActivityForResult(myIntent, 1);
            }
        });


        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1)
        {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("text");
                Servidor.enviarEvento(NEW_MESSAGE_EVENT, result);
            }
        }
    }

    private void attemptSend(String msj)
    {

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

    private void addMensaje(String usr, String msj, boolean isLocal)
    {

        items.add(new Usuario(R.drawable.foto1, usr, msj));

        adapter.notifyItemInserted(items.size() -1);
        scrollToBottom();
    }

    private void scrollToBottom()
    {
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


    private void addEnventsToClient()
    {
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
        public void call(final Object... args)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;
                    String mensaje;
                    System.out.println(data);
                    //Toast.makeText(getContext(), data.toString(), Toast.LENGTH_LONG).show();
                    try
                    {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        //Aqui hay que conseguir el nombre de usuario ..
                        mensaje = data.getString("mensaje");
                        //Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();

                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(getContext(), "Error al recibir msj", Toast.LENGTH_LONG)
                        .show();
                        return;
                    }

                    //removeEscribiendo("otro user");

                    //repGet();
                    addMensaje(nombre_Usuario, mensaje, false);
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
                    try
                    {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        numUsuarios = data.getInt("numUsuarios");
                    }
                    catch (JSONException e)
                    {
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
                    }
                    catch (JSONException e)
                    {

                    }
                }
            });
        }
    };

// cuando se descomente la parte de abajo, la llave que esta debajo de este comentario se debe eliminar
}


/*

    @Override
    public void onDestroy()
    {

        super.onDestroy();
    }

    public void onViewCreated()
    {

        //super.onViewCreated(view, savedInstanceState);

        mensajesView = (RecyclerView) activity.findViewById(R.id.messages);
        mensajesView.setLayoutManager(new LinearLayoutManager(activity));
        mensajesView.setAdapter(msjAdapter);

        inputMensajeView = (EditText) activity.findViewById(R.id.message_input);
        inputMensajeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event)
            {
                if (id == R.id.send || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        inputMensajeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null == strUserName) return;
                if (!Client.isConnected()) return;

                if (!estaEscribiendo)
                {
                    estaEscribiendo = true;
                    Client.enviarEvento(USER_TYPING);
                }

                escribiendoHandler.removeCallbacks(onEscribiendoTimeout);
                escribiendoHandler.postDelayed(onEscribiendoTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable s) {   }
        });

        ImageButton sendButton = (ImageButton) activity.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                attemptSend();
            }
        });

        ImageButton addButton = (ImageButton) activity.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PopupWindow popupMenu = menuAdd();
                popupMenu.showAsDropDown(v, -40, 18);
            }
        });

    }

    public PopupWindow menuAdd()
    {

        final PopupWindow popupMenu = new PopupWindow(activity);

        // inflate your layout or dynamically add view
        LayoutInflater inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_menu, null);

        ImageButton attachButton =(ImageButton)view.findViewById(R.id.menu_attach_button);
        attachButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                seleccionar();
            }
        });

        ImageButton micButton =(ImageButton)view.findViewById(R.id.menu_play_stop_button_record);
        micButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (!AudioStream.isRecording())
                    AudioStream.startRecording();
                else
                    AudioStream.stopRecording();
            }
        });

        popupMenu.setFocusable(true);
        popupMenu.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupMenu.setContentView(view);

        return popupMenu;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK != resultCode)
        {
            activity.finish();
            return;
        }

        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == Activity.RESULT_OK && null!=data)
        {
            final String selectedImagePath;
            InputStream is;
            try
            {

                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = activity.getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                selectedImagePath = cursor.getString(columnIndex);
                cursor.close();

                is = activity.getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new java.io.BufferedInputStream(is);
                Object fl = BitmapFactory.decodeStream(bis);

                //repSend();

                addFile(strUserName, fl, true);

                Client.enviarEvento(SEND_IMAGE, encodeFile(selectedImagePath));

            } catch (java.io.FileNotFoundException e) {}
        }
        else
        {
            strUserName = data.getStringExtra ("nombre_Usuario");
            int numUsers = data.getIntExtra ("numUsuarios", 1);

            addLog (getResources ().getString (R.string.message_welcome));
            addParticipantesLog (numUsers);
        }
    }

    private void addLog(String msj)
    {
        mensajes.add(new Mensaje.Builder(Mensaje.TYPE_LOG, false)
                .mensaje(msj).file(null).build());
        msjAdapter.notifyItemInserted(mensajes.size() - 1);
        scrollToBottom();
    }

    private void addParticipantesLog(int numUsers)
    {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void addMensaje(String usr, String msj, boolean isLocal)
    {
        mensajes.add(new Mensaje.Builder(Mensaje.TYPE_MESSAGE, isLocal).usuario(usr).mensaje(msj).file(null).build());
        msjAdapter.notifyItemInserted(mensajes.size() - 1);
        scrollToBottom();
    }

    private void addFile(String usr, Object file, boolean isLocal)
    {
        mensajes.add(new Mensaje.Builder(Mensaje.TYPE_IMAGE, isLocal)
                .usuario(usr).file(file).build());
        msjAdapter.notifyItemInserted(mensajes.size() - 1);
        scrollToBottom();
    }

    private void addEscribiendo(String usr)
    {
        mensajes.add(new Mensaje.Builder(Mensaje.TYPE_ACTION, false)
                .usuario(usr).file(null).build());
        msjAdapter.notifyItemInserted(mensajes.size() - 1);
        scrollToBottom();
    }

    private void removeEscribiendo(String usr)
    {
        for (int i = mensajes.size() - 1; i >= 0; i--)
        {
            Mensaje msj = mensajes.get(i);
            if (msj.getTipo() == Mensaje.TYPE_ACTION && msj.getUsuario().equals(usr)) {
                mensajes.remove(i);
                msjAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void attemptSend()
    {
        if (null == strUserName) return;
        if (!Client.isConnected()) return;

        estaEscribiendo = false;

        String msj = inputMensajeView.getText().toString().trim();
        if (TextUtils.isEmpty(msj)) {
            inputMensajeView.requestFocus();
            return;
        }

        inputMensajeView.setText("");

        //repSend();

        addMensaje(strUserName, msj, true);


        Client.enviarEvento(NEW_MESSAGE_EVENT, msj);
    }
/*
    private void repSend()
    {
        android.media.MediaPlayer snd = android.media.MediaPlayer.create(activity, R.raw.popsend);
        snd.start();
    }

    private void repGet()
    {
        android.media.MediaPlayer snd = android.media.MediaPlayer.create(activity, R.raw.popget);
        snd.start();
    }
*/
/*
    private void salir()
    {

        strUserName = null;
    }

    private void seleccionar()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
    }

    private String encodeFile(String path)
    {

        try
        {
            File file = new File(path);
            byte [] bytes = convertirAByteArray(file);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        catch (java.io.IOException e)
        {
            return null;
        }
    }

    private byte[] convertirAByteArray(File file) throws IOException
    {

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] arrayDeBytes = new byte[(int)file.length()];
        fileInputStream.read(arrayDeBytes);
        fileInputStream.close();

        return arrayDeBytes;
    }

    private Object decodeImage(String data)
    {
        byte[] b = Base64.decode(data,android.util.Base64.DEFAULT);
        Object bmp = BitmapFactory.decodeByteArray(b,0,b.length);
        return bmp;
    }

    private void scrollToBottom()
    {
        mensajesView.scrollToPosition(msjAdapter.getItemCount() - 1);
    }

    private void addEnventsToClient()
    {
        Client.anadirEvento(Socket.EVENT_CONNECT_ERROR, onConnectError);
        Client.anadirEvento(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        Client.anadirEvento(NEW_MESSAGE_EVENT, onNuevoMensaje);
        Client.anadirEvento(NEW_USER, onUsuarioUnido);
        Client.anadirEvento(USER_TYPING, onEscribiendo);
        Client.anadirEvento(DISCONNECT_USER, onUsuarioDesconectado);
        Client.anadirEvento(USER_NOT_TYPING, onNoEscribiendo);
        Client.anadirEvento(SEND_IMAGE, onEnviarImagen);

        AudioStream.startReceiver();
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
        public void call(final Object... args)
        {
            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    JSONObject data = (JSONObject) args[0];
                    String nombre_Usuario;
                    String mensaje;
                    try
                    {
                        //nombre_Usuario = data.getString("nombre_Usuario");
                        //Aqui hay que conseguir el nombre de usuario ..
                        mensaje = data.getString("mensaje");

                    }
                    catch (JSONException e)
                    {
                        return;
                    }

                    removeEscribiendo("otro user");

                    //repGet();
                    addMensaje("otro user", mensaje, false);
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

                    addLog(getResources().getString(R.string.message_user_joined, nombre_Usuario));
                    addParticipantesLog(numUsuarios);
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
                    try
                    {
                        nombre_Usuario = data.getString("nombre_Usuario");
                        numUsuarios = data.getInt("numUsuarios");
                    }
                    catch (JSONException e)
                    {
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, nombre_Usuario));
                    addParticipantesLog(numUsuarios);
                    removeEscribiendo(nombre_Usuario);
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
                    addEscribiendo(nombre_Usuario);
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

                    removeEscribiendo(nombre_Usuario);
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

                        addFile("otro user", decodeImage(imgCodificada), false);
                    }
                    catch (JSONException e)
                    {

                    }
                }
            });
        }
    };

    private Runnable onEscribiendoTimeout = new Runnable() {
        @Override
        public void run() {
            if (!estaEscribiendo) return;

            estaEscribiendo = false;
            Client.enviarEvento(USER_NOT_TYPING);
        }
    };
}
*/
