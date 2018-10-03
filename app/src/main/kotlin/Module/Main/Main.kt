package Module.Main

interface Main {
    interface view{
        fun onResultBackPressed(result : Boolean, msg : Int)
    }
    interface presenter{
        fun requestBarcodeScan()
        fun requestBackPressed()
    }
}