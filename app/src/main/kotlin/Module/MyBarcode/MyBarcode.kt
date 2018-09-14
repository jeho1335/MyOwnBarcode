package srjhlab.com.myownbarcode.Module.MyBarcode

import srjhlab.com.myownbarcode.Adapter.BarcodeRecyclerviewAdapter
import srjhlab.com.myownbarcode.Item.BarcodeItem

interface MyBarcode {
    interface view{
        fun onResultBarcodeList(items : MutableList<BarcodeItem>)
    }
    interface presenter{
        fun requestBarcodeList()
        fun requestAddBarcode(adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestDeleteBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestModifyBarcode(pos : Int, adapter : BarcodeRecyclerviewAdapter, item : BarcodeItem)
        fun requestSaveBarcodeList(adapter : BarcodeRecyclerviewAdapter)
    }
}

