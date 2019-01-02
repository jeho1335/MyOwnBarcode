package srjhlab.com.myownbarcode.Model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.GsonBuilder
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Utils.ConstVariables
import java.util.*
import kotlin.collections.ArrayList

object PreferencesManager {
    val TAG = this.javaClass.simpleName

    fun saveBarcodeItemList(context: Context, items: MutableList<BarcodeItem>) {
        Log.d(TAG, "##### saveBarcodeItemList #####")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = GsonBuilder().create()
        val iterator = items.iterator()
        val saveList: MutableList<String> = ArrayList()
        var cnt = 10000

        iterator.forEach {
            Log.d(TAG, "##### saveBarcodeItemList ${it.barcodeName}#####")
            val gsotString = cnt++.toString() + ";" + gson.toJson(it)
            saveList.add(gsotString)
        }
        val typeSet: MutableSet<String> = HashSet()
        typeSet.addAll(saveList)
        editor.putStringSet(ConstVariables.PREF_BARCODE_ITEM, typeSet)
        editor.apply()
    }

    fun loadBarcodeItemList(context: Context): MutableList<BarcodeItem> {
        Log.d(TAG, "##### loadBarcodeItemList #####")

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = GsonBuilder().create()
        val barcodeItemList: MutableList<BarcodeItem> = ArrayList()
        val set: MutableSet<String>? = sharedPreferences.getStringSet(ConstVariables.PREF_BARCODE_ITEM, null)
        if (set == null) {
            barcodeItemList.add(BarcodeItem(ConstVariables.ITEM_TYPE_EMPTY, 0, "새 바코드 추가", 0L, " "))
            return barcodeItemList
        }

        val list: MutableList<String> = ArrayList(set)
        Collections.sort(list, cmpAsc)
        val iterator = list.iterator()

        iterator.forEach {
            val temp: List<String> = it.split(ConstVariables.PREF_SPLIT)
            val item: BarcodeItem = gson.fromJson(temp.get(1), BarcodeItem().javaClass)
            barcodeItemList.add(item)
            Log.d(TAG, "##### loadBarcodeITemList ##### item.barcodeName : " + item.barcodeName + " bitmap : " + item.barcodeValue)
        }

        return barcodeItemList
    }

    private val cmpAsc: java.util.Comparator<String> = java.util.Comparator { o1, o2 -> o1.compareTo(o2) }

    /*
    * 위 코드 다음 RC에서 삭제 예정
    * */

    private inline fun SharedPreferences.save(block: SharedPreferences.Editor.() -> Unit) = apply {
        val editor = edit()
        editor.block()
        editor.apply()
    }

    fun saveAutoBrightState(context: Context, state: Boolean) {
        Log.d(TAG, "##### saveAutoBrightState #####")
        PreferenceManager.getDefaultSharedPreferences(context).save { putBoolean(ConstVariables.PREF_AUTO_BRIGHT_MAX, state) }
    }

    fun loadAutoBrightState(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(ConstVariables.PREF_AUTO_BRIGHT_MAX, false)

    fun saveReadLastNotice(context: Context, date: Int) {
        Log.d(TAG, "##### saveReadLastNotice #####")
        PreferenceManager.getDefaultSharedPreferences(context).save { putInt(ConstVariables.PREF_READ_LAST_NOTICE, date) }
    }

    fun loadRealLastNotice(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).getInt(ConstVariables.PREF_READ_LAST_NOTICE, ConstVariables.PREF_NO_READ_LAST_NOTICE)
}
