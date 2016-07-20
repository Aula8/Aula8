package unegdevelop.paintfragments;

import android.graphics.Bitmap;


import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;

import org.vudroid.core.DecodeServiceBase;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.pdfdroid.codec.PdfContext;


import java.io.File;


/**
 * Created by Slaush on 22/06/2016.
 */

public class PDFImageView
{
    // Constantes ..
    private static final int   START_PAGE = 0;
    private static final float START_ZOOM = 1.0F;
    private static final float MIN_ZOOM = 1.0F;
    private static final float MAX_ZOOM = 2.0F;
    private static final float ZOOM_INCREMENT = 0.05f;

    // Atributos ..
    private String            pdfFileName;
    private int               pX,pY;
    private Handler           handler;
    private int               numberPage;
    private float             zoomDistance;
    private CodecPage         pdfPage;
    private Bitmap            bitmapPage;
    private ImageView         image;
    private int               height,width;
    private int               rotation;
    private DecodeServiceBase pdfDecoder;

    public PDFImageView(String file, ImageView image) throws Exception
    {

        handler = new Handler();
        if(image == null)
            throw new Exception("ERROR");
        else
            this.image = image;

        if (file == null)
            throw new Exception("ERROR");
        else
            pdfFileName = file;

        numberPage   = START_PAGE;
        zoomDistance = START_ZOOM;
        rotation = 0;
        reiniciarRect();
        bitmapPage = null;
        setContent();

    }


    private void setContent() throws Exception
    {

        pdfDecoder = new DecodeServiceBase(new PdfContext());
        pdfDecoder.setContentResolver(image.getContext().getContentResolver());
        pdfDecoder.open(Uri.fromFile(new File(pdfFileName)));
        showPage();
    }

    private  Runnable runnableShowPage = new Runnable()
    {
        @Override
            public void run()
            {

                image.setImageBitmap(null);

                pdfPage = pdfDecoder.getPage(numberPage);
                calcularDimesiones();
                bitmapPage =  createBitmap(width, height);
                image.setImageBitmap(bitmapPage);

            }
    };

    private void calcularDimesiones()
    {
        if(image.getHeight() == 0 || image.getWidth() == 0)
        {
            width  = pdfPage.getWidth();
            height = pdfPage.getHeight();
        }
        else
        {
            width  = image.getWidth();
            height = image.getHeight();
        }
    }

    private void showPage() throws Exception
    {
        handler.post(runnableShowPage);
    }

    private Bitmap createBitmap(int width, int height)
    {
        int x = (this.pX*width)/100;

        int y = (this.pY*height)/100;

        RectF rectF = new RectF(0,0, 1,1);
        Bitmap p = pdfPage.renderBitmap((int) (pdfPage.getWidth() * zoomDistance),
                                        (int) (pdfPage.getHeight() * zoomDistance), rectF);

        if(rotation > 0)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            p = Bitmap.createBitmap(p, 0, 0,
                    p.getWidth(), p.getHeight(),
                    matrix, true);
        }



        if(p.getHeight()< height)
            height = p.getHeight();

        if(p.getWidth() < width)
            width =  p.getWidth();

        if(x + width >  p.getWidth())
            x = (p.getWidth() -width);
        if(y + height > p.getHeight())
            y = (p.getHeight()-height);

        if(x == 0 && y ==0  && zoomDistance == MIN_ZOOM)
        {
            width = p.getWidth();
            height = p.getHeight();
        }

        return Bitmap.createBitmap(p, x, y, width, height);
    }

    public void zoomIn() throws Exception
    {
        if (pdfDecoder != null)
        {
            if (zoomDistance < MAX_ZOOM)
            {
                zoomDistance += ZOOM_INCREMENT;

                if (zoomDistance > MAX_ZOOM)
                    zoomDistance = MAX_ZOOM;
                showPage();
            }
        }
    }

    public void zoomOut() throws Exception
    {
        if (pdfDecoder != null)
        {
            if (zoomDistance > MIN_ZOOM)
            {
                zoomDistance -= ZOOM_INCREMENT;

                if (zoomDistance < MIN_ZOOM)
                    zoomDistance = MIN_ZOOM;
                showPage();
            }
        }
    }

    public void nextPage() throws Exception
    {
        if (pdfDecoder != null && numberPage < pdfDecoder.getPageCount()-1)
        {
            numberPage += 1;
            reiniciarRect();
            showPage();
        }
    }

    public void prevPage() throws Exception
    {
        if (pdfDecoder != null && numberPage > 0)
        {
            numberPage -= 1;
            showPage();
        }
    }

    public void gotoPage(int page) throws Exception
    {
        if (pdfDecoder != null && page >= 0 && page < pdfDecoder.getPageCount()-1)
        {
            numberPage = page;
            showPage();
        }
    }

    public int getPage()
    {
        return numberPage;
    }

    public float getZoom()
    {
        return zoomDistance;
    }

    public Bitmap getBitmap()
    {
        return bitmapPage;
    }

    private void reiniciarRect()
    {
        zoomDistance = MIN_ZOOM;
        pX = 0;
        pY = 0;
    }

    public void mover(int pX, int pY) throws Exception
    {
        this.pX += pX;

        if(this.pX  < 0)
            this.pX = 0;

        this.pY += pY;

        if(this.pY  < 0)
            this.pY = 0;

        showPage();
    }

    public int getNumberPages()
    {
        if(pdfDecoder != null)
            return pdfDecoder.getPageCount();
        else
            return 0;
    }

    public void rotar() throws Exception
    {
        if(rotation == 270)
            rotation = 0;
        else
            rotation+=90;
        showPage();
    }
}
