package unegdevelop.paintfragments;

import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Handler;
import android.widget.ImageView;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFImage;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PDFPaint;
import com.sun.pdfview.PDFRenderer;
import com.sun.pdfview.colorspace.PDFColorSpace;
import com.sun.pdfview.decrypt.PDFAuthenticationFailureException;


import net.sf.andpdf.nio.ByteBuffer;
import net.sf.andpdf.refs.HardReference;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by Slaush on 22/06/2016.
 */

public class PDFImageView
{
    // Constantes ..
    private static final int STARTPAGE = 1;
    private static final float STARTZOOM = 1.0F;
    private static final float MIN_ZOOM = 1.0F;
    private static final float MAX_ZOOM = 2.0F;
    private static final float ZOOM_INCREMENT = 0.05f;

    // Atributos ..
    private String pdffilename;
    private PDFColorSpace colorSpace;
    private int pX,pY;
    private PDFFile mPdfFile;
    private Handler handler;
    private int mPage;
    private float mZoom;
    private PDFPage mPdfPage;
    private Bitmap pageBitmap;
    private ImageView img;
    private int height,width;

    public PDFImageView(String file, ImageView img) throws Exception
    {
        PDFImage.sShowImages = true;
        PDFPaint.s_doAntiAlias = true;
        HardReference.sKeepCaches= false;





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
        reiniciarRect();
        pageBitmap = null;
        setContent();

    }


    private void setContent() throws Exception
    {
        parsePDF(pdffilename);
        showPage(mPage);
    }

    private void parsePDF(String filename) throws IOException
    {
        File f = new File(filename);
        long len = f.length();
        if (len == 0)
            throw  new PDFAuthenticationFailureException("Error");
        else
        {
            openFile(f);
        }
    }


    public void openFile(File file) throws IOException
    {
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        FileChannel channel = raf.getChannel();

        ByteBuffer bb = ByteBuffer.NEW(channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size()));
        mPdfFile = new PDFFile(bb);
    }

    private void showPage(final int page) throws Exception
    {
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {

                //liberarBitmap();
                img.setImageBitmap(null);
                if (mPdfPage == null || mPdfPage.getPageNumber() != page)
                {
                    mPdfPage = mPdfFile.getPage(page, true);
                }

                if(img.getHeight() == 0 || img.getWidth() == 0)
                {
                    width = (int)mPdfPage.getWidth();
                    height = (int) mPdfPage.getHeight();
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

        Bitmap p = mPdfPage.getImage((int)(mPdfPage.getWidth()*mZoom),
                (int)(mPdfPage.getHeight()*mZoom),
                null, true, true);

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
            width = (int)mPdfPage.getWidth();
            height = (int)mPdfPage.getHeight();
        }

        return Bitmap.createBitmap(p,x,y,width,height);

    }

    private void liberarBitmap()
    {
        if(pageBitmap != null  && !pageBitmap.isRecycled())
        {
            pageBitmap.recycle();
            pageBitmap = null;
        }
    }

    public void zoomIn() throws Exception
    {
        if (mPdfFile != null)
        {
            if (mZoom < MAX_ZOOM)
            {
                mZoom += ZOOM_INCREMENT;

                if (mZoom > MAX_ZOOM)
                    mZoom = MAX_ZOOM;
                showPage(mPage);
            }
        }
    }

    public void zoomOut() throws Exception
    {
        if (mPdfFile != null)
        {
            if (mZoom > MIN_ZOOM)
            {
                mZoom -= ZOOM_INCREMENT;

                if (mZoom < MIN_ZOOM)
                    mZoom = MIN_ZOOM;
                showPage(mPage);
            }
        }
    }

    public void nextPage() throws Exception
    {
        if (mPdfFile != null && mPage < mPdfFile.getNumPages())
        {
            mPage += 1;
            reiniciarRect();
            showPage(mPage);
        }
    }

    public void prevPage() throws Exception
    {
        if (mPdfFile != null && mPage > 1)
        {
            mPage -= 1;
            showPage(mPage);
        }
    }

    public void gotoPage(int page) throws Exception
    {
        if (mPdfFile != null && page > 1 && page < mPdfFile.getNumPages())
        {
            mPage = page;
            showPage(mPage);
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

        showPage(mPage);
    }

    public int getNumberPages()
    {
        if(mPdfFile != null)
            return mPdfFile.getNumPages();
        else
            return 0;
    }
}
