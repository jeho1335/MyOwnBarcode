package srjhlab.com.myownbarcode.Item;

import android.util.Log;

public class BarcodePagerItem {
    private final static String TAG = BarcodePagerItem.class.getSimpleName();
    private int mType;

    public BarcodePagerItem(int type){
        this.mType = type;
    }

    public int getType(){
        Log.d(TAG, "##### getType #####e");
        return this.mType;
    }
}
