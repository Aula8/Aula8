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
    private static final int STARTPAGE = 0;
    private static final float STARTZOOM = 1.0F;
    private static final float MIN_ZOOM = 1.0F;
    private static final float MAX_ZOOM = 2.0F;
    private static final float ZOOM_INCREMENT = 0.05f;

    // Atributos ..
    private String pdffilename;
    private int pX,pY;
    private Handler handler;
    private int mPage;
    private float mZoom;
    private CodecPage mPdfPage;
    private Bitmap pageBitmap;
    private ImageView img;
    private int height,width;
    int rotacion;

    //Prueba con la Lib Nueva
    DecodeServiceBase pdfDecoder;

    public PDFImageView(String file, ImageView img) throws Exception
    {

        handler = new Handler();
        if(img == null)
            throw new Exception("ERROR");
        else
            this.img = img;

        if (file == null)
            throw new Exception("ERROR");
        else
            pdffilename = file;

        mPage = STARTPAGE;
        mZoom = STARTZOOM;
        rotacion = 0;
        reiniciarRect();
        pageBitmap = null;
        setContent();

    }


    private void setContent() throws Exception
    {

        pdfDecoder = new DecodeServiceBase(new PdfContext());
        pdfDecoder.setContentResolver(img.getContext().getContentResolver());
        pdfDecoder.open(Uri.fromFile(new File(pdffilename)));
        showPage();
    }


    private void showPage() throws Exception
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {

                img.setImageBitmap(null);

                mPdfPage = pdfDecoder.getPage(mPage);


                if(img.getHeight() == 0 || img.getWidth() == 0)
                {
                    width =  mPdfPage.getWidth();
                    height = mPdfPage.getHeight();
                }
                else
                {
                    width = img.getWidth();
                    height = img.getHeight();
                }

                pageBitmap =  crearBitmap(width, height);
                img.setImageBitmap(pageBitmap);

            }
        });

    }

    private Bitmap crearBitmap(int width, int height)
    {
        int x = (this.pX*width)/100;

        int y = (this.pY*height)/100;

        RectF rectF = new RectF(0,0, 1,1);
        Bitmap p = mPdfPage.renderBitmap((int) (mPdfPage.getWidth() * mZoom),
                                         (int) (mPdfPage.getHeight() * mZoom), rectF);

        if(rotacion > 0)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotacion);
            p = Bitmap.createBitmap(p, 0, 0,
                    p.getWidth(), p.getHeight(),
                    matrix, true);
        }



        if(p.getHeight()< height)
            height = p.getHeight();

        if(p.getWidth() < width)
            width = p.getWidth();

        if(x + width > p.getWidth())
            x = (p.getWidth()-width);
        if(y + height > p.getHeight())
            y = (p.getHeight() - height);

        if(x == 0 && y ==0  && mZoom == MIN_ZOOM)
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
            if (mZoom < MAX_ZOOM)
            {
                mZoom += ZOOM_INCREMENT;

                if (mZoom > MAX_ZOOM)
                    mZoom = MAX_ZOOM;
                showPage();
            }
        }
    }

    public void zoomOut() throws Exception
    {
        if (pdfDecoder != null)
        {
            if (mZoom > MIN_ZOOM)
            {
                mZoom -= ZOOM_INCREMENT;

                if (mZoom < MIN_ZOOM)
                    mZoom = MIN_ZOOM;
                showPage();
            }
        }
    }

    public void nextPage() throws Exception
    {
        if (pdfDecoder != null && mPage < pdfDecoder.getPageCount()-1)
        {
            mPage += 1;
            reiniciarRect();
            showPage();
        }
    }

    public void prevPage() throws Exception
    {
        if (pdfDecoder != null && mPage > 0)
        {
            mPage -= 1;
            showPage();
        }
    }

    public void gotoPage(int page) throws Exception
    {
        if (pdfDecoder != null && page > 1 && page < pdfDecoder.getPageCount()-1)
        {
            mPage = page;
            showPage();
        }
    }

    public int getPage()
    {
        return mPage;
    }

    public float getZoom()
    {
        return mZoom;
    }

    public Bitmap getBitmap()
    {
        return pageBitmap;
    }

    private void reiniciarRect()
    {
        mZoom = MIN_ZOOM;
        pX = 0;
        pY = 0;
    }

    public void mover(int pX, int pY) throws Exception
    {
        this.pX += pX;

        if(this.pX < 0)
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
        if(rotacion == 270)
            rotacion = 0;
        else
            rotacion+=90;
        showPage();
    }
}
