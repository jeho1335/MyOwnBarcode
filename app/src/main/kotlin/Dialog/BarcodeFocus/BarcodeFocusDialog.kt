package srjhlab.com.myownbarcode.Dialog

import Dialog.BarcodeFocus.BarcodeFocusPresenter
import android.Manifest
import android.app.DialogFragment
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.layout_dialog_barcodefocus.*
import org.apache.commons.lang.StringUtils
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.MakeBarcode

class BarcodeFocusDialog : DialogFragment() {
    private val TAG = this.javaClass.simpleName
    private var mViewType = VIEW_TYPE_FOCUS
    private lateinit var mPresenter: BarcodeFocusPresenter
    private lateinit var mItem: BarcodeItem

    companion object {
        const val VIEW_TYPE_FOCUS = 1
        const val VIEW_TYPE_SHARE = 2
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.layout_dialog_barcodefocus, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter = BarcodeFocusPresenter()
        initializeUi()
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onResume() {
        Log.d(TAG, "##### onResume #####")
        super.onResume()
        var params = dialog.window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as android.view.WindowManager.LayoutParams

        var layoutParams = layout_barcode_focus.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

        if (mViewType.equals(VIEW_TYPE_SHARE)) {
            val permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                }
            } else {
                CommonUtils.shareBitmapToApps(activity, CommonUtils.viewToBitmap(activity, layout_barcode_focus))
                dismiss()
            }
        } else {
            mPresenter.requestScreenBrightMax(activity, dialog.window)
        }
    }

    fun setBarcodeItem(item: BarcodeItem): BarcodeFocusDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    fun setCommandType(type: Int): BarcodeFocusDialog {
        Log.d(TAG, "##### setCommandType ##### type : $type")
        this.mViewType = type
        return this
    }

    private fun initializeUi() {
        text_name.text = mItem.barcodeName
        var strArr = CommonUtils.splitStringEvery(mItem.barcodeValue, 4);
        var str = StringUtils.join(strArr, " ")
        text_value.text = str
        text_value.invalidate()
        img_barcode.setImageBitmap((MakeBarcode.getInstance().makeBarcode(mItem.barcodeType.toInt(), mItem.barcodeValue)))
    }

}