#include <jni.h>
#include <string>

// 因为ffmpeg是纯C代码，要在cpp中使用则需要使用 extern "C"
extern "C" {
//#include "include/libavutil/avutil.h"
#include <libavutil/avutil.h>
}

extern "C" JNIEXPORT jstring JNICALL

Java_com_example_videofeatureapp_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
    return env->NewStringUTF(av_version_info());
}