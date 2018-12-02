package srjhlab.com.myownbarcode.Module.MyBarcode

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.MakeBarcode
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class MyBarcodePresenter(context: Context, val mView: MyBarcode.view) : MyBarcode.presenter {
    val TAG = this.javaClass.simpleName
    var mContext = context

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

    @SuppressLint("CheckResult")
    override fun requestBarcodeList(recyclerView: RecyclerView, adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestBarcodeList #####")
        val mItems = PreferencesManager.loadBarcodeItemList(mContext)

        Maybe.fromCallable<MutableList<BarcodeItem>> {
            MakeBarcode().initBarcodeList(mItems){
                mView.onResultProgress("(${it+1}/${mItems.size})")
            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is MutableList<BarcodeItem>) {
                        val recyclerViewContext = recyclerView.context
                        val animationController = AnimationUtils.loadLayoutAnimation(recyclerViewContext, R.anim.layout_animation_fall_down)
                        recyclerView.layoutAnimation = animationController
                        adapter.setItems(it)
                        adapter.notifyDataSetChanged()
                        recyclerView.scheduleLayoutAnimation()
                        PreferencesManager.saveBarcodeItemList(mContext, it)
                        mView.onResultBarcodeList(true, R.string.string_wait_init_barcode)
                    } else {
                        mView.onResultBarcodeList(false, R.string.string_unknown_error)
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    it.printStackTrace()
                    mView.onResultBarcodeList(false, R.string.string_unknown_error)

                }, {
                    Log.d(TAG, "##### onCompletion #####")
                    mView.onResultBarcodeList(false, R.string.string_unknown_error)
                })
    }

    override fun requestAddBarcode(adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestAddBarcode #####")
        adapter.addItem(item)
        PreferencesManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestDeleteBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestDeleteBarcode #####")
        adapter.deleteItem(pos, item)
        PreferencesManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestModifyBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestModifyBarcode #####")
        adapter.updateItem(pos, item)
        PreferencesManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

    override fun requestSaveBarcodeList(adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestSaveBarcodeList #####")
        PreferencesManager.saveBarcodeItemList(mContext, adapter.getItems())
    }

}