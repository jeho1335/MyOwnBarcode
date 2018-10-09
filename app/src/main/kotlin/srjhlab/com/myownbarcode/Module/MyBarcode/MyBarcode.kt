package srjhlab.com.myownbarcode.Module.MyBarcode

import android.support.v7.widget.RecyclerView
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem

interface MyBarcode {
    interface view{
        fun onResultBarcodeList(result : Boolean, msg : Int)
    }
    interface presenter{
        fun requestBarcodeList(recyclerView : RecyclerView, adapter : BarcodeRecyclerviewAdapter)
        fun requestAddBarcode(adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestDeleteBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestModifyBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestSaveBarcodeList(adapter : BarcodeRecyclerviewAdapter)
    }
}

