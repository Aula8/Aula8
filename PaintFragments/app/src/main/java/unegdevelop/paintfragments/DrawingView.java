package unegdevelop.paintfragments;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

/**
 * Created by MujicaM on 11/05/2016.
 *
 * Edited by VunterSlaush on 19/06/2016
 */
public class DrawingView extends View
{

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private float brushSize, lastBrushSize;

    private boolean erase = false;

    private float posx [] = {0,0} ;
    private float posy [] = {0,0} ;

    private String brush = "point";

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing()
    {
//get drawing area setup for interaction

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        Servidor.anadirEventoRecibidoAlSocket("pintar",drawListener);
        Servidor.anadirEventoRecibidoAlSocket("borrar todo", eraseAllListener);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        if(brush.equals("point")) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawPath(drawPath, drawPaint);
        }
        else if(brush.equals("line")){

            canvas.drawLine(posx[0], posy[0], posx[1], posy[1], drawPaint);
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(brush.equals("point"))
        {
            float touchX = event.getX();
            float touchY = event.getY();
            pintarPunto(touchX,touchY,event.getAction());
            enviarStreamPunto(touchX,touchY,event.getAction());
        }
        else if(brush.equals("line"))
        {
            pintarLinea(event);
        }
        invalidate();
        return true;
    }

    private void pintarPunto(float x, float y, int event)
    {
        switch (event)
        {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
        }
    }

    private void pintarLinea(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                posx[0]=event.getX();
                posy[0]=event.getY();
                posx[1]=event.getX();
                posy[1]=event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                posx[1]=event.getX();
                posy[1]=event.getY();
                break;

            case MotionEvent.ACTION_UP:
                posx[1]=event.getX();
                posy[1]=event.getY();
                drawCanvas.drawLine(posx[0], posy[0], posx[1], posy[1], drawPaint);
                enviarStreamLinea();
                posx[0]=posx[1];
                posy[0]=posy[1];
                break;
        }
    }

    private void enviarStreamPunto(float x, float y, int eventType)
    {
        try
        {
            JSONObject obj = crearJSONPunto(x,y,eventType);
            Servidor.enviarEvento("pintar", obj);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void enviarStreamLinea()
    {
        try
        {
            JSONObject obj = crearJSONLinea();
            Servidor.enviarEvento("pintar", obj);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject crearJSONPunto(float x, float y, int eventType) throws JSONException
    {
        JSONObject obj = crearJSON();
        obj.put("x",x);
        obj.put("y",y);
        obj.put("event",eventType);
        return obj;
    }

    private JSONObject crearJSONLinea() throws JSONException
    {
        JSONObject obj = crearJSON();
        obj.put("x0",posx[0]);
        obj.put("x1",posx[1]);
        obj.put("y0",posy[0]);
        obj.put("y1",posy[1]);
        return obj;
    }


    private JSONObject crearJSON() throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("color",paintColor);
        obj.put("brushSize", brushSize);
        obj.put("brush",brush);
        obj.put("erase",erase);
        obj.put("width",drawCanvas.getWidth());
        obj.put("height",drawCanvas.getHeight());
        return obj;
    }

    public void setColor(String newColor)
    {

        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setColor(int newColor)
    {
        //set color
        invalidate();
        paintColor = newColor;
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize)
    {


        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize) {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    public void setErase(boolean isErase)
    {
        erase = isErase;
        if (erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
    }

    public String getBrush (){
        return brush;
    }

    public void setBrush (String typeBrush)
    {
        brush=typeBrush;
    }

    public void startNew()
    {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        setBrush("");
        invalidate();
    }


    private float reglaDeTres(int x,  int y,  float xx)
    {
        return ((y * xx) / x);
    }

    private Activity getActivity()
    {
        Activity activity = (Activity) getContext();
        return activity;
    }


    private void pintarStreamPunto(final JSONObject stream)
    {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    setPropiedades(stream);

                    int widthEnviador = stream.getInt("width");

                    int heightEnviador = stream.getInt("height");

                    float xEnviador = (float)stream.getDouble("x");

                    float yEnviador = (float)stream.getDouble("y");

                    float xReceptor = reglaDeTres(widthEnviador,
                            drawCanvas.getWidth(),
                            xEnviador);

                    float yReceptor = reglaDeTres(heightEnviador,
                            drawCanvas.getHeight(),
                            yEnviador);
                    pintarPunto(xReceptor,yReceptor,stream.getInt("event"));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    private void pintarStreamLinea(final JSONObject stream)
    {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    setPropiedades(stream);
                    setLine(stream);
                    drawCanvas.drawLine(posx[0], posy[0], posx[1], posy[1], drawPaint);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setLine(JSONObject stream) throws JSONException {
        int widthEnviador = stream.getInt("width");

        int heightEnviador = stream.getInt("height");

        posx[0] = reglaDeTres(widthEnviador,
                drawCanvas.getWidth(),
                (float) stream.getDouble("x0"));

        posx[1] = reglaDeTres(widthEnviador,
                drawCanvas.getWidth(),
                (float) stream.getDouble("x1"));


        posy[0] = reglaDeTres(heightEnviador,
                drawCanvas.getHeight(),
                (float) stream.getDouble("y0"));

        posy[1] = reglaDeTres(heightEnviador,
                drawCanvas.getHeight(),
                (float) stream.getDouble("y1"));
    }

    private void setPropiedades(JSONObject stream) throws JSONException
    {
        setBrushSize((float)stream.getDouble("brushSize"));
        setColor(stream.getInt("color"));
        setErase(stream.getBoolean("erase"));
        setBrush(stream.getString("brush"));
    }


    private Emitter.Listener drawListener = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            JSONObject obj = (JSONObject) args[0];
            try
            {
                if(obj.getString("brush").equals("point"))
                    pintarStreamPunto(obj);
                else
                    pintarStreamLinea(obj);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener eraseAllListener = new Emitter.Listener()
    {

        @Override
        public void call(Object... args)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    startNew();
                }
            });
        }
    };



}
