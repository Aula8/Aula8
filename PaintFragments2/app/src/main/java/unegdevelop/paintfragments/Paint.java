package unegdevelop.paintfragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import unegdevelop.paintfragments.aula8.StudyMaterial.DialogFileDownload;

public class Paint extends Fragment implements OnClickListener
{
    //Parámetros de inicialización de fragmentos , por ejemplo, ARG_ITEM_NUMBER
    private static final String           ARG_PARAM1 = "param1";
    private static final String           ARG_PARAM2 = "param2";

    private String                        mParam1;
    private String                        mParam2;

    private OnFragmentInteractionListener mListener;

    private DrawingView                   drawView;
    private ImageButton                   drawBtn;
    private ImageButton                   eraseBtn;
    private ImageButton                   newBtn;
    private ImageButton                   lineBtn;
    private ImageButton                   colores;
    private ImageButton                   voiceBtn;
    //private ImageButton                   shapeBtn;
    private ImageButton                   textBoxBtn;
    private ImageButton                   sigBtn;
    private ImageButton                   prevBtn;
    private ImageButton                   pdfBtn;
    private ImageButton                   zinTtn;
    private ImageButton                   zoutBtn;
    private ImageButton                   rotateBtn;
    private PDFImageView                  pdf;
    private float                         tamanioPincel;
    private SeekBar                       barraPincel;
    private TextView                      textoTamanioPincel;
    private ImageView                     imageView;


    // TODO Agregado Alex 20/07/16 1:27 a.m
    private ImageButton                   shapeToolBtn;
    private ImageButton                   drawToolBtn;
    private ImageButton                   pdfToolBtn;
    private ImageButton                   shapeRectBtn;
    private ImageButton                   shapeOvalBtn;
    private ImageButton                   filledRectBtn;
    private ImageButton                   filledOvalBtn;
    private ImageButton                   downloadMaterialBtn;
    private ImageButton                   end_sesion;

    public Paint() {
    }

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

        View rootView = inflater.inflate(R.layout.fragment_paint, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        iniciarPropiedades(getView());
            if(Servidor.haveAccess())
                iniciarBotones(getView());
            iniciaDownloadBtn(getView());

        anadirEventosDeReplicacion();
    }

