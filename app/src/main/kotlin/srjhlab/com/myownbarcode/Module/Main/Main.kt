package Module.Main

interface Main {
    interface view{
        fun onResultBarcodeScan(result : Boolean, msg : Int)
        fun onResultBackPressed(result : Boolean, msg : Int)
    }
    interface presenter{
        fun requestBarcodeScan()
        fun requestBackPressed()
    }
}