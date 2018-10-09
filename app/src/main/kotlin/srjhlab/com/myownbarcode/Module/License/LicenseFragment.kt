package srjhlab.com.myownbarcode.Module.License

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.layout_fragment_license.*
import srjhlab.com.myownbarcode.R

class LicenseFragment : Fragment(), License.view {
    val TAG = this.javaClass.simpleName
    private lateinit var mPresenter : LicensePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "##### onCreateView #####")
        mPresenter = LicensePresenter(this)
        return inflater.inflate(R.layout.layout_fragment_license, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onActivityCreated #####")
        super.onActivityCreated(savedInstanceState)
        initializeUi()
    }
    private fun initializeUi(){
        Log.d(TAG, "##### initializeUi #####")
        mPresenter.requestLicense(activity as Activity)
        this.view!!.isFocusableInTouchMode = true
        this.view!!.requestFocus()
        this.view!!.setOnKeyListener(mBackKeyListenr)
    }

    override fun onResultLicense(txt: String) {
        Log.d(TAG, "##### onResultLicense #####")
        txt_license.text = txt
    }

    private val mBackKeyListenr : View.OnKeyListener = object : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
            Log.d(TAG, "##### onKey #####")
            mPresenter.requestHandleKeyEvent(keyCode)
            return true
        }
    }
}