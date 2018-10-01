package srjhlab.com.myownbarcode.Item;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class BarcodeItem implements Serializable {
    private final static String TAG = BarcodeItem.class.getSimpleName();

    private int mItemType;
    private int mBarcodeType;
    private int mBarcodeCardColor;
    private int mBarcodeId;
    private String mBarcodeName;
    private String mBarcodeValue;

    public void setItemType(int type) {
        Log.d(TAG, "##### getItemType #####");
        this.mItemType = type;
    }

    public int getItemType() {
        Log.d(TAG, "##### getItemType #####");
        return this.mItemType;
    }

    public void setBarcodeType(int type) {
        Log.d(TAG, "##### getItemType #####");
        this.mBarcodeType = type;
    }

    public int getBarcodeType(){
        Log.d(TAG, "##### getBarcodeType #####");
        return this.mBarcodeType;
    }

    public void setBarcodeName(String name) {
        Log.d(TAG, "##### getItemType #####");
        this.mBarcodeName = name;
    }

    public String getBarcodeName() {
        Log.d(TAG, "##### getBarcodeName #####");
        return this.mBarcodeName;
    }

    public void setBarcodeValue(String value){
        this.mBarcodeValue = value;
    }

    public String getBarcodeValue() {
        Log.d(TAG, "##### getBarcodeValue #####");
        return this.mBarcodeValue;
    }

    public void setBarcodeCardColor(int color){
        this.mBarcodeCardColor = color;
    }

    public int getBarcodeCardColor() {
        Log.d(TAG, "##### getBarcodeCardColor #####");
        return this.mBarcodeCardColor;
    }

    public void setBarcodeId(int id){
        this.mBarcodeId = id;
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
        //this.mBarcodeBitmap = barcodeBitmap;
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
        //this.mBarcodeBitmap = barcodeBitmap;
    }

    public BarcodeItem(String barcodeValue, int barcodeType) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n barcodeType : " + barcodeType
                + "\n barcodeValue : " + barcodeValue
                + "\n #####################");

        this.mBarcodeType = barcodeType;
        this.mBarcodeValue = barcodeValue;
    }

    public BarcodeItem(){}
}
