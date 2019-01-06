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
