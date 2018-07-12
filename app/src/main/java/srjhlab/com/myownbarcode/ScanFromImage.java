package srjhlab.com.myownbarcode;

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
    String format, value = null;

    public void putImage(Bitmap bMap) {
        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            format = result.getBarcodeFormat().toString();
            value = result.getText();
            Log.d("TAG" , "바코드 포맷 : " + format + " 바코드 값 : " + value);
        }
        catch (Exception e) {
            Log.e("test", "Error decoding barcode", e);
        }
    }

    public String getFormat(){
        return format;
    }

    public String getValue(){
        return value;
    }
}
