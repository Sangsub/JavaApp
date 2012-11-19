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

static int aaa = 1;
static int bbb=1;


void
Java_com_MemAlloc_MemAllocService_PrintAddressFromJNI(JNIEnv * env, jobject thiz, jint i, jint * jdAddr)
{
	LOGE("[Native] PrintAddressFromJNI :: [%d] jdAddr : 0x%x \n", i, jdAddr);
}


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
	jclass targetClass;
	jint javaVal;
	jfieldID jID;
	double * temp;
	int i=0;
	running_memalloc = MEMALLOC_TRUE;
	temp = (double *) malloc(sizeof(double));

	LOGE("[Native] local address :: i[0x%x]\n", &i);
	LOGE("[Native] global address :: running_memalloc[0x%x]\n", &running_memalloc);
	LOGE("[Native] static address :: aaa[0x%x], bbb[0x%x]\n", &aaa, &bbb);
	LOGE("[Native] dynamic address :: temp[0x%x]\n", temp);
	LOGE("[Native] native func address [0x%x]\n", Java_com_MemAlloc_MemAllocService_NativeMemAllocStartFromJNI);

	targetClass = (*env)->GetObjectClass(env,thiz);
	LOGE("[Native] thiz[0x%x], java Class address [0x%x]\n", thiz, targetClass);

	jID = (*env)->GetFieldID(env, targetClass, "arr", "[[Ljava/lang/Double;");
	javaVal = (*env)->GetIntField(env, thiz, jID);
	LOGE("[Native] jID [%d], javaVal[0x%x]\n", jID, javaVal);

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

		LOGE("[Native] Native Write OK [address : 0x%x]", temp);

		sleep(1);
		arrCount--;
	}

	running_memalloc = MEMALLOC_FALSE;
	return MEMALLOC_TRUE;
}
