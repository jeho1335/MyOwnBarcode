package srjhlab.com.myownbarcode.Dialog

import android.app.DialogFragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_dialog_barcodefocus.*
import kotlinx.android.synthetic.main.layout_dialog_progress.*
import org.apache.commons.lang.StringUtils
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.MakeBarcode

class ProgressDialog : DialogFragment() {
    private val TAG = this.javaClass.simpleName
    private var mProgressTitle = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.layout_dialog_progress, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }

    override fun onResume() {
        Log.d(TAG, "##### onResume #####")
        super.onResume()
        var params = dialog.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as android.view.WindowManager.LayoutParams
    }


    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        txt_title_progress.text = mProgressTitle
    }

    fun setTitle(title : String) : ProgressDialog{
        Log.d(TAG, "##### setTitle #####")
        mProgressTitle = title
        return this
    }

}