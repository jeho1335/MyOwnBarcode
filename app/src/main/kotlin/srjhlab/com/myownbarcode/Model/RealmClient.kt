package srjhlab.com.myownbarcode.Model

import android.util.Log
import io.reactivex.Flowable
import io.realm.Realm
import io.realm.kotlin.where
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.RealmDatabaseObject

object RealmClient {
    private val TAG = this.javaClass.simpleName

    fun insertMyBarcodeRealm(item: BarcodeItem, uid: (Int) -> Unit) {
        Log.d(TAG, "##### insertMyBarcodeRealm #####")
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.executeTransaction { realmTransaction ->
            val currentUid = realmTransaction.where<RealmDatabaseObject>().max("barcodeUid")
            val nextUid = currentUid?.toInt()?.plus(1) ?: 1

            realmTransaction.createObject(RealmDatabaseObject::class.java, nextUid).apply {
                barcodeType = item.barcodeType
                barcodeCardColor = item.barcodeCardColor
                barcodeName = item.barcodeName
                barcodeValue = item.barcodeValue
                barcodeBitmapArr = item.barcodeBitmapArr
            }
            uid(nextUid)
        }
    }

    fun readMyBarcodeRealm(): Flowable<MutableList<RealmDatabaseObject>> {
        Log.d(TAG, "##### readMyBarcodeRealm #####")
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.where<RealmDatabaseObject>().sort("barcodeUid").findAllAsync()
                .let { realmResult ->
                    return Flowable.just(defaultRealm.copyFromRealm(realmResult))
                }
    }

    fun updateMyBarcodeRealm(item: BarcodeItem, isSuccess: (Boolean) -> Unit) {
        Log.d(TAG, "##### updateMyBarcodeRealm ##### targetUid : ${item.barcodeId} arr : ${item.barcodeBitmapArr}")
        //개선 필요.. RealmDatabaseObject <-> BarcodeItem간 autocasting 가능할지
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.where<RealmDatabaseObject>().equalTo("barcodeUid", item.barcodeId).findFirst()
                    .let { currentObject ->
                        currentObject?.barcodeName = item.barcodeName
                        currentObject?.barcodeCardColor = item.barcodeCardColor
                        currentObject?.barcodeBitmapArr = item.barcodeBitmapArr
                        realmTransaction.insertOrUpdate(currentObject)
                    }
            isSuccess(true)
        }, {

        }, {
            isSuccess(false)
        })
    }

    fun deleteMyBarcodeRealm(item: BarcodeItem, isSuccess: (Boolean) -> Unit) {
        Log.d(TAG, "##### deleteMyBarcodeRealm ##### targetUid : ${item.barcodeId}")
        val defaultRealm = Realm.getDefaultInstance()
        defaultRealm.executeTransactionAsync({ realmTransaction ->
            realmTransaction.where<RealmDatabaseObject>().equalTo("barcodeUid", item.barcodeId).findFirst().let { targetColumn ->
                targetColumn?.deleteFromRealm()
            }
            isSuccess(true)
        }, {

        }, {
            isSuccess(false)
        })
    }

    fun clearMyBarcodeRealm() {
        Log.d(TAG, "##### clearMyBarcodeRealm #####")
        Realm.getDefaultInstance()
                .let { realm ->
                    realm.executeTransaction { realmTransaction ->
                        realmTransaction.deleteAll()
                    }
                }
    }
}