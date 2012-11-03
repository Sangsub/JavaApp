LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_LDLIBS := -llog
LOCAL_MODULE	:= memAlloc-jni
LOCAL_SRC_FILES	:= memAlloc-jni.c
  

include $(BUILD_SHARED_LIBRARY)