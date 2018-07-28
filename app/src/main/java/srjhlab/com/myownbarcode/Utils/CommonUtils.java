package srjhlab.com.myownbarcode.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;

import srjhlab.com.myownbarcode.R;

public class CommonUtils {
    private final static String TAG = CommonUtils.class.getSimpleName();

    public static Drawable getSampleBarcode(Context context, int pos) {
        Log.d(TAG, "##### getSampleBarcode ##### pos : " + pos);
        Drawable resDrawable = null;
        switch (pos) {
            case 0:
                resDrawable = context.getResources().getDrawable(R.drawable.code_code39);
                break;
            case 1:
                resDrawable = context.getResources().getDrawable(R.drawable.code_code93);
                break;
            case 2:
                resDrawable = context.getResources().getDrawable(R.drawable.code_code128);
                break;
            case 3:
                resDrawable = context.getResources().getDrawable(R.drawable.code_ean8);
                break;
            case 4:
                resDrawable = context.getResources().getDrawable(R.drawable.code_ean13);
                break;
            case 5:
                resDrawable = context.getResources().getDrawable(R.drawable.code_pdf417);
                break;
            case 6:
                resDrawable = context.getResources().getDrawable(R.drawable.code_upca);
                break;
            case 7:
                resDrawable = context.getResources().getDrawable(R.drawable.code_codabar);
                break;
            case 8:
                resDrawable = context.getResources().getDrawable(R.drawable.code_itf);
                break;
            case 9:
                resDrawable = context.getResources().getDrawable(R.drawable.code_qrcode);
                break;
            case 10:
                resDrawable = context.getResources().getDrawable(R.drawable.code_maxicode);
                break;
            case 11:
                resDrawable = context.getResources().getDrawable(R.drawable.code_aztec);
                break;
        }
        return resDrawable;
    }

    public static int convertBarcodeType(Context context, String type) {
        int convertVal = -1;
        if (context.getString(R.string.var_code_39).equals(type)) {
            convertVal =  ConstVariables.CODE_39;
        } else if (context.getString(R.string.var_code_93).equals(type)) {
            convertVal =  ConstVariables.CODE_93;
        } else if (context.getString(R.string.var_code_128).equals(type)) {
            convertVal =  ConstVariables.CODE_128;
        } else if (context.getString(R.string.var_ean_8).equals(type)) {
            convertVal =  ConstVariables.EAN_8;
        } else if (context.getString(R.string.var_ean_13).equals(type)) {
            convertVal =  ConstVariables.EAN_13;
        } else if (context.getString(R.string.var_pdf_417).equals(type)) {
            convertVal =  ConstVariables.PDF_417;
        } else if (context.getString(R.string.var_upc_a).equals(type)) {
            convertVal =  ConstVariables.UPC_A;
        } else if (context.getString(R.string.var_upc_e).equals(type)) {
            //TBD
        } else if (context.getString(R.string.var_codabar).equals(type)) {
            convertVal =  ConstVariables.CODABAR;
        } else if (context.getString(R.string.var_qr_code).equals(type)) {
            convertVal =  ConstVariables.QR_CODE;
        } else if (context.getString(R.string.var_aztec).equals(type)) {
            convertVal =  ConstVariables.AZTEC;
        }

        return convertVal;
    }

    public static Bitmap convertImg(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    public static byte[] getByteArrayFromDrawable(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        Log.d("TAG", "drawable 이미지를 byte로 변환 : " + data);
        return data;
    }
}
