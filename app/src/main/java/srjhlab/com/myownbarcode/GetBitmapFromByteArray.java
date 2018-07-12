package srjhlab.com.myownbarcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Administrator on 2017-05-11.
 */

public class GetBitmapFromByteArray {

    public Bitmap convertImg(byte[] b){
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }
}
