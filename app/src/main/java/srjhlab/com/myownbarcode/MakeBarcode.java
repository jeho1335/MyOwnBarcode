package srjhlab.com.myownbarcode;

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

import srjhlab.com.myownbarcode.Utils.ConstVariables;

/**
 * Created by Administrator on 2017-05-12.
 */

public class MakeBarcode {
    private final static String TAG = MakeBarcode.class.getSimpleName();
    private static MakeBarcode mInstance = null;

    public static MakeBarcode getInstance(){
        if(mInstance != null){
            return mInstance;
        }else{
            return new MakeBarcode();
        }
    }

    public MakeBarcode(){
        mInstance = this;
    }

    public  Bitmap makeBarcode(int type, String value){
        Bitmap bmp = null;

        switch (type){
            case ConstVariables.CODE_39:
                Log.d(TAG, "##### makeBarcode ##### CODE_39");
                bmp = makeCODE39(value);
                break;
            case ConstVariables.CODE_93:
                Log.d(TAG, "##### makeBarcode ##### CODE_93");
                bmp = makeCODE93(value);
                break;
            case ConstVariables.CODE_128:
                Log.d(TAG, "##### makeBarcode ##### CODE_128");
                bmp = makeCODE128(value);
                break;
            case ConstVariables.EAN_8:
                Log.d(TAG, "##### makeBarcode ##### EAN_8");
                bmp = makeEAN8(value);
                break;
            case ConstVariables.EAN_13:
                Log.d(TAG, "##### makeBarcode ##### EAN_13");
                bmp = makeeEAN13(value);
                break;
            case ConstVariables.PDF_417:
                Log.d(TAG, "##### makeBarcode ##### PDF_417");
                bmp = makePDF417(value);
                break;
            case ConstVariables.UPC_A:
                Log.d(TAG, "##### makeBarcode ##### UPC_A");
                bmp = makeUPCA(value);
                break;
            case ConstVariables.CODABAR:
                Log.d(TAG, "##### makeBarcode ##### CODABAR");
                bmp = makeCODABAR(value);
                break;
            case ConstVariables.ITF:
                Log.d(TAG, "##### makeBarcode ##### ITF");
                bmp = makeITF(value);
                break;
            case ConstVariables.QR_CODE:
                Log.d(TAG, "##### makeBarcode ##### QR_CODE");
                bmp = makeQRCODE(value);
                break;
            case ConstVariables.MAXI_CODE:
                Log.d(TAG, "##### makeBarcode ##### MAXI_CODE");
                bmp = makeMAXICODE(value);
                break;
            case ConstVariables.AZTEC:
                Log.d(TAG, "##### makeBarcode ##### AZTEC");
                bmp = makeAZTEC(value);
                break;
        }

       return  bmp;
    }

    private Bitmap makeCODE39(String value){
        Log.d(TAG, "##### makeCODE39 #####");
        Bitmap bmp = null;
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

        return bmp;
    }

    private Bitmap makeCODE93(String value){
        Log.d(TAG, "##### makeCODE93 #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeCODE128(String value){
        Log.d(TAG, "##### makeCODE128 #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeEAN8(String value){
        Log.d(TAG, "##### makeEAN8 #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeeEAN13(String value){
        Log.d(TAG, "##### makeEAN13 #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makePDF417(String value){
        Log.d(TAG, "##### maekPDF417 #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeUPCA(String value){
        Log.d(TAG, "##### makeUPCA #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeUPCE(String value){
        Log.d(TAG, "##### makeUPCE #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeCODABAR(String value){
        Log.d(TAG, "##### makeCODABAR #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeITF(String value){
        Log.d(TAG, "##### makeITF #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeQRCODE(String value){
        Log.d(TAG, "##### makeQRCODE #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeMAXICODE(String value){
        Log.d(TAG, "##### makeMAXICODE #####");
        Bitmap bmp = null;

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

        return bmp;
    }

    private Bitmap makeAZTEC(String value){
        Log.d(TAG, "##### makeAZTEC #####");
        Bitmap bmp = null;

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

        return bmp;
    }
}
