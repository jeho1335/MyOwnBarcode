package srjhlab.com.myownbarcode.Module.MyBarcode

import androidx.recyclerview.widget.RecyclerView
import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Base.BasePresenter
import srjhlab.com.myownbarcode.Item.BarcodeItem

interface MyBarcode {
    interface view{
        fun onResultBarcodeList(result : Boolean, msg : Int)
    }
    interface presenter : BasePresenter{
        fun requestBarcodeList(recyclerView : RecyclerView, adapter : BarcodeRecyclerviewAdapter)
        fun requestAddBarcode(adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestDeleteBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestModifyBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestSaveBarcodeList(adapter : BarcodeRecyclerviewAdapter)
    }
}

