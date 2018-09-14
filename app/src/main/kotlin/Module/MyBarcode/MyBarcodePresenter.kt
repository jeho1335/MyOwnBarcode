package srjhlab.com.myownbarcode.Module.MyBarcode

import android.content.Context
import android.util.Log
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class MyBarcodePresenter(context : Context, barcodeView : MyBarcode.view) : MyBarcode.presenter {
    val TAG = this.javaClass.simpleName
    var mContext = context
    var mBarcodeView = barcodeView
    var mPreferenceManager = PreferencesManager

    override fun requestBarcodeList() {
        Log.d(TAG, "##### requestBarcodeList #####")
        return mBarcodeView.onResultBarcodeList(mPreferenceManager.loadBarcodeItemList(mContext))
    }

    override fun requestAddBarcode(adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestAddBarcode #####")
        adapter.addItem(item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestDeleteBarcode(pos : Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestDeleteBarcode #####")
        adapter.deleteItem(pos, item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestModifyBarcode(pos : Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestModifyBarcode #####")
        adapter.updateItem(pos, item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestSaveBarcodeList(adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestSaveBarcodeList #####")
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

}