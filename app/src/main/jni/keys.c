#include <string.h>
#include <jni.h>
 
JNIEXPORT jstring JNICALL
Java_Utils_AES256Chiper_getNativeKey1(JNIEnv *env, jobject instance) {
    return (*env)->  NewStringUTF(env, "AOCKSODI12AOSCMN");
}
 
JNIEXPORT jstring JNICALL
Java_Utils_AES256Chiper_getNativeKey2(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "Second Key");
}