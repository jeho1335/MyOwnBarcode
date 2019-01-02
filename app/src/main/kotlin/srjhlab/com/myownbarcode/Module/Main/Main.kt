package Module.Main

import srjhlab.com.myownbarcode.Base.BasePresenter

interface Main {
    interface view{
        fun onResultBarcodeScan(result : Boolean, msg : Int)
        fun onResultBackPressed(result : Boolean, msg : Int)
        fun onResultNewNotice(result : Boolean, notice : String = "")
    }
    interface presenter : BasePresenter{
        fun requestBarcodeScan()
        fun requestBackPressed()
        fun requestNewNotice()
    }
}