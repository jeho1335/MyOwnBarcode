package srjhlab.com.myownbarcode.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ToastUtil {
    private final static String TAG = ToastUtil.class.getSimpleName();

    private Context mContext = null;
    private static ToastUtil mInstance = null;
    private static Toast mToast = null;

    private ToastUtil(Context context) {
        this.mContext = context;
        this.mToast = new Toast(context);
    }

    public static ToastUtil getInstance(Context context) {
        Log.d(TAG, "##### getInstance #####");
        if (mInstance == null) {
            mInstance = new ToastUtil(context);
        }
        return mInstance;
    }

    public void showToast(String msg) {
        Log.d(TAG, "##### showToast #####");

        mToast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
