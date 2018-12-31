package srjhlab.com.myownbarcode.Module.MyBarcode

import android.util.Log
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Model.RealmClient
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.MakeBarcode

class MyBarcodePresenter(private val view: MyBarcodeFragment) : MyBarcode.presenter {
    val TAG = this.javaClass.simpleName
    private val mView = view as MyBarcode.view

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
    }

    override fun requestRealmBarcodeList(recyclerView: RecyclerView, adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestRealmBarcodeList #####")
        val resultList: MutableList<BarcodeItem> = ArrayList()
        RealmClient
                .readMyBarcodeRealm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    for ((index, value) in it.withIndex()) {
                        Log.d(TAG, "##### requestRealmBarcodeList onSuccess arr : ${value.barcodeBitmapArr}#####")
                        BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE,
                                value.barcodeType,
                                value.barcodeUid,
                                value.barcodeName,
                                value.barcodeCardColor,
                                value.barcodeValue,
                                value.barcodeBitmapArr)
                                .let { item -> resultList.add(item) }
                    }
                    BarcodeItem(ConstVariables.ITEM_TYPE_EMPTY, 0, "새 바코드 추가", 0L, " ")
                            .let { emptyItem -> resultList.add(emptyItem) }
                    initBitmapByteArray(resultList, recyclerView, adapter)
                }, {
                    Log.d(TAG, "##### requestRealmBarcodeList ##### onFailure")

                }, {
                    Log.d(TAG, "##### requestRealmBarcodeList ##### onComplete")

                })
                .apply {
                    view.disposables.add(this)
                }
    }

    private fun initBitmapByteArray(list: MutableList<BarcodeItem>, recyclerView: RecyclerView, adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### initBitmapByteArray #####")
        Maybe.fromCallable<MutableList<BarcodeItem>> {
            MakeBarcode().initBarcodeList(list) {
                mView.onResultProgress("(${it + 1}/${list.size})")
            }
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "##### onSuccess #####")
                    if (it is MutableList<BarcodeItem>) {
                        //Realm Bytearr필드 업데이트
                        for ((index, value) in it.withIndex()) {
                            if (value.itemType == ConstVariables.ITEM_TYPE_BARCODE) {
                                RealmClient.updateMyBarcodeRealm(value) {
                                    when {
                                        true -> {
                                        }
                                        false -> {
                                        }
                                    }
                                }
                            }
                        }
                        val recyclerViewContext = recyclerView.context
                        val animationController = AnimationUtils.loadLayoutAnimation(recyclerViewContext, R.anim.layout_animation_fall_down)
                        recyclerView.layoutAnimation = animationController
                        adapter.setItems(it)
                        adapter.notifyDataSetChanged()
                        recyclerView.scheduleLayoutAnimation()
                        mView.onResultRealmBarcodeList(true, R.string.string_wait_init_barcode)
                    } else {
                        mView.onResultRealmBarcodeList(false, R.string.string_unknown_error)
                    }
                }, {
                    Log.d(TAG, "##### onError #####")
                    it.printStackTrace()
                    mView.onResultRealmBarcodeList(false, R.string.string_unknown_error)

                }, {
                    Log.d(TAG, "##### onCompletion #####")
                    mView.onResultRealmBarcodeList(false, R.string.string_unknown_error)
                })
                .apply {
                    view.disposables.add(this)
                }
    }

    override fun requestAddBarcode(adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestAddBarcode #####")
        RealmClient.insertMyBarcodeRealm(item) {
            Log.d(TAG, "##### requestAddBarcode ##### added item uid : $it")
            item.barcodeId = it
            adapter.addItem(item)
        }
    }

    override fun requestDeleteBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestDeleteBarcode #####")
        RealmClient.deleteMyBarcodeRealm(item) {
            when {
                true -> {
                    adapter.deleteItem(pos, item)
                }
                false -> {
                    // delete error
                }
            }
        }
    }

    override fun requestModifyBarcode(pos: Int, adapter: BarcodeRecyclerviewAdapter, item: BarcodeItem) {
        Log.d(TAG, "##### requestModifyBarcode #####")
        RealmClient.updateMyBarcodeRealm(item) {
            when {
                true -> {
                    adapter.updateItem(pos, item)
                }
                false -> {

                }
            }
        }
    }

    override fun requestSaveBarcodeList(adapter: BarcodeRecyclerviewAdapter) {
        Log.d(TAG, "##### requestSaveBarcodeList #####")
        val items = adapter.getItems()
        RealmClient.clearMyBarcodeRealm()
        for ((index, value) in items.withIndex()) {
            if (value.barcodeType != null) {
                Log.d(TAG, "##### requestSaveBarcodeList name : ${value.barcodeName}#####")
                RealmClient.insertMyBarcodeRealm(value) {
                    items[index].barcodeId = it
                    adapter.notifyItemChanged(index)
                }
            }
        }
    }
}