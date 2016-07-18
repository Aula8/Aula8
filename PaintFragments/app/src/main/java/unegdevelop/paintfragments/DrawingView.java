package unegdevelop.paintfragments;

import android.app.Activity;
import android.app.Notification;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

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

    private String currentText;


    private float brushSize, lastBrushSize, savedBrushSize;

    private boolean erase = false;

    private float posx [] = {0,0} ;
    private float posy [] = {0,0} ;


    private float textX = -10;
    private float textY = -10;


    private String brush = "point";

    public DrawingView(Context context, AttributeSet attrs)
    {
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

        //Alex!!!
        currentText = "newText";


        Servidor.anadirEventoRecibidoAlSocket("pintar",drawListener);
        Servidor.anadirEventoRecibidoAlSocket("borrar todo", eraseAllListener);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if(brush.equals("text"))
        {
            prepareForText();
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawText(currentText,textX,textY,drawPaint);
        }
        else if(brush.equals("point")) 
        {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawPath(drawPath, drawPaint);
        }
        else if(brush.equals("line"))
        {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawLine(posx[0], posy[0], posx[1], posy[1], drawPaint);
            //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
        }
        else if(brush.equals("rectangle_shape"))
        { // ALEX!!!
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawRect(calculateDirection(posx[1], posy[1], posx[0], posy[0]), drawPaint);
            
        }
        else if(brush.equals("filled_rectangle_shape"))
        { // ALEX!!!
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            drawPaint.setStyle(Paint.Style.FILL);
            canvas.drawRect(calculateDirection(posx[1], posy[1], posx[0], posy[0]), drawPaint);
            drawPaint.setStyle(Paint.Style.STROKE);
        }
        else if(brush.equals("oval_shape"))
        { // ALEX!!!
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            canvas.drawOval(calculateDirection(posx[1], posy[1], posx[0], posy[0]), drawPaint);
            
        }
        else if(brush.equals("filled_oval_shape"))
        { // ALEX!!!
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);//Refresca la pantalla
            drawPaint.setStyle(Paint.Style.FILL);
            canvas.drawOval(calculateDirection(posx[1], posy[1], posx[0], posy[0]), drawPaint);
            drawPaint.setStyle(Paint.Style.STROKE);
        }

    }

    //ALEX!!!
    public RectF calculateDirection(float x1,float y1,float x2,float y2)
    {
        if (x2 < x1){
            float temp = x2;
            x2 = x1;
            x1 = temp;
        }

        if (y2 < y1){
            float temp = y2;
            y2 = y1;
            y1 = temp;
        }

        return new RectF(x1,y1,x2,y2);
    }
    //ALEXX
    private void prepareForText()
    {
        drawPaint.setTextSize(brushSize);
        savedBrushSize = brushSize;
        //brushSize = 0;
        drawPaint.setStyle(Paint.Style.FILL);
    }

    //ALEX!!!
    private void doneWithText()
    {
        drawPaint.setStyle(Paint.Style.STROKE);
        setBrush("point");
        setCurrentText("New Text");
        brushSize=savedBrushSize;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(brush.equals("point"))
        {
            float touchX = event.getX();
            float touchY = event.getY();
            pintarPunto(touchX, touchY, event.getAction());
            enviarStreamPunto(touchX, touchY, event.getAction());
        }
        else if(brush.equals("line"))
        {
            pintarLinea(event);
        }
        else if (brush.equals("rectangle_shape"))
        {
            pintarRectangulo(event, false);
        }
        else if (brush.equals("filled_rectangle_shape"))
        {
            pintarRectangulo(event, true);
        }
        else if (brush.equals("oval_shape"))
        {
            pintarOvalo(event, false);
        }
        else if (brush.equals("filled_oval_shape"))
        {
            pintarOvalo(event, true);
        }
        else if (brush.equals("text"))
        {
            pintarTexto(event);
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

    //ALEXX!!!
    private void pintarTexto(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                textX=event.getX();
                textY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                textX=event.getX();
                textY=event.getY();
                break;
            case MotionEvent.ACTION_UP:
                textX=event.getX();
                textY=event.getY();
                drawCanvas.drawText(currentText, textX, textY, drawPaint);
                enviarStreamTxt();
                doneWithText();
                textX = textY = -10;
                break;
        }
    }

    //ALEX!!!!!!!!
    private void pintarRectangulo(MotionEvent event,boolean filled)
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
                if (filled) drawPaint.setStyle(Paint.Style.FILL);
                drawCanvas.drawRect(posx[0], posy[0], posx[1], posy[1], drawPaint);
                if (filled) drawPaint.setStyle(Paint.Style.STROKE);
                enviarStreamRectangulo(filled);
                posx[0]=posx[1];
                posy[0]=posy[1];
                break;
        }
    }

    //ALEX!!!!!!!!!!
    private void pintarOvalo(MotionEvent event,boolean filled)
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
                if (filled) drawPaint.setStyle(Paint.Style.FILL);
                drawCanvas.drawOval(new RectF(posx[0], posy[0], posx[1], posy[1]), drawPaint);
                if (filled) drawPaint.setStyle(Paint.Style.STROKE);
                enviarStreamOvalo(filled);
                posx[0]=posx[1];
                posy[0]=posy[1];
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
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //ALEX!!!!!!!!
    private void enviarStreamRectangulo(boolean filled)
    {
        try
        {
            JSONObject obj = crearJSONRectangulo(filled);
            Servidor.enviarEvento("pintar", obj);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //ALEX!!!!!!!!
    private void enviarStreamOvalo(boolean filled)
    {
        try
        {
            JSONObject obj = crearJSONOvalo(filled);
            Servidor.enviarEvento("pintar", obj);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void enviarStreamTxt()
    {
        try
        {
            JSONObject obj = crearJSONTxt();
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

    //ALEX!!!!!!!!
    private JSONObject crearJSONRectangulo(boolean filled) throws JSONException
    {
        JSONObject obj = crearJSON();
        obj.put("x0",posx[0]);
        obj.put("x1",posx[1]);
        obj.put("y0",posy[0]);
        obj.put("y1",posy[1]);
        obj.put("filled",filled);
        return obj;
    }

    private JSONObject crearJSONTxt() throws JSONException
    {
        JSONObject obj = crearJSON();
        obj.put("text",currentText);
        obj.put("tx",textX);
        obj.put("ty",textY);
        return obj;
    }

    private JSONObject crearJSONOvalo(boolean filled) throws JSONException
    {
        JSONObject obj = crearJSON();
        obj.put("x0",posx[0]);
        obj.put("x1",posx[1]);
        obj.put("y0",posy[0]);
        obj.put("y1",posy[1]);
        obj.put("filled",filled);
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
                    setPosiciones(stream);
                    drawCanvas.drawLine(posx[0], posy[0], posx[1], posy[1], drawPaint);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPosiciones(JSONObject stream) throws JSONException 
    {
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
        setBrushSize(calcularBrushSize(stream));
        setColor(stream.getInt("color"));
        setErase(stream.getBoolean("erase"));
        setBrush(stream.getString("brush"));
    }

    private float calcularBrushSize(JSONObject stream) throws JSONException
    {
        int pixelesEnviador = stream.getInt("height") * stream.getInt("width");
        int pixeles = drawCanvas.getHeight() * drawCanvas.getWidth();
        
        if(pixelesEnviador > pixeles)
            return reglaDeTres(pixelesEnviador,pixeles,(float)stream.getDouble("brushSize"));
        else
            return (float) stream.getDouble("brushSize");
    }


    private Emitter.Listener drawListener = new Emitter.Listener()
    {
        @Override
        public void call(Object... args)
        {
            JSONObject obj = (JSONObject) args[0];
            try
            {
                if(drawCanvas!= null)
                    pintarSegunBrush(obj);
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

    //ALEXXXX!!!
    public void setCurrentText(String currentText)
    {
        this.currentText = currentText;
    }   

    private void pintarSegunBrush(JSONObject obj) throws JSONException
    {
        switch(obj.getString("brush"))
        {
            case "point":
                 pintarStreamPunto(obj);
            break;

            case "line":
                pintarStreamLinea(obj);
            break;
            case "rectangle_shape":
                pintarStreamRect(obj);
            break;
            case "filled_rectangle_shape":
                pintarStreamRect(obj);
            break;
            case "oval_shape":
                pintarStreamOval(obj);
            break;
            case "filled_oval_shape":
                pintarStreamOval(obj);
            break;
            case "text":
                pintarStreamTxt(obj);
            break;
        }
    }

    private void pintarStreamTxt(final JSONObject obj)
    {   


        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    setPropiedades(obj);
                    setDrawText(obj);
                    prepareForText();
                    drawCanvas.drawText(currentText, textX, textY, drawPaint);
                    doneWithText();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        
    }

    private void setDrawText(JSONObject obj) throws JSONException
    {
        setCurrentText(obj.getString("text"));
        textX = reglaDeTres(obj.getInt("width"),drawCanvas.getWidth(), (float) obj.getDouble("tx"));
        textY = reglaDeTres(obj.getInt("height"),drawCanvas.getHeight(), (float) obj.getDouble("ty"));
    }

    private void pintarStreamOval(final JSONObject obj)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    setPropiedades(obj);
                    setPosiciones(obj);
                    boolean filled = obj.getBoolean("filled");
                    if (filled) 
                        drawPaint.setStyle(Paint.Style.FILL);
                    
                    drawCanvas.drawOval(new RectF(posx[0], posy[0], posx[1], posy[1]), drawPaint);
                    
                    if (filled) 
                        drawPaint.setStyle(Paint.Style.STROKE);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void pintarStreamRect(final JSONObject obj)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    setPropiedades(obj);
                    setPosiciones(obj);
                    boolean filled = obj.getBoolean("filled");
                    if (filled) 
                        drawPaint.setStyle(Paint.Style.FILL);
                    
                    drawCanvas.drawRect(posx[0], posy[0], posx[1], posy[1], drawPaint);
                    
                    if (filled) 
                        drawPaint.setStyle(Paint.Style.STROKE);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }



}
