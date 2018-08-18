package srjhlab.com.myownbarcode.Utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

/**
 * Created by Administrator on 2017-06-11.
 */

public class ScanFromImage {
    private final static String TAG = ScanFromImage.class.getSimpleName();
    private String mFormat, mValue;

    public void putImage(Bitmap bMap) {
        Log.d(TAG, "##### putImage #####");
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            mFormat = result.getBarcodeFormat().toString();
            mValue = result.getText();
        } catch (Exception e) {
            Log.e("test", "Error decoding barcode", e);
        }
    }

    public String getFormat() {
        Log.d(TAG, "##### getForamt #####");
        return mFormat;
    }

    public String getValue() {
        Log.d(TAG, "##### getValue #####");
        return mValue;
    }
}
