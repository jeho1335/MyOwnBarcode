package srjhlab.com.myownbarcode.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
        Log.d(TAG, "##### convertBarcodeType #####");
        int convertVal = -1;
        if (context.getString(R.string.var_code_39).equals(type)) {
            convertVal = ConstVariables.CODE_39;
        } else if (context.getString(R.string.var_code_93).equals(type)) {
            convertVal = ConstVariables.CODE_93;
        } else if (context.getString(R.string.var_code_128).equals(type)) {
            convertVal = ConstVariables.CODE_128;
        } else if (context.getString(R.string.var_ean_8).equals(type)) {
            convertVal = ConstVariables.EAN_8;
        } else if (context.getString(R.string.var_ean_13).equals(type)) {
            convertVal = ConstVariables.EAN_13;
        } else if (context.getString(R.string.var_pdf_417).equals(type)) {
            convertVal = ConstVariables.PDF_417;
        } else if (context.getString(R.string.var_upc_a).equals(type)) {
            convertVal = ConstVariables.UPC_A;
        } else if (context.getString(R.string.var_upc_e).equals(type)) {
            //TBD
        } else if (context.getString(R.string.var_codabar).equals(type)) {
            convertVal = ConstVariables.CODABAR;
        } else if (context.getString(R.string.var_qr_code).equals(type)) {
            convertVal = ConstVariables.QR_CODE;
        } else if (context.getString(R.string.var_aztec).equals(type)) {
            convertVal = ConstVariables.AZTEC;
        }

        return convertVal;
    }

    public static String convertBarcodeType(Context context, int type) {
        Log.d(TAG, "##### convertBarcodeType #####");
        String convertVal = null;

        if (type == ConstVariables.CODE_39) {
            convertVal = context.getResources().getString(R.string.var_code_39);
        } else if (type == ConstVariables.CODE_93) {
            convertVal = context.getResources().getString(R.string.var_code_93);
        } else if (type == ConstVariables.CODE_128) {
            convertVal = context.getResources().getString(R.string.var_code_128);
        } else if (type == ConstVariables.EAN_8) {
            convertVal = context.getResources().getString(R.string.var_ean_8);
        } else if (type == ConstVariables.EAN_13) {
            convertVal = context.getResources().getString(R.string.var_ean_13);
        } else if (type == ConstVariables.PDF_417) {
            convertVal = context.getResources().getString(R.string.var_pdf_417);
        } else if (type == ConstVariables.UPC_A) {
            convertVal = context.getResources().getString(R.string.var_upc_a);
        } else if (type == ConstVariables.CODABAR) {
            convertVal = context.getResources().getString(R.string.var_codabar);
        } else if (type == ConstVariables.QR_CODE) {
            convertVal = context.getResources().getString(R.string.var_qr_code);
        } else if (type == ConstVariables.AZTEC) {
            convertVal = context.getResources().getString(R.string.var_aztec);
        }

        return convertVal;
    }

    public static Bitmap convertImg(byte[] b) {
        Log.d(TAG, "##### convertImg #####");
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

    public static String[] splitStringEvery(String s, int interval) {
        Log.d(TAG, "##### SplitStringEvert #####");
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        }
        result[lastIndex] = s.substring(j);

        return result;
    }

    public static byte[] getByteArrayFromDrawable(Drawable d) {
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();
        Log.d("TAG", "drawable 이미지를 byte로 변환 : " + data);
        return data;
    }

    public static void shareBitmapToApps(Context context, Bitmap bitmap) {
        Log.d(TAG ,"##### shareBitmapToApps #####");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        Uri paresUri = Uri.parse(path);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, paresUri);

        try {
            context.startActivity(Intent.createChooser(i, "My Profile ..."));
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static Bitmap viewToBitmap(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}
