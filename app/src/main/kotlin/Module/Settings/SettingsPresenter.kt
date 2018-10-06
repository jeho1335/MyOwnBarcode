package Module.Settings

import Model.FirebaseDatabaseReference
import Utils.AES256Chiper
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import srjhlab.com.myownbarcode.Item.BarcodeItem
import srjhlab.com.myownbarcode.R
import srjhlab.com.myownbarcode.Utils.ConstVariables
import srjhlab.com.myownbarcode.Utils.PreferencesManager

class SettingsPresenter(view: Settings.view) : Settings.presenter {
    private val TAG = this.javaClass.simpleName
    private val mView = view

    init {
        System.loadLibrary("keys")

    }

    external fun getGoogleAuthApiKey(): String

    override fun requestGoogleSignInClient(activity: Activity) {
        Log.d(TAG, "##### requestGoogleSIgnInClient #####")
        try {
            val googleSignInOptions = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getGoogleAuthApiKey())
                    .requestEmail()
                    .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
            googleSignInClient.signOut()
            mView.onResultGoogleSignInClient(true, -1, googleSignInClient)
        } catch (e: Exception) {
            e.printStackTrace()
            mView.onResultGoogleSignInClient(false, R.string.string_cannot_sign_in_google, null)
        }
    }

    override fun requestSignInIntent(activity: Activity, client: GoogleSignInClient?) {
        Log.d(TAG, "##### requestGoogleSIgnInClient #####")
        val signInIntent = client!!.signInIntent
        activity.startActivityForResult(signInIntent, ConstVariables.RC_SIGN_IN)
    }

    override fun requestGoogleSignIn(activity: Activity, data: Intent?) {
        Log.d(TAG, "##### requestGoogleSignIn #####")
        val firebaseAuth = FirebaseAuth.getInstance()
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity) {
                if (task.isSuccessful) {
                    mView.onResultGoogleSignIn(true, R.string.string_sign_in_google, firebaseAuth)
                } else {
                    mView.onResultGoogleSignIn(false, R.string.string_cannot_sign_in_google, firebaseAuth)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mView.onResultGoogleSignIn(false, R.string.string_cannot_sign_in_google, firebaseAuth)
            return
        }
    }

    override fun requestSetDataBackup(activity: Activity, auth: FirebaseAuth) {
        Log.d(TAG, "##### requestSetDataBackup #####")
        var currentUserEmail = auth.currentUser!!.email
        try {
            currentUserEmail = currentUserEmail!!.substring(0, currentUserEmail.lastIndexOf("@"))
        } catch (e: Exception) {
            e.printStackTrace()
            mView.onResultSetDataBackup(false, R.string.string_failed_set_backup_google)
            return
        }
        FirebaseDatabaseReference.mUserBarcodesDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "##### requestSetDataBackup onCancelled ${databaseError.message}#####")
                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultSetDataBackup(false, R.string.string_failed_set_backup_google)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "##### requestSetDataBackup onDataChange currentUserEmail : $currentUserEmail #####")
                FirebaseDatabaseReference.mUserBarcodesDatabase.child(currentUserEmail).ref.removeValue()

                val child = dataSnapshot.children.iterator()
                while (child.hasNext()) {
                    if (child.next().value == currentUserEmail) {
                        Log.d(TAG, "##### requestSetDataBackup onDataChange currentUserEmail : $currentUserEmail #####")
                        FirebaseDatabaseReference.mUserBarcodesDatabase.child(currentUserEmail).ref.removeValue(null)
                    }
                }

                val currentBarcodeItems = PreferencesManager.loadBarcodeItemList(activity)
                for ((index, value) in currentBarcodeItems.withIndex()) {
                    Log.d(TAG, "##### requestSetDataBackup onDataChange id : ${value.itemType}#####")
                    if (value.itemType != 0L) {
                        val encodeBarcodeName = AES256Chiper().AES_Encode(value.barcodeName)
                        val encodeBarcodeValue = AES256Chiper().AES_Encode(value.barcodeValue)
                        Log.d(TAG, "##### requestSetDataBackup onDataChange email : $currentUserEmail id : $encodeBarcodeName#####")

                        val ref = FirebaseDatabaseReference.mUserBarcodesDatabase.child(currentUserEmail).child(index.toString())
                        ref.child("mItemType").setValue(value.itemType)
                        ref.child("mBarcodeType").setValue(value.barcodeType)
                        ref.child("mBarcodeCardColor").setValue(value.barcodeCardColor)
                        ref.child("mBarcodeId").setValue(value.barcodeId)
                        ref.child("mBarcodeName").setValue(encodeBarcodeName)
                        ref.child("mBarcodeValue").setValue(encodeBarcodeValue)
                    }
                }
                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultSetDataBackup(true, R.string.string_success_set_backup_google)
            }
        })
    }

    @Suppress("CAST_NEVER_SUCCEEDS")
    override fun requestGetDataBackup(activity: Activity, auth: FirebaseAuth) {
        Log.d(TAG, "##### requestGetDataBackup #####")
        var currentUserEmail = auth.currentUser!!.email
        try {
            currentUserEmail = currentUserEmail!!.substring(0, currentUserEmail.lastIndexOf("@"))
        } catch (e: Exception) {
            mView.onResultGetDataBackup(false, R.string.string_success_set_backup_google)
            return
        }

        FirebaseDatabaseReference.mUserBarcodesDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "##### requestGetDataBackup onCancelled ${databaseError.message}#####")
                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultGetDataBackup(false, R.string.string_failed_get_backup_google)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "##### requestGetDataBackup onDataChange currentUserEmail : $currentUserEmail #####")
                val resultList: MutableList<BarcodeItem> = ArrayList()
                val child = dataSnapshot.child(currentUserEmail).children.toMutableList()
                for (value in child.withIndex()) {
                    Log.d(TAG, "##### requestGetDataBackup onDataChange value : ${value.value.child("mItemType")} #####")
                    val decodeBarcodeName = AES256Chiper().AES_Decode(value.value.child("mBarcodeName").value as String)
                    val decodeBarcodeValue = AES256Chiper().AES_Decode(value.value.child("mBarcodeValue").value as String)
                    Log.d(TAG, "##### requestGetDataBackup decodeBarcodeName : $decodeBarcodeName decodeBarcodeValue : $decodeBarcodeValue#####")

                    resultList.add(BarcodeItem(
                            value.value.child("mItemType").value as Long
                            , value.value.child("mBarcodeType").value as Long
                            , 0L
                            , decodeBarcodeName
                            , value.value.child("mBarcodeCardColor").value as Long
                            , decodeBarcodeValue))

                }
                resultList.add(BarcodeItem(ConstVariables.ITEM_TYPE_EMPTY, 0L, "새 바코드 추가", 0L, " "))

                PreferencesManager.saveBarcodeItemList(activity, resultList)
                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultGetDataBackup(true, R.string.string_success_get_backup_google)
            }
        })
    }

    override fun requestDeleteDataBackup(activity: Activity, auth: FirebaseAuth) {
        Log.d(TAG, "##### requestDeleteDataBackup #####")
        var currentUserEmail = auth.currentUser!!.email
        try {
            currentUserEmail = currentUserEmail!!.substring(0, currentUserEmail.lastIndexOf("@"))
        } catch (e: Exception) {
            e.printStackTrace()
            mView.onResultDeleteDataBackup(false, R.string.string_failed_delete_backup_google)
            return
        }
        FirebaseDatabaseReference.mUserBarcodesDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, "##### requestDeleteDataBackup onCancelled ${databaseError.message}#####")
                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultDeleteDataBackup(false, R.string.string_failed_delete_backup_google)
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "##### requestDeleteDataBackup onDataChange currentUserEmail : $currentUserEmail #####")
                FirebaseDatabaseReference.mUserBarcodesDatabase.child(currentUserEmail).ref.removeValue()

                val child = dataSnapshot.children.iterator()
                while (child.hasNext()) {
                    if (child.next().value == currentUserEmail) {
                        Log.d(TAG, "##### requestDeleteDataBackup onDataChange currentUserEmail : $currentUserEmail #####")
                        FirebaseDatabaseReference.mUserBarcodesDatabase.child(currentUserEmail).ref.removeValue(null)
                    }
                }

                FirebaseDatabaseReference.mUserBarcodesDatabase.removeEventListener(this)
                mView.onResultDeleteDataBackup(true, R.string.string_success_delete_backup_google)
            }
        })
    }

    override fun requestSetAutobright(activity: Activity) {
        Log.d(TAG, "##### requestSetAutobright #####")
        var currentState = PreferencesManager.loadAutoBrightState(activity)
        if (currentState) {
            PreferencesManager.saveAutoBrightState(activity, false)
        } else {
            PreferencesManager.saveAutoBrightState(activity, true)
        }

        currentState = PreferencesManager.loadAutoBrightState(activity)
        val msg = if (currentState) {
            R.string.string_autobright_on
        } else {
            R.string.string_autobright_off
        }
        mView.onResultGetAutoBright(PreferencesManager.loadAutoBrightState(activity), msg)
    }

    override fun requestGetAutobright(activity: Activity) {
        Log.d(TAG, "##### requestGetAutobright #####")
        val currentState = PreferencesManager.loadAutoBrightState(activity)
        val msg = if (currentState) {
            R.string.string_autobright_on
        } else {
            R.string.string_autobright_off
        }

        mView.onResultGetAutoBright(PreferencesManager.loadAutoBrightState(activity), -1)
    }

    override fun requestGetCurrentAppVersion(activity: Activity) {
        Log.d(TAG, "##### requestGetCurrentAppVersion #####")
        try {
            val packageInfo = activity.packageManager.getPackageInfo(activity.packageName, 0)
            val version = packageInfo.versionName
            mView.onResultGetCurrentAppVersion(true, version)
        } catch (e: Exception) {
            e.printStackTrace()
            mView.onResultGetCurrentAppVersion(false, "")
        }
    }

    override fun requestClickPrivacyPolicy(activity: Activity) {
        Log.d(TAG, "##### requestClickPrivacyPolicy #####")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://gearex2003.dothome.co.kr/myownbarcode_privacy_policy.htm"))
        activity.startActivity(intent)
    }

    override fun requestClickOpenSourceLiscense(activity: Activity) {
        Log.d(TAG, "##### requestClickOpenSourceLiscense #####")
    }
}