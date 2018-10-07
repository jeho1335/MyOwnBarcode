package srjhlab.com.myownbarcode.Module.Settings

import Model.ActivityResultEvent
import Module.Settings.Settings
import Module.Settings.SettingsPresenter
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_fragment_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.*
import srjhlab.com.myownbarcode.Dialog.ProgressDialog
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables

class SettingsFragment : Fragment(), View.OnClickListener, Settings.view {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: SettingsPresenter
    private val mProgress = ProgressDialog()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        EventBus.getDefault().register(this)
        return inflater.inflate(R.layout.layout_fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        mPresenter = SettingsPresenter(this)
        initializeUi()
    }

    override fun onDestroyView() {
        Log.d(TAG, "##### onDestroyView #####")
        super.onDestroyView()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        txt_backup_signin.setOnClickListener(this)
        txt_privacy_settings.setOnClickListener(this)
        txt_liscense_settings.setOnClickListener(this)
        layout_func_bright.setOnClickListener(this)
        mPresenter.requestGetCurrentAppVersion(activity as Activity)
        mPresenter.requestGetAutobright(activity as Activity)
    }

    override fun onClick(v: View) {
        Log.d(TAG, "##### onClick #####")
        when (v.id) {
            txt_backup_signin.id -> {
                mPresenter.requestGoogleSignInClient(activity as Activity)
            }
            layout_func_bright.id -> {
                mPresenter.requestSetAutobright(activity as Activity)
            }
            txt_privacy_settings.id -> {
                mPresenter.requestClickPrivacyPolicy(activity as Activity)
            }
            txt_liscense_settings.id -> {
                mPresenter.requestClickOpenSourceLiscense()
            }
        }
    }

    override fun onResultGoogleSignInClient(result: Boolean, msg: Int, client: GoogleSignInClient?) {
        Log.d(TAG, "##### onResultGoogleSignInClient ##### result : $result")
        mProgress.setTitle(getString(R.string.string_wait_signin)).show(activity!!.fragmentManager, this.javaClass.simpleName)
        if (result) {
            mPresenter.requestSignInIntent(activity as Activity, client)
        } else {
            activity!!.toast(resources.getString(msg))
            if (mProgress.showsDialog) {
                mProgress.dismiss()
            }
        }
    }

    override fun onResultGoogleSignIn(result: Boolean, msg: Int, auth: FirebaseAuth) {
        Log.d(TAG, "##### onResultGoogleSignIn #####")
        if (mProgress.showsDialog) {
            mProgress.dismiss()
        }
        if (result) {
            val menuList = listOf(getString(R.string.string_alert_select_setdata), getString(R.string.string_alert_select_getdata), getString(R.string.string_alert_select_deletedata))
            activity!!.selector("", menuList) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        activity!!.alert(getString(R.string.string_alert_setdata_from_google)) {
                            yesButton {
                                mPresenter.requestSetDataBackup(activity as Activity, auth)
                                mProgress.setTitle(getString(R.string.string_wait_export)).show(activity!!.fragmentManager, this.javaClass.simpleName)
                            }
                            noButton { }
                        }.show()
                    }
                    1 -> {
                        activity!!.alert(getString(R.string.string_alert_deletedata_from_google)) {
                            yesButton {
                                mPresenter.requestGetDataBackup(activity as Activity, auth)
                                mProgress.setTitle(getString(R.string.string_wait_import)).show(activity!!.fragmentManager, this.javaClass.simpleName)
                            }
                            noButton { }
                        }.show()
                    }
                    2 -> {
                        activity!!.alert(getString(R.string.string_alert_deletedata_from_google)) {
                            yesButton {
                                mPresenter.requestDeleteDataBackup(activity as Activity, auth)
                                mProgress.setTitle(getString(R.string.string_wait_delete)).show(activity!!.fragmentManager, this.javaClass.simpleName)
                            }
                            noButton { }
                        }.show()
                    }
                }
            }
        } else {
            activity!!.toast(getString(msg))
        }
    }

    override fun onResultSetDataBackup(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultSetDataBackup #####")
        if (mProgress.showsDialog) {
            mProgress.dismiss()
        }
        activity!!.toast(getString(msg))
    }

    override fun onResultGetDataBackup(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultGetDataBackup #####")
        if (mProgress.showsDialog) {
            mProgress.dismiss()
        }
        activity!!.toast(getString(msg))
    }

    override fun onResultDeleteDataBackup(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultGetDataBackup #####")
        if (mProgress.showsDialog) {
            mProgress.dismiss()
        }
        activity!!.toast(getString(msg))
    }

    override fun onResultGetAutoBright(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultGetAutoBright ##### result : $result")
        if (msg != -1) {
            activity!!.toast(getString(msg))
        }
        if (result) {
            txt_bright_on.isSelected = true
            txt_bright_off.isSelected = false
        } else {
            txt_bright_on.isSelected = false
            txt_bright_off.isSelected = true
        }
    }

    override fun onResultGetCurrentAppVersion(result: Boolean, version: String) {
        Log.d(TAG, "##### onResultGetCurrentAppVersion #####")
        if (result) {
            txt_version.text = version
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(obj: CommonEventbusObejct) {
        Log.d(TAG, "##### onEvent #####")
        when (obj.type) {
            ConstVariables.EVENTBUS_ON_ACTIBITY_RESULT -> {
                val resultIntent = obj.`val` as ActivityResultEvent
                if (resultIntent.requestCode == ConstVariables.RC_SIGN_IN) {
                    mPresenter.requestGoogleSignIn(activity as Activity, resultIntent.data)
                }
            }

        }
    }
}