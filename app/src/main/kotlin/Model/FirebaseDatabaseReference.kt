package Model

import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseReference {
    val mUserBarcodesDatabase = FirebaseDatabase.getInstance().getReference("UserBarcodes")
}