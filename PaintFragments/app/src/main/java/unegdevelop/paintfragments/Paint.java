package unegdevelop.paintfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;

import io.socket.emitter.Emitter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Paint.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Paint#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Paint extends Fragment implements OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // aqui va  los parámetros de inicialización de fragmentos , por ejemplo, ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DrawingView drawView;
    private ImageButton  drawBtn, eraseBtn, newBtn, lineBtn, colores, voiceBtn;
    private ImageButton  sigBtn, prevBtn, pdfBtn, zinTtn, zoutBtn;
    private PDFImageView pdf;
    private float TamañoPincel;
    private SeekBar BarraPincel;
    private TextView TextoTamañoPincel;
    private ImageView imageView;
    public Paint() {
        // requiere constructor vacio
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Paint.
     */
    // TODO: Rename and change types and number of parameters
    public static Paint newInstance(String param1, String param2)
    {
        Paint fragment = new Paint();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // diseño de este fragmento

        View RootView = inflater.inflate(R.layout.fragment_paint, container, false);

        return RootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        drawView = (DrawingView)getView().findViewById(R.id.drawing);

        TextoTamañoPincel = (TextView)getView().findViewById(R.id.TamañoPincel);

        TamañoPincel = 25;
        TextoTamañoPincel.setText(TamañoPincel+" px");

        BarraPincel = (SeekBar)getView().findViewById(R.id.BarraPincel);
        BarraPincel.setMax(50);
        BarraPincel.setProgress((int)TamañoPincel);

        BarraPincel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub

                TamañoPincel=progress;
                TextoTamañoPincel.setText(TamañoPincel+" px");
                drawView.setBrushSize(TamañoPincel);
                drawView.setLastBrushSize(TamañoPincel);
            }
        });

        colores = (ImageButton)getView().findViewById(R.id.colores);
        colores.setOnClickListener(this);
        drawBtn = (ImageButton)getView().findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(20);
        eraseBtn = (ImageButton)getView().findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)getView().findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        lineBtn = (ImageButton)getView().findViewById(R.id.line_btn);
        lineBtn.setOnClickListener(this);
        sigBtn = (ImageButton) getView().findViewById(R.id.prox);
        sigBtn.setOnClickListener(this);
        prevBtn = (ImageButton) getView().findViewById(R.id.prev);
        prevBtn.setOnClickListener(this);
        pdfBtn = (ImageButton) getView().findViewById(R.id.cargarpdf);
        pdfBtn.setOnClickListener(this);
        zinTtn = (ImageButton) getView().findViewById(R.id.zoomdown);
        zinTtn.setOnClickListener(this);
        zoutBtn = (ImageButton) getView().findViewById(R.id.zoomup);
        zoutBtn.setOnClickListener(this);


        voiceBtn = (ImageButton)getView().findViewById(R.id.voice_btn);
        voiceBtn.setOnClickListener(this);
        imageView = (ImageView)getView().findViewById(R.id.PDF);
        pdf = null;
        Servidor.anadirEventoRecibidoAlSocket("descargar pdf",onNewPDF);
        Servidor.anadirEventoRecibidoAlSocket("pag_sig",onSigPag);
        Servidor.anadirEventoRecibidoAlSocket("pag_prev",onPrevPag);
        Servidor.anadirEventoRecibidoAlSocket("zoom_in",onZoomIn);
        Servidor.anadirEventoRecibidoAlSocket("zoom_out",onZoomOut);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void paintClicked(String color){
        //seleccionar el color


        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        //Actualiza el  color
        drawView.setColor(color);
        Drawable PD = BarraPincel.getProgressDrawable();
        PD.setColorFilter(Color.parseColor(color),android.graphics.PorterDuff.Mode.MULTIPLY);
        BarraPincel.setProgressDrawable(PD);


    }

    @Override
    public void onClick(View view){
//respond to clicks

        if(view.getId()==R.id.draw_btn){
            //draw button clicked

            drawView.setErase(false);
            drawView.setBrush("point");

        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            drawView.setErase(true);
            drawView.setBrush("point");

        }
        else if(view.getId()==R.id.new_btn)
        {
            //Boton Limpiar

            AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
            newDialog.setTitle("Limpiar Pizarra");
            newDialog.setMessage("Esta seguro de que desea limpiar la pizarra?(Se perdera lo que ha dibujado)");
            newDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    Servidor.enviarEvento("borrar todo");
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();

        }else if(view.getId()==R.id.line_btn){
            drawView.setBrush("line");
            drawView.setErase(false);

        }else if(view.getId()==R.id.colores){

            Intent myIntent = new Intent(Paint.this.getActivity() , DialogoPintura.class);
            Paint.this.startActivityForResult(myIntent,1);
        }
        else if(view.getId() == R.id.voice_btn)
        {
            if(AudioStream.isRecording())
            {
                AudioStream.stopRecording();
                voiceBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_audio_online));
            }
            else
            {
                AudioStream.startRecording();
                voiceBtn.setImageDrawable(getResources().getDrawable(android.R.drawable.presence_audio_busy));
            }
        }
        else if(view.getId() == R.id.zoomdown)
        {
            if(pdf!=null)
            {
                try
                {
                    pdf.zoomOut();
                    Servidor.enviarEvento("zoom_out");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        else if(view.getId() == R.id.zoomup)
        {
            try
            {
                pdf.zoomIn();
                Servidor.enviarEvento("zoom_in");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(view.getId() ==  R.id.prox)
        {
            try
            {
                pdf.nextPage();
                Servidor.enviarEvento("pag_sig");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(view.getId() ==  R.id.prev)
        {
            try
            {
                pdf.prevPage();
                Servidor.enviarEvento("pag_prev");
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(view.getId() == R.id.cargarpdf)
        {
            new FileChooser((Activity) getContext()).setFileListener(new FileChooser.FileSelectedListener()
            {
                @Override
                public void fileSelected(final File file)
                {
                    try
                    {
                        if(file.getName().endsWith(".pdf"))
                        {
                            pdf = new PDFImageView(file.getAbsolutePath(),imageView);
                            replicarPDF(file);
                        }
                    }
                    catch (Exception e)
                    {
                        pdf = null;
                    }
                }
            }).showDialog();

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("color");
                paintClicked(result);
            }
        }
    }

    private void replicarPDF(final File file)
    {
        Thread hiloReplicacionPDF = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //Aqui tiene que ir el nombre de la carpeta de la clase
                    //Cambiar PDF_ACTUAL por la carpeta donde se vaya a guardar el PDF..
                    FilesController.uploadFile(file, "PDF_ACTUAL/");
                    Servidor.enviarEvento("descargar pdf", "PDF_ACTUAL/"+file.getName() );

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        hiloReplicacionPDF.start();

    }

    Emitter.Listener onNewPDF = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            String dir = (String)args[0];
            System.out.println(dir);
            FilesController.donwloadFile(dir);
            int p = dir.lastIndexOf("/");
            final String filename = Utils.getA8Folder()+dir.substring(p);
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    File f = new File(filename);

                    while(!f.exists());
                    try
                    {
                        pdf = new PDFImageView(filename,imageView);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }
    };

    Emitter.Listener onSigPag = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(pdf != null)
                        try {
                            pdf.nextPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    };

    Emitter.Listener onPrevPag = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(pdf != null)
                        try {
                            pdf.prevPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    };

    Emitter.Listener onZoomIn = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(pdf != null)
                        try {
                            pdf.zoomIn();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    };

    Emitter.Listener onZoomOut = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(pdf != null)
                        try {
                            pdf.zoomOut();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    };
}
