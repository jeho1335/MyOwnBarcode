package Module.Main

import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

interface Main {
    interface presenter{
        fun requestBarcodeScan()
    }
}