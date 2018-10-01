package srjhlab.com.myownbarcode.Module.Settings

import Model.ActivityResultEvent
import Module.Settings.Settings
import Module.Settings.SettingsPresenter
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.layout_fragment_settings.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.*
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.CommonEventbusObejct
import srjhlab.com.myownbarcode.Utils.ConstVariables

class SettingsFragment : Fragment(), View.OnClickListener, Settings.view {
    private val TAG = this.javaClass.simpleName
    private lateinit var mPresenter: SettingsPresenter

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

    override fun onDestroy() {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    private fun initializeUi() {
        Log.d(TAG, "##### initializeUi #####")
        btn_google_signin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            btn_google_signin.id -> {
                mPresenter.requestGoogleSignInClient(activity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "##### onActivityResult #####")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResultGoogleSignInClient(result: Boolean, msg: Int, client: GoogleSignInClient?) {
        Log.d(TAG, "##### onResultGoogleSignInClient ##### result : $result")
        if (result) {
            mPresenter.requestSignInIntent(activity, client)
        } else {
            activity.toast(resources.getString(msg))
        }
    }

    override fun onResultGoogleSignIn(result: Boolean, msg: Int, auth: FirebaseAuth) {
        Log.d(TAG, "##### onResultGoogleSignIn #####")
        if (result) {
            val menuList = listOf(getString(R.string.string_alert_select_setdata), getString(R.string.string_alert_select_getdata))
            activity.selector("", menuList) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        activity.alert(getString(R.string.string_alert_setdata_from_google)) {
                            yesButton { mPresenter.requestSetDataBackup(activity, auth) }
                            noButton { }
                        }.show()
                    }
                    1 -> {
                        activity.alert(getString(R.string.string_alert_getdata_from_google)) {
                            yesButton { mPresenter.requestGetDataBackup(activity, auth) }
                            noButton { }
                        }.show()
                    }
                }
            }
        } else {
            activity.toast(getString(msg))
        }
    }

    override fun onResultSetDataBackup(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultSetDataBackup #####")
        activity.toast(getString(msg))
    }

    override fun onResultGetDataBackup(result: Boolean, msg: Int) {
        Log.d(TAG, "##### onResultGetDataBackup #####")
        activity.toast(getString(msg))
    }

    @Subscribe
    fun onEvent(obj: CommonEventbusObejct) {
        Log.d(TAG, "##### onEvent #####")
        when (obj.type) {
            ConstVariables.EVENTBUS_ON_ACTIBITY_RESULT -> {
                val resultIntent = obj.`val` as ActivityResultEvent
                if (resultIntent.requestCode == ConstVariables.RC_SIGN_IN) {
                    mPresenter.requestGoogleSignIn(activity, resultIntent.data)
                }
            }

        }
    }
}