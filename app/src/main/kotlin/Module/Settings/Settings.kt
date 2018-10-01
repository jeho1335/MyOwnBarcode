package Module.Settings

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

interface Settings {
    interface view{
        fun onResultGoogleSignInClient(result : Boolean, msg : Int, client : GoogleSignInClient?)
        fun onResultGoogleSignIn(result : Boolean, msg : Int, auth : FirebaseAuth)
        fun onResultSetDataBackup(result : Boolean, msg : Int)
        fun onResultGetDataBackup(result : Boolean, msg : Int)
    }
    interface presenter{
        fun requestGoogleSignInClient(activity : Activity)
        fun requestSignInIntent(activity : Activity, client : GoogleSignInClient?)
        fun requestGoogleSignIn(activity : Activity, data : Intent?)
        fun requestSetDataBackup(activity: Activity, auth : FirebaseAuth)
        fun requestGetDataBackup(activity: Activity, auth : FirebaseAuth)
    }
}