    private void iniciarPropiedades(View view)
    {
        drawView = (DrawingView)view.findViewById(R.id.drawing);

        textoTamanioPincel = (TextView)view.findViewById(R.id.TamañoPincel);

        tamanioPincel = 25;
        textoTamanioPincel.setText(tamanioPincel + " px");

        drawView.setBrushSize(20);

        barraPincel = (SeekBar)view.findViewById(R.id.BarraPincel);
        barraPincel.setMax(50);
        barraPincel.setProgress((int)tamanioPincel);

        barraPincel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                tamanioPincel = progress;
                textoTamanioPincel.setText(tamanioPincel + " px");
                drawView.setBrushSize(tamanioPincel);
                drawView.setLastBrushSize(tamanioPincel);
            }
        });

        imageView = (ImageView)view.findViewById(R.id.PDF);

        pdf = null;
    }

    private void iniciarBotones(View view)
    {
        colores = (ImageButton) view.findViewById(R.id.colores);
        colores.setOnClickListener(this);

        drawBtn = (ImageButton) view.findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        eraseBtn = (ImageButton) view.findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        newBtn = (ImageButton) view.findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        lineBtn = (ImageButton) view.findViewById(R.id.line_btn);
        lineBtn.setOnClickListener(this);

        sigBtn = (ImageButton) view.findViewById(R.id.prox);
        sigBtn.setOnClickListener(this);

        prevBtn = (ImageButton) view.findViewById(R.id.prev);
        prevBtn.setOnClickListener(this);

        pdfBtn = (ImageButton) view.findViewById(R.id.cargarpdf);
        pdfBtn.setOnClickListener(this);

        zinTtn = (ImageButton) view.findViewById(R.id.zoomdown);
        zinTtn.setOnClickListener(this);

        zoutBtn = (ImageButton) view.findViewById(R.id.zoomup);
        zoutBtn.setOnClickListener(this);

        rotateBtn = (ImageButton)view.findViewById(R.id.rotar_pdf);
        rotateBtn.setOnClickListener(this);

        /*shapeBtn = (ImageButton) view.findViewById(R.id.shape_btn);
        shapeBtn.setOnClickListener(this);*/

        textBoxBtn = (ImageButton) view.findViewById(R.id.text_box_btn);
        textBoxBtn.setOnClickListener(this);

        voiceBtn = (ImageButton)view.findViewById(R.id.voice_btn);
        voiceBtn.setOnClickListener(this);

        //TODO Agregado Alex 1:31 a.m. 20/07/16
        shapeToolBtn = (ImageButton)view.findViewById(R.id.shapes_tool_btn);
        shapeToolBtn.setOnClickListener(this);

        drawToolBtn = (ImageButton)view.findViewById(R.id.draw_tool_btn);
        drawToolBtn.setOnClickListener(this);

        pdfToolBtn = (ImageButton)view.findViewById(R.id.pdf_tool_btn);
        pdfToolBtn.setOnClickListener(this);

        shapeRectBtn = (ImageButton)view.findViewById(R.id.rectangle_shape_btn);
        shapeRectBtn.setOnClickListener(this);

        shapeOvalBtn = (ImageButton)view.findViewById(R.id.oval_shape_btn);
        shapeOvalBtn.setOnClickListener(this);

        filledRectBtn = (ImageButton)view.findViewById(R.id.filled_rectangle_shape_btn);
        filledRectBtn.setOnClickListener(this);

        filledOvalBtn = (ImageButton)view.findViewById(R.id.filled_oval_shape_btn);
        filledOvalBtn.setOnClickListener(this);

        end_sesion = (ImageButton)view.findViewById(R.id.end_session);
        end_sesion.setOnClickListener(this);

        //Fin editado

    }

    private void iniciaDownloadBtn(View view)
    {
        downloadMaterialBtn = (ImageButton)view.findViewById(R.id.download_tool_btn);
        downloadMaterialBtn.setOnClickListener(this);
    }

    private void anadirEventosDeReplicacion()
    {
        Servidor.anadirEventoRecibidoAlSocket("descargar pdf", onNewPDF);
        Servidor.anadirEventoRecibidoAlSocket("pagina", onPag);
        Servidor.anadirEventoRecibidoAlSocket("zoom_in", onZoomIn);
        Servidor.anadirEventoRecibidoAlSocket("zoom_out", onZoomOut);
        Servidor.anadirEventoRecibidoAlSocket("rotar", onRotate);
        Servidor.anadirEventoRecibidoAlSocket("getFilesSubject", filesSubject);
    }

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void paintClicked(String color){

        //seleccionar el color
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        //Actualiza el  color
        drawView.setColor(color);
        Drawable pD = barraPincel.getProgressDrawable();
        pD.setColorFilter(Color.parseColor(color),android.graphics.PorterDuff.Mode.MULTIPLY);
        barraPincel.setProgressDrawable(pD);


    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            /*case R.id.shape_btn:
                shapeBtnClick();
                break;*/
            case R.id.text_box_btn:
                textBoxBtnClick();
                break;
            case R.id.draw_btn:
                drawBtnClick();
                break;
            case R.id.erase_btn:
                eraseBtnClick();
                break;
            case R.id.new_btn:
                newBtnClick();
                break;
            case R.id.line_btn:
                lineBtnClick();
                break;
            case R.id.colores:
                colorsClick();
                break;
            case R.id.voice_btn:
                voiceBtnClick();
                break;
            case R.id.zoomdown:
                zoomDownClick();
                break;
            case R.id.zoomup:
                zoomUpClick();
                break;
            case R.id.rotar_pdf:
                rotarPDFClick();
                break;
            case R.id.prox:
                proxClick();
                break;
            case R.id.prev:
                prevClick();
                break;
            case R.id.cargarpdf:
                cargarPDFClick();
                break;

            //TODO Alex Agregado 1:33 a.m. 20/07/16
            case R.id.draw_tool_btn:
                showPdfToolbar(false);
                showShapeToolbar(false);
                showDrawToolbar(true);
                break;
            case R.id.shapes_tool_btn:
                showDrawToolbar(false);
                showPdfToolbar(false);
                showShapeToolbar(true);
                break;
            case R.id.pdf_tool_btn:
                drawView.setBrush("point");
                showDrawToolbar(false);
                showShapeToolbar(false);
                showPdfToolbar(true);
                break;
            case R.id.rectangle_shape_btn:
                drawView.setBrush("rectangle_shape");
                drawView.setErase(false);
                break;
            case R.id.filled_rectangle_shape_btn:
                drawView.setBrush("filled_rectangle_shape");
                drawView.setErase(false);
                break;
            case R.id.oval_shape_btn:
                drawView.setBrush("oval_shape");
                drawView.setErase(false);
                break;
            case R.id.filled_oval_shape_btn:
                drawView.setBrush("filled_oval_shape");
                drawView.setErase(false);
                break;
            case R.id.download_tool_btn:
                showDownloadMaterial();
            case R.id.end_session:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            //fin Alex Agreado
            default:
                break;
        }
    }

    //TODO Alex agregado 1:36 a.m. 20/07/16
    private void showDrawToolbar(boolean show){
        if (show){
            drawBtn.setVisibility(View.VISIBLE);
            colores.setVisibility(View.VISIBLE);
            eraseBtn.setVisibility(View.VISIBLE);
            lineBtn.setVisibility(View.VISIBLE);
        }else{
            drawBtn.setVisibility(View.GONE);
            colores.setVisibility(View.GONE);
            eraseBtn.setVisibility(View.GONE);
            lineBtn.setVisibility(View.GONE);
        }
    }

    private void showShapeToolbar(boolean show){
        if (show){
            colores.setVisibility(View.VISIBLE);
            shapeRectBtn.setVisibility(View.VISIBLE);
            filledRectBtn.setVisibility(View.VISIBLE);
            shapeOvalBtn.setVisibility(View.VISIBLE);
            filledOvalBtn.setVisibility(View.VISIBLE);
        }else{
            shapeRectBtn.setVisibility(View.GONE);
            filledRectBtn.setVisibility(View.GONE);
            shapeOvalBtn.setVisibility(View.GONE);
            filledOvalBtn.setVisibility(View.GONE);
        }
    }

    private void showPdfToolbar(boolean show){
        if (show){
            sigBtn.setVisibility(View.VISIBLE);
            prevBtn.setVisibility(View.VISIBLE);
            pdfBtn.setVisibility(View.VISIBLE);
            zinTtn.setVisibility(View.VISIBLE);
            zoutBtn.setVisibility(View.VISIBLE);
            rotateBtn.setVisibility(View.VISIBLE);
        }else{
            sigBtn.setVisibility(View.GONE);
            prevBtn.setVisibility(View.GONE);
            pdfBtn.setVisibility(View.GONE);
            zinTtn.setVisibility(View.GONE);
            zoutBtn.setVisibility(View.GONE);
            rotateBtn.setVisibility(View.GONE);
        }
    }

    private void showDownloadMaterial(){
        JSONObject data = new JSONObject();
        String materia = Servidor.getActualSubject();
        String section = Servidor.getActualSection();
        String session = Servidor.room;
        try {
            data.put("subject", materia);
            data.put("section", section);
            data.put("session", session);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Servidor.enviarEvento("getFilesSubject", data);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode)
        {
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("color");
                    paintClicked(result);
                }
                break;
            case 2:/*
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("shape");
                    drawView.setBrush(result);
                }
                break;*/
            case 3:
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("text");
                    drawView.setCurrentText(result);
                    drawView.setBrush("text");
                }
                break;
            default:
                break;
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
                    //Cambiar PDF_ACTUAL por la carpeta donde se vaya a guardar el PDF

                    FilesController.uploadFile(file, "PDF_ACTUAL/",getContext(),true);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
        });
        hiloReplicacionPDF.start();

    }

    private void enviarPagina(int pagina)
    {
        JSONObject obj = new JSONObject();
        try {
            obj.put("pagina",pagina);
            Servidor.enviarEvento("pagina", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Emitter.Listener filesSubject = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            List material;
            final JSONObject data = (JSONObject) args[0];
            try {
                JSONArray dataResult = data.getJSONArray("result");
                String dataLink = data.get("link").toString();
                FragmentActivity activity = (FragmentActivity)(getContext());
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

    Emitter.Listener onNewPDF = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            final String dir = (String)args[0];

            Thread t = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        String filename;
                        String filedir = Utils.reemplazarSlash(dir);
                        System.out.println("DESCARGANDO EN "+filedir);
                        FilesController.donwloadFile(filedir, getActivity().getApplicationContext());
                        int slashR = filedir.lastIndexOf("/")+1;
                        filename = Utils.getA8Folder() + filedir.substring(slashR);
                        while (!FilesController.getDownloadState());
                        asignarPDF(filename);

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            t.start();

        }
    };

    private void asignarPDF(final String filename)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Toast.makeText(getActivity(),"Cargando en Pantalla: "+filename
                            , Toast.LENGTH_LONG).show();
                    pdf = new PDFImageView(filename, imageView);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
    Emitter.Listener onPag = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            final JSONObject obj = (JSONObject) args[0];
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(pdf != null)
                        try {
                            pdf.gotoPage(obj.getInt("pagina"));
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

    Emitter.Listener onRotate = new Emitter.Listener()
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
                            pdf.rotar();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            });
        }
    };

    /*//Slots onClick
    private void shapeBtnClick()
    {
        Intent myIntent = new Intent(Paint.this.getActivity() , ShapeChooser.class);
        Paint.this.startActivityForResult(myIntent,2);
    }*/

    private void textBoxBtnClick()
    {
        Intent myIntent = new Intent(Paint.this.getActivity() , TextBoxEdit.class);
        Paint.this.startActivityForResult(myIntent,3);
    }

    private void drawBtnClick()
    {
        drawView.setErase(false);
        drawView.setBrush("point");
    }

    private void eraseBtnClick()
    {
        drawView.setErase(true);
        drawView.setBrush("point");
    }

    private void newBtnClick()
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
    }

    private void lineBtnClick()
    {
        drawView.setBrush("line");
        drawView.setErase(false);
    }

    private void colorsClick()
    {
        Intent myIntent = new Intent(Paint.this.getActivity() , DialogoPintura.class);
        Paint.this.startActivityForResult(myIntent,1);
    }

    private void voiceBtnClick()
    {
        if(AudioStream.isRecording())
        {
            AudioStream.stopRecording();
            voiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.microfono));
        }
        else
        {
            AudioStream.startRecording();
            voiceBtn.setImageDrawable(getResources().getDrawable(R.drawable.microfonored));
        }
    }

    private void zoomDownClick()
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

    private void zoomUpClick()
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

    private void rotarPDFClick()
    {
        try
        {
            pdf.rotar();
            Servidor.enviarEvento("rotar");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void proxClick()
    {
        try
        {
            pdf.nextPage();
            enviarPagina(pdf.getPage());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void prevClick()
    {
        try
        {
            pdf.prevPage();
            enviarPagina(pdf.getPage());

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void cargarPDFClick()
    {
        new FileChooser((Activity) getContext()).setFileListener(new FileChooser.FileSelectedListener()
        {
            @Override
            public void fileSelected(final File file)
            {
                try
                {
                    cargarPDF(file);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).showDialog();
    }

    private void cargarPDF(File file) throws Exception
    {
        if(pdf != null)
            pdf = null;

        if(file.getName().endsWith(".pdf") || file.getName().endsWith(".PDF"))
        {
            pdf = new PDFImageView(file.getAbsolutePath(),imageView);
            replicarPDF(file);
        }
    }

}
