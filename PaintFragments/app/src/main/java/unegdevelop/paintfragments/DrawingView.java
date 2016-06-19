package unegdevelop.paintfragments;

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

/**
 * Created by MujicaM on 11/05/2016.
 */
public class DrawingView extends View {

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

    private void setupDrawing() {
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


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//draw view

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
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch

        if(brush.equals("point")){
            float touchX = event.getX();
            float touchY = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawPath.moveTo(touchX, touchY);
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                    break;
                default:
                    return false;
            }
        }else if(brush.equals("line")){
            switch (event.getAction()) {
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
                    posx[0]=posx[1];
                    posy[0]=posy[1];

                    break;
                default:
                    return false;
            }
        }


        invalidate();
        return true;
    }

    public void setColor(String newColor) {
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize) {
//update size

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

    public void setErase(boolean isErase) {
//set erase true or false
        erase = isErase;
        if (erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);

    }

    public String getBrush (){
        return brush;
    }

    public void setBrush (String typeBrush){
        brush=typeBrush;
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

}
