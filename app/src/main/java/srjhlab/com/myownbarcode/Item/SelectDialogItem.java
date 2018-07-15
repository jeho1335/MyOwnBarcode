package srjhlab.com.myownbarcode.Item;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class SelectDialogItem {
    private final static String TAG = SelectDialogItem.class.getSimpleName();

    public final static int INPUT_SELF = 0;
    public final static int INPUT_SCAN = 1;
    public final static int INPUT_IMAGE = 2;

    private int mItemType;

    public SelectDialogItem(int type){
        this.mItemType = type;
    }

    public void setItemType(int type){
        Log.d(TAG, "##### setItemType ##### type : " + type);
        this.mItemType = type;
    }

    public int getItemType(){
        Log.d(TAG,"##### getItemType #####");
        return this.mItemType;
    }
}
