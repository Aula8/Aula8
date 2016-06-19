package unegdevelop.paintfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;


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
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, lineBtn;
    private float smallBrush, mediumBrush, largeBrush;

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
    public static Paint newInstance(String param1, String param2) {
        Paint fragment = new Paint();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        drawView = (DrawingView)getView().findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)getView().findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        drawBtn = (ImageButton)getView().findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(mediumBrush);
        eraseBtn = (ImageButton)getView().findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        newBtn = (ImageButton)getView().findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        lineBtn = (ImageButton)getView().findViewById(R.id.line_btn);
        lineBtn.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
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

    public void paintClicked(View view){
        //seleccionar el color

        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        if(view!=currPaint){
           //Actualiza el  color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public void onClick(View view){
//respond to clicks

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Tamaño del Pincel:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //Boton pequeño
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    drawView.setBrush("point");
                    brushDialog.dismiss();
                }
            });

            //Boton mediano
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    drawView.setBrush("point");
                    brushDialog.dismiss();
                }
            });

            //Boton grande
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    drawView.setBrush("point");
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Tamaño del Borrador:");
            brushDialog.setContentView(R.layout.brush_chooser);

            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrush("point");
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrush("point");
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrush("point");
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.new_btn){
            //Boton Limpiar

            AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
            newDialog.setTitle("Limpiar Pizarra");
            newDialog.setMessage("Esta seguro de que desea limpiar la pizarra?(Se perdera lo que ha dibujado)");
            newDialog.setPositiveButton("Si", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
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
            drawView.setBrushSize(smallBrush);
            drawView.setErase(false);

        }

    }

}
