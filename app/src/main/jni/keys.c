#include <jni.h>
 
JNIEXPORT jstring JNICALL
Java_Utils_AES256Chiper_getAes256Key(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "AOCKSODI12AOSCMNSDCV34GB6H7D3NMK");
}
 
JNIEXPORT jstring JNICALL
Java_Module_Settings_SettingsPresenter_getGoogleAuthApiKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "687505231144-ebmfooo856j10gn1mabr35mgop1lh80u.apps.googleusercontent.com");
}