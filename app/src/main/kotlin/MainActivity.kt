package srjhlab.com.myownbarcode

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import srjhlab.com.myownbarcode.Module.MyBarcode.MyBarcodeFragment
import srjhlab.com.myownbarcode.Module.Settings.SettingsFragment
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct

//When create class in kotlin, not use extends, implements keyword
class MainActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = this.javaClass.simpleName!!
    private val FRAGMENT_STATE_BARCODE_LIST = 1
    private val FRAGMENT_STATE_SETTINGS = 2
    private lateinit var mMyBArcodeFragment: MyBarcodeFragment
    private lateinit var mSettingsFragment: SettingsFragment
    private lateinit var mContentView: View

    //When override a method in kotlin, not use @annotation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)
        mMyBArcodeFragment = MyBarcodeFragment()
        mSettingsFragment = SettingsFragment()
        initializeUI()
        hanleFragment(FRAGMENT_STATE_BARCODE_LIST)
    }


    override fun onDestroy() {
        Log.d(TAG, "##### onDestroy ####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick #####")
        val viewId = v.id

        //when is like that Switch() in java
        when (viewId) {
            img_drawer_icon.id -> layout_drawer.openDrawer(Gravity.LEFT)
            textview_drawer_home.id -> {
                hanleFragment(FRAGMENT_STATE_BARCODE_LIST)
                layout_drawer.closeDrawer(Gravity.LEFT)
            }
            textview_drawer_settings.id -> {
                hanleFragment(FRAGMENT_STATE_SETTINGS)
                layout_drawer.closeDrawer(Gravity.LEFT)
            }
            textview_drawer_license.id -> Log.d(TAG, "##### onItemCLick #####")
            else -> return
        }
    }

    private fun initializeUI() {
        Log.d(TAG, "##### initializeUI #####")
        mContentView = layout_content as View
        img_drawer_icon.setOnClickListener(this)
        textview_drawer_home.setOnClickListener(this)
        textview_drawer_settings.setOnClickListener(this)
        textview_drawer_license.setOnClickListener(this)
    }

    private fun hanleFragment(state: Int) {
        Log.d(TAG, "##### hanleFragment ##### state : $state")
        val fm = fragmentManager
        val ft = fm.beginTransaction()
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
        when (state) {
            FRAGMENT_STATE_BARCODE_LIST -> {
                ft.replace(mContentView.id, mMyBArcodeFragment, mMyBArcodeFragment.TAG)
            }

            FRAGMENT_STATE_SETTINGS -> {
                if (mSettingsFragment.isAdded) {
                    Log.d(TAG, "##### isAdded #####")
                    ft.show(mSettingsFragment)
                } else {
                    Log.d(TAG, "##### isNotAdded #####")
                    ft.add(mContentView.id, mSettingsFragment, mSettingsFragment.TAG)
                }
            }
        }
        Log.d(TAG, "##### commit #####")
        ft.commit()
    }

    fun isDrawerOpen(): Boolean {
        return layout_drawer.isDrawerOpen(Gravity.LEFT)
    }

    @Subscribe
    fun onEVent(obj: CommonEventbusObejct) {

    }
}