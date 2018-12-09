package srjhlab.com.myownbarcode.Module.Dialog.AddFromImage

import Model.ActivityResultEvent
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import srjhlab.com.myownbarcode.Base.BaseDialog
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables

class AddFromImageDialog : BaseDialog(), AddFromImage.view{
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter : AddFromImagePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onCreate #####")
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullSizeDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window?.attributes?.windowAnimations = R.style.SelectDialogAnimation
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        mPresenter = AddFromImagePresenter(this)
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.layout_dialog_addimage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter.getImageFromGallery(activity as Activity)
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
        super.onDestroy()
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onResultScanFromImage(format: String, value: String) {
        Log.d(TAG, "##### onResultScanFromImage #####")
        EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_BARCODE, BarcodeItem(value, CommonUtils.convertBarcodeType(activity, format).toLong())))
        dismiss()
    }

    override fun onResultErrorHandling() {
        Log.d(TAG, "##### onResultErrorHandling #####")
        activity?.toast(resources.getString(R.string.string_cannot_find_barcode))
        mPresenter.onDestroy()
        dismiss()
    }

    @Subscribe(priority = 0, threadMode = ThreadMode.MAIN)
    fun onEvent(obj: CommonEventbusObejct) {
        Log.d(TAG, "##### onEvent #####")
        when (obj.type) {
            ConstVariables.EVENTBUS_ON_ACTIBITY_RESULT -> {
                val resultIntent = obj.`val` as ActivityResultEvent
                if (resultIntent.requestCode == ConstVariables.RC_FROM_IMAGE) {
                    mPresenter.getBitmapFromImage(resultIntent.data)
                }
            }
        }
    }
}