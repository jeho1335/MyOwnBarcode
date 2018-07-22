package srjhlab.com.myownbarcode.Item;

import android.util.Log;

public class CommonBarcodeItem {
    private final static String TAG = CommonBarcodeItem.class.getSimpleName();

    private String mTitle = null;
    private String mValue = null;
    private int mColor;
    private int mType;

    public CommonBarcodeItem(String name, String value, int type, int color) {
        this.mTitle = name;
        this.mValue = value;
        this.mColor = color;
        this.mType = type;
    }

    public CommonBarcodeItem(String value, int type) {
        this.mValue = value;
        this.mType = type;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public String getTitle() {
        Log.d(TAG, "##### getTitle #####");
        return this.mTitle;
    }

    public String getValue() {
        Log.d(TAG, "##### getValue #####");
        return this.mValue;
    }

    public int getColor() {
        Log.d(TAG, "##### getColor #####");
        return this.mColor;
    }

    public int getType() {
        Log.d(TAG, "##### getType #####");
        return this.mType;
    }

}

