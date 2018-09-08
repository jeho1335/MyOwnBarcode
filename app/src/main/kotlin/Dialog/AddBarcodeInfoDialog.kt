package srjhlab.com.myownbarcode.Dialog

import android.app.DialogFragment
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_dialog_addinfo.*
import org.apache.commons.lang.StringUtils
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.MakeBarcode

@Suppress("DEPRECATION")

class AddBarcodeInfoDialog : DialogFragment(), View.OnClickListener {
    private val TAG = this.javaClass.simpleName

    companion object {
        const val MODE_ADD_BARCODE = 0
        const val MODE_EDIT_BARCODE = 1
    }

    private lateinit var mItem: BarcodeItem
    private var mCommandType: Int = -1
    private lateinit var mBitmap: Bitmap
    private lateinit var mDrawable: Drawable
    private lateinit var mColorArr: Array<Int>
    private lateinit var mColorPickerArr: Array<ImageView>
    private var mPicColor: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        mColorArr = arrayOf(
                activity.getColor(R.color.color_pic_1)
                , activity.getColor(R.color.color_pic_2)
                , activity.getColor(R.color.color_pic_3)
                , activity.getColor(R.color.color_pic_4)
                , activity.getColor(R.color.color_pic_5)
                , activity.getColor(R.color.color_pic_6)
                , activity.getColor(R.color.color_pic_7)
                , activity.getColor(R.color.color_pic_8)
                , activity.getColor(R.color.color_pic_9)
                , activity.getColor(R.color.color_pic_10)
                , activity.getColor(R.color.color_pic_11)
                , activity.getColor(R.color.color_pic_12)
        )
        return inflater.inflate(R.layout.layout_dialog_addinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "##### onACtivityCReated #####")
        initializeUI()
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick ##### v.id : ${v.id}")
        when (v.id) {
            textview_dialog_ok.id -> {
                saveBarcode()
                dismiss()
                return
            }
            textview_dialog_cancel.id -> {
                dismiss()
                return
            }
        }
        for ((index, i) in mColorArr.withIndex()) {
            if (mColorPickerArr[index].id.equals(v.id)) {
                clearSelectedColor()
                this.mPicColor = mColorArr[index]
                mColorPickerArr[index].isSelected = true
                return
            }
        }
    }

    fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        textview_dialog_ok.setOnClickListener(this)
        textview_dialog_cancel.setOnClickListener(this)

        mColorPickerArr = arrayOf(
                color_pic_1
                , color_pic_2
                , color_pic_3
                , color_pic_4
                , color_pic_5
                , color_pic_6
                , color_pic_7
                , color_pic_8
                , color_pic_9
                , color_pic_10
                , color_pic_11
                , color_pic_12)

        for ((index, i) in mColorPickerArr.withIndex()) {
            mColorPickerArr[index].setOnClickListener(this)
        }

        val strArr: Array<String> = CommonUtils.splitStringEvery(mItem.barcodeValue, 4)
        val str = StringUtils.join(strArr, " ")
        textview_value_dialog_add_barcode.text = str

        if (mItem.barcodeValue != null) {
            setOverviewBarcode(mItem.barcodeType, mItem.barcodeValue)
        }

        if (mItem.barcodeType != null) {
            textview_type_dialog_add_barcode.text = CommonUtils.convertBarcodeType(activity, mItem.barcodeType)
        }

        if (mCommandType == MODE_EDIT_BARCODE) {
            edittext_dialog_add_barcode.setText(mItem.barcodeName)
            setPreSelectedColor(mItem.barcodeCardColor)
            mPicColor = mItem.barcodeCardColor
        }
    }


    fun setCommandType(type: Int): AddBarcodeInfoDialog {
        Log.d(TAG, "##### setCommandType #####")
        this.mCommandType = type
        return this
    }

    fun setBarcodeItem(item: BarcodeItem): AddBarcodeInfoDialog {
        Log.d(TAG, "##### setBarcodeItem #####")
        this.mItem = item
        return this
    }

    private fun saveBarcode() {
        Log.d(TAG, "##### saveBarcode #####")
        if (mPicColor == -1) {
//            ToastUtil.getInstance(activity).showToast(resources.getString(R.string.string_request_pic_color))
            toast(resources.getString(R.string.string_request_pic_color))
            return
        }

        if (edittext_dialog_add_barcode.text.toString() == "") {
//            ToastUtil.getInstance(activity).showToast(resources.getString(R.string.string_request_write_name))
            toast(resources.getString(R.string.string_request_write_name))
            return
        }

        when (mCommandType) {
            MODE_ADD_BARCODE -> EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_NEW_BARCODE, BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.barcodeType, mItem.barcodeId, edittext_dialog_add_barcode.text.toString(), mPicColor, mItem.barcodeValue, mBitmap)))
            MODE_EDIT_BARCODE -> EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_MODIFY_BARCODE, BarcodeItem(ConstVariables.ITEM_TYPE_BARCODE, mItem.barcodeType, mItem.barcodeId, edittext_dialog_add_barcode.text.toString(), mPicColor, mItem.barcodeValue, mItem.barcodeBitmap)))
        }
        dismiss()

    }

    private fun setPreSelectedColor(color: Int) {
        Log.d(TAG, "##### setPreSelectedColor #####")
        when (color) {
            activity.getColor(R.color.color_pic_1) -> mColorPickerArr.get(0).isSelected = true
            activity.getColor(R.color.color_pic_2) -> mColorPickerArr.get(1).isSelected = true
            activity.getColor(R.color.color_pic_3) -> mColorPickerArr.get(2).isSelected = true
            activity.getColor(R.color.color_pic_4) -> mColorPickerArr.get(3).isSelected = true
            activity.getColor(R.color.color_pic_5) -> mColorPickerArr.get(4).isSelected = true
            activity.getColor(R.color.color_pic_6) -> mColorPickerArr.get(5).isSelected = true
            activity.getColor(R.color.color_pic_7) -> mColorPickerArr.get(6).isSelected = true
            activity.getColor(R.color.color_pic_8) -> mColorPickerArr.get(7).isSelected = true
            activity.getColor(R.color.color_pic_9) -> mColorPickerArr.get(8).isSelected = true
            activity.getColor(R.color.color_pic_10) -> mColorPickerArr.get(9).isSelected = true
            activity.getColor(R.color.color_pic_11) -> mColorPickerArr.get(10).isSelected = true
            activity.getColor(R.color.color_pic_12) -> mColorPickerArr.get(11).isSelected = true
        }
    }

    private fun clearSelectedColor() {
        Log.d(TAG, "#### clearSelectedColor #####")
        for ((index, i) in mColorPickerArr.withIndex()) {
            mColorPickerArr[index].isSelected = false
        }
    }

    private fun setOverviewBarcode(type: Int, value: String) {
        Log.d(TAG, "##### setOverviewBarcode #####")
        var imageBitmap = MakeBarcode.getInstance().makeBarcode(type, value)
        this.mBitmap = imageBitmap
        imageview_barcode_dialog_add_barcode.setImageBitmap(mBitmap)
        this.mDrawable = BitmapDrawable(mBitmap)
    }
}