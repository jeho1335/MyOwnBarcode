package srjhlab.com.myownbarcode.Item;

import android.util.Log;

public class BarcodePagerItem {
    private final static String TAG = BarcodePagerItem.class.getSimpleName();
    private int mType;
    private String mName;

    public BarcodePagerItem(int type, String name){
        this.mType = type;
        this.mName = name;
    }

    public int getType(){
        Log.d(TAG, "##### getType #####e");
        return this.mType;
    }

    public String getName(){
        Log.d(TAG, "##### getName #####");
        return this.mName;
    }
}
