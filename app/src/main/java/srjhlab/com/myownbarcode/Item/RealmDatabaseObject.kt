package srjhlab.com.myownbarcode.Item

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmDatabaseObject : RealmObject() {
    @PrimaryKey
    open var barcodeUid : Int = 0
    open var barcodeType : Long = 0L
    open var barcodeCardColor : Long = 0L
    open var barcodeName : String = ""
    open var barcodeValue : String = ""
    open var barcodeBitmapArr : ByteArray? = null

}