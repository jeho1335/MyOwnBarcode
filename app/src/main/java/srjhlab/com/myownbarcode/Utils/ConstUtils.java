package srjhlab.com.myownbarcode.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import srjhlab.com.myownbarcode.R;

public class ConstUtils {
    private final static String TAG = ConstUtils.class.getSimpleName();

    public static Drawable getSampleBarcode(Context context, int pos){
        Log.d(TAG, "##### getSampleBarcode ##### pos : " + pos);
        Drawable resDrawable = null;
        switch (pos){
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
}
