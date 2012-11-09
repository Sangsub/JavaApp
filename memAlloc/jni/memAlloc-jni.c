#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "sangsub"

#define MEMALLOC_FALSE 0
#define MEMALLOC_TRUE 1

#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG    , __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG    , __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG    , __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG    , __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG    , __VA_ARGS__)


int running_memalloc = MEMALLOC_FALSE;


void
Java_com_MemAlloc_MemAllocService_NativeMemAllocStopFromJNI(JNIEnv * env, jobject thiz)
{
	LOGE("[Native] NativeMemAllocStop :: prev running_memalloc : %d \n", running_memalloc);

	running_memalloc = MEMALLOC_FALSE;
}


jboolean
Java_com_MemAlloc_MemAllocService_NativeMemAllocStartFromJNI(JNIEnv * env, jobject thiz,
															jint arrCount, jint arrSize)
{
	double * temp;
	int i=0;
	running_memalloc = MEMALLOC_TRUE;

	LOGE("[Native] NativeMemAllocStart:: arrCount[%d], arrSize[%d]\n", arrCount, arrSize);

	while(arrCount > 0 && running_memalloc)
	{
		temp = (double *) malloc(sizeof(double)*arrSize);

		if(temp == NULL)
		{
			LOGE("[Native] NativeMemAlloc Fail");
			running_memalloc = MEMALLOC_FALSE;
			return MEMALLOC_FALSE;
		}
		LOGE("[Native] NativeMemAlloc Success");

		for(i=0; i<arrSize;i++)
		{
			temp[i] = 1;
		}

		LOGE("[Native] Native Write OK");

		sleep(1);
		arrCount--;
	}

	running_memalloc = MEMALLOC_FALSE;
	return MEMALLOC_TRUE;
}
