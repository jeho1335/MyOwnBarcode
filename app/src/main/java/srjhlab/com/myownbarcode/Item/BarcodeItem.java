package srjhlab.com.myownbarcode.Item;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by Administrator on 2017-02-08.
 */

public class BarcodeItem {
    private final static String TAG = BarcodeItem.class.getSimpleName();

    private int mItemType;
    private int mBarcodeType;
    private int mBarcodeCardColor;
    private int mBarcodeId;
    private Bitmap mBarcodeBitmap;
    private String mBarcodeName;
    private String mBarcodeValue;

    public int getItemType() {
        Log.d(TAG, "##### getItemType #####");
        return this.mItemType;
    }

    public int getBarcodeType(){
        Log.d(TAG, "##### getBarcodeType #####");
        return this.mBarcodeType;
    }

    public Bitmap getBarcodeBitmap() {
        Log.d(TAG, "##### getBarcodeBitmap #####");
        return this.mBarcodeBitmap;
    }

    public String getBarcodeName() {
        Log.d(TAG, "##### getBarcodeName #####");
        return this.mBarcodeName;
    }

    public String getBarcodeValue() {
        Log.d(TAG, "##### getBarcodeValue #####");
        return this.mBarcodeValue;
    }

    public int getBarcodeCardColor() {
        Log.d(TAG, "##### getBarcodeCardColor #####");
        return this.mBarcodeCardColor;
    }

    public int getBarcodeId() {
        Log.d(TAG, "##### getBarcodeId #####");
        return this.mBarcodeId;
    }

    public BarcodeItem(int type, int barcodeType, int barcodeId, String barcodeName, int barcodeColor, String barcodeValue, Bitmap barcodeBitmap) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n type : " + type
                + "\n barcodeType : " + barcodeType
                + "\n barcodeId " + barcodeId
                + "\n barcodeName : " + barcodeName
                + "\n barcodeColor : " + barcodeColor
                + "\n barcodeValue : " + barcodeValue
                + "\n #####################");

        this.mItemType = type;
        this.mBarcodeType = barcodeType;
        this.mBarcodeId = barcodeId;
        this.mBarcodeName = barcodeName;
        this.mBarcodeCardColor = barcodeColor;
        this.mBarcodeValue = barcodeValue;
        this.mBarcodeBitmap = barcodeBitmap;
    }

    public BarcodeItem(int type, int barcodeId, String barcodeName, int barcodeColor, String barcodeValue, Bitmap barcodeBitmap) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n type : " + type
                + "\n barcodeId " + barcodeId
                + "\n barcodeName : " + barcodeName
                + "\n barcodeColor : " + barcodeColor
                + "\n barcodeValue : " + barcodeValue
                + "\n #####################");

        this.mItemType = type;
        this.mBarcodeId = barcodeId;
        this.mBarcodeName = barcodeName;
        this.mBarcodeCardColor = barcodeColor;
        this.mBarcodeValue = barcodeValue;
        this.mBarcodeBitmap = barcodeBitmap;
    }

    public BarcodeItem(String barcodeValue, int barcodeType) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n barcodeType : " + barcodeType
                + "\n barcodeValue : " + barcodeValue
                + "\n #####################");

        this.mBarcodeType = barcodeType;
        this.mBarcodeValue = barcodeValue;
    }
}
