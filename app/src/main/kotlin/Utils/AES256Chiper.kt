package Utils

import android.util.Base64
import android.util.Log
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec



class AES256Chiper {
    val TAG = this.javaClass.simpleName

    init {
        System.loadLibrary("keys")

    }
    external fun getNativeKey1(): String
    external fun getNativeKey2(): String

    val secretKey = getNativeKey1()

    val ivBytes: ByteArray = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
    fun AES_Encode(str: String): String {
        Log.d(TAG, "##### AES_Encode #####")
        val textBytes = str.toByteArray(Charsets.UTF_8)
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)

        return Base64.encodeToString(cipher.doFinal(textBytes), 0)

    }

    fun AES_Decode(str: String): String {
        val textBytes = Base64.decode(str, 0)
        val ivSpec = IvParameterSpec(ivBytes)
        val newKey = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, newKey)

        return cipher.doFinal(textBytes) as String
    }
}