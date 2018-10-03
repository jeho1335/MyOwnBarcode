package srjhlab.com.myownbarcode.Dialog

import android.app.DialogFragment
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_dialog_addfromkey.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.Item.BarcodePagerItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.CommonUtils
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.ValidityCheck

class AddFromKeyDialog : DialogFragment(), View.OnClickListener {
    private val TAG = this.javaClass.simpleName

    private var mSelectBarcodeType = -1
    private lateinit var mPagerArr: Array<BarcodePagerItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "##### onCreateView #####")
        dialog.window!!.attributes.windowAnimations = R.style.SelectDialogAnimation
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        EventBus.getDefault().register(this)
        mPagerArr = arrayOf(
                srjhlab.com.myownbarcode.Item.BarcodePagerItem(srjhlab.com.myownbarcode.Utils.ConstVariables.CODE_39, resources.getString(srjhlab.com.myownbarcode.R.string.var_code_39))
                , BarcodePagerItem(ConstVariables.CODE_93, resources.getString(R.string.var_code_93))
                , BarcodePagerItem(ConstVariables.CODE_128, resources.getString(R.string.var_code_128))
                , BarcodePagerItem(ConstVariables.EAN_8, resources.getString(R.string.var_ean_8))
                , BarcodePagerItem(ConstVariables.EAN_13, resources.getString(R.string.var_ean_13))
                , BarcodePagerItem(ConstVariables.PDF_417, resources.getString(R.string.var_pdf_417))
                , BarcodePagerItem(ConstVariables.UPC_A, resources.getString(R.string.var_upc_a))
                , BarcodePagerItem(ConstVariables.CODABAR, resources.getString(R.string.var_codabar))
                , BarcodePagerItem(ConstVariables.ITF, resources.getString(R.string.var_itf))
                , BarcodePagerItem(ConstVariables.QR_CODE, resources.getString(R.string.var_qr_code))
                , BarcodePagerItem(ConstVariables.MAXI_CODE, resources.getString(R.string.var_maxicode))
                , BarcodePagerItem(ConstVariables.AZTEC, resources.getString(R.string.var_aztec)))
        return inflater.inflate(R.layout.layout_dialog_addfromkey, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        initializeUI()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy #####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick ##### v.id : ${v.id}")
        when (v.id) {
            textview_dialog_ok.id -> onClickOk()
            textview_dialog_cancel.id -> dismiss()
        }
    }

    fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        dialog_viewpager.adapter = BarcodePagerAdapter(activity)
        dialog_viewpager.addOnPageChangeListener(mPageChangeListener)
        dialog_viewpager.offscreenPageLimit = 1
        dialog_viewpager.clipToOutline = true

        type_dialog_textview.text = mPagerArr[0].name
        mSelectBarcodeType = 0
        textview_dialog_ok.setOnClickListener(this)
        textview_dialog_cancel.setOnClickListener(this)

    }

    private fun onClickOk() {
        Log.d(TAG, "##### onCLickOk #####")
        val value = edittext_dialog.text.toString()
        if (ValidityCheck.getInstance(activity).check(value, mSelectBarcodeType)) {
            EventBus.getDefault().post(CommonEventbusObejct(ConstVariables.EVENTBUS_ADD_BARCODE, BarcodeItem(value, mSelectBarcodeType.toLong())))
        }
        dismiss()
    }

    private val mPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            Log.d(TAG, "##### onPageSelected pisition : $position ")
            mSelectBarcodeType = position
            type_dialog_textview.text = mPagerArr[position].name
        }
    }


    private inner class BarcodePagerAdapter(context: Context) : PagerAdapter() {
        private var mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            Log.d(TAG, "##### InstantiateItem #####")
            val pagerLayout = mLayoutInflater.inflate(R.layout.item_viewpager, container, false)
            val imageView = pagerLayout.findViewById<ImageView>(R.id.imageview_pager)
            imageView.background = CommonUtils.getSampleBarcode(activity, position)
            pagerLayout.tag = position

            container.addView(pagerLayout, 0)
            return pagerLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            Log.d(TAG, "##### destrotItem #####")
            container.removeView(`object` as View)
        }

        override fun getCount(): Int {
            Log.d(TAG, "##### getCOunt #####")
            return mPagerArr.size
        }

        override fun getItemPosition(`object`: Any): Int {
            Log.d(TAG, "##### getItemPosition #####")
            return POSITION_NONE
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            Log.d(TAG, "##### isVIewFormObject #####")
            return view == `object`
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(obj: CommonEventbusObejct) {
        when (obj.type) {
            ConstVariables.EVENTBUS_ADD_BARCODE_SUCCESS -> dismiss()
        }
    }
}