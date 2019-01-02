package Model

import com.google.firebase.database.FirebaseDatabase

object FirebaseDatabaseReference {
    val getUserBarcode = FirebaseDatabase.getInstance().getReference("UserBarcodes")
    val getNewNotice = FirebaseDatabase.getInstance().getReference("Notice")
}