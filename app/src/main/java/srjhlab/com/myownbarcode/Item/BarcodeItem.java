package srjhlab.com.myownbarcode.Item;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-02-08.
 */

public class BarcodeItem implements Serializable {
    private final static String TAG = BarcodeItem.class.getSimpleName();

    private Long mItemType;
    private Long mBarcodeType;
    private Long mBarcodeCardColor;
    private int mBarcodeId;
    private String mBarcodeName;
    private String mBarcodeValue;
    private byte[] mBarcodeBitmapArr;

    public void setItemType(Long type) {
        Log.d(TAG, "##### getItemType #####");
        this.mItemType = type;
    }

    public Long getItemType() {
        Log.d(TAG, "##### getItemType #####");
        return this.mItemType;
    }

    public void setBarcodeType(Long type) {
        Log.d(TAG, "##### getItemType #####");
        this.mBarcodeType = type;
    }

    public Long getBarcodeType() {
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

    public void setBarcodeValue(String value) {
        this.mBarcodeValue = value;
    }

    public String getBarcodeValue() {
        Log.d(TAG, "##### getBarcodeValue #####");
        return this.mBarcodeValue;
    }

    public void setBarcodeCardColor(Long color) {
        this.mBarcodeCardColor = color;
    }

    public Long getBarcodeCardColor() {
        Log.d(TAG, "##### getBarcodeCardColor #####");
        return this.mBarcodeCardColor;
    }

    public void setBarcodeId(int id) {
        this.mBarcodeId = id;
    }

    public int getBarcodeId() {
        Log.d(TAG, "##### getBarcodeId #####");
        return this.mBarcodeId;
    }

    public void setBarcodeBitmapArr(byte[] arr) {
        Log.d(TAG, "##### setBarcodeBitmapArr #####");
        this.mBarcodeBitmapArr = arr;
    }

    public byte[] getBarcodeBitmapArr() {
        Log.d(TAG, "##### getBarcodeBitmapArr #####");
        return this.mBarcodeBitmapArr;
    }

    public BarcodeItem(Long type, Long barcodeType, int barcodeId, String barcodeName, Long barcodeColor, String barcodeValue, byte[] bitmapArr) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n type : " + type
                + "\n barcodeType : " + barcodeType
                + "\n barcodeId " + barcodeId
                + "\n barcodeName : " + barcodeName
                + "\n barcodeColor : " + barcodeColor
                + "\n barcodeValue : " + barcodeValue
                + "\n bitmapArr : " + bitmapArr
                + "\n #####################");

        this.mItemType = type;
        this.mBarcodeType = barcodeType;
        this.mBarcodeId = barcodeId;
        this.mBarcodeName = barcodeName;
        this.mBarcodeCardColor = barcodeColor;
        this.mBarcodeValue = barcodeValue;
        this.mBarcodeBitmapArr = bitmapArr;
        //this.mBarcodeBitmap = barcodeBitmap;
    }

    public BarcodeItem(Long type, int barcodeId, String barcodeName, Long barcodeColor, String barcodeValue) {
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

    public BarcodeItem(String barcodeValue, Long barcodeType) {
        Log.d(TAG, "##### BarcodeItem #####"
                + "\n barcodeType : " + barcodeType
                + "\n barcodeValue : " + barcodeValue
                + "\n #####################");

        this.mBarcodeType = barcodeType;
        this.mBarcodeValue = barcodeValue;
    }

    public BarcodeItem() {
    }
}
