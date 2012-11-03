#include <stdio.h>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "sangsub"

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG    , __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG    , __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG    , __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG    , __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG    , __VA_ARGS__)



jstring
Java_com_nativeTest_jni_nativeTest_stringFromJNI(JNIEnv * env, jobject thiz)
{
	char buf[256];
	sprintf(buf, "Hello from 45JNI");

	LOGE("testaaaaaaaaaaaaaaaaaaaaaa");

	return (*env)->NewStringUTF(env, buf);
}



