package srjhlab.com.myownbarcode.Module.MyBarcode

import android.content.Context
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class MyBarcodePresenter(context: Context, barcodeView: MyBarcode.view) : MyBarcode.presenter {
    val TAG = this.javaClass.simpleName
    var mContext = context
    var mBarcodeView = barcodeView
    var mPreferenceManager = PreferencesManager

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

    override fun requestBarcodeList(recyclerView : RecyclerView, adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestBarcodeList #####")
        val mItems = PreferencesManager.loadBarcodeItemList(mContext)
        val recyclerViewContext = recyclerView.context
        val animationController = AnimationUtils.loadLayoutAnimation(recyclerViewContext, R.anim.layout_animation_fall_down)
        recyclerView.layoutAnimation = animationController
        adapter.setItems(mItems)
        adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun requestAddBarcode(adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestAddBarcode #####")
        adapter.addItem(item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestDeleteBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestDeleteBarcode #####")
        adapter.deleteItem(pos, item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestModifyBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestModifyBarcode #####")
        adapter.updateItem(pos, item)
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestSaveBarcodeList(adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestSaveBarcodeList #####")
        mPreferenceManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

}