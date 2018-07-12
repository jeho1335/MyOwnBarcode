package srjhlab.com.myownbarcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.ITFWriter;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.oned.UPCEWriter;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by Administrator on 2017-05-12.
 */

public class MakeBarcode {
    private Context context;
    GetByteArrayFromDrawable convert = new GetByteArrayFromDrawable();


    public Bitmap MakeBarcode(String value, String format){
        Bitmap bmp = null;

        if(format.equals("CODE_39")){
            com.google.zxing.Writer c9 = new Code39Writer();
            bmp = Bitmap.createBitmap(1800, 4500, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.CODE_39, 1800, 450);
                bmp = Bitmap.createBitmap(1800, 450, Bitmap.Config.ARGB_8888);
                Log.d("TAG" , "MakeBarcode : " + bmp.getRowBytes());

                for(int i = 0; i < 1800; i++) {
                    for (int j = 0; j < 450; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("CODE_93")){
            com.google.zxing.Writer c9 = new Code93Writer();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.CODE_93, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("CODE_128")){
            com.google.zxing.Writer c9 = new Code128Writer();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.CODE_128, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("EAN_8")){
            com.google.zxing.Writer c9 = new EAN8Writer();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.EAN_8, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("EAN_13")){
            com.google.zxing.Writer c9 = new EAN13Writer();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.EAN_13, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("PDF_417")){
            com.google.zxing.Writer c9 = new PDF417Writer();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.PDF_417, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("UPC_A")){
            com.google.zxing.Writer c9 = new UPCAWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.UPC_A, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("UPC_E")){
            com.google.zxing.Writer c9 = new UPCEWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.UPC_E, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("CODABAR")){
            com.google.zxing.Writer c9 = new CodaBarWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.CODABAR, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("ITF")){
            com.google.zxing.Writer c9 = new ITFWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.ITF, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("QR_CODE")){
            com.google.zxing.Writer c9 = new QRCodeWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.QR_CODE, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("DATA_MATRIX")){
            com.google.zxing.Writer c9 = new DataMatrixWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.DATA_MATRIX, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }

        else if(format.equals("AZTEC")){
            com.google.zxing.Writer c9 = new AztecWriter();
            bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);
            try{
                BitMatrix bm = c9.encode(value, BarcodeFormat.AZTEC, 800, 200);
                bmp = Bitmap.createBitmap(800, 200, Bitmap.Config.ARGB_8888);

                for(int i = 0; i < 800; i++) {
                    for (int j = 0; j < 200; j++) {
                        bmp.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);
                    }
                }
            }catch (WriterException e){
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
       return  bmp;
    }
}
