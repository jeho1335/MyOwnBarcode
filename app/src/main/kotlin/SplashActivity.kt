package srjhlab.com.myownbarcode


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class SplashActivity : Activity() {
    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "##### onCreate #####")
        super.onCreate(savedInstanceState)
        val intent = Intent(this, MainActivity().javaClass)
        startActivity(intent)
        finish()
    }
}