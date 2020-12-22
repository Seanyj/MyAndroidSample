#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_seanyj_mysamples_MainActivity_transformToUpper(JNIEnv *env, jobject instance,
                                                                      jstring s_) {
    const char *s = env->GetStringUTFChars(s_, 0);

    const char *returnValue = "hello world";
    env->ReleaseStringUTFChars(s_, s);

    return env->NewStringUTF(returnValue);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_seanyj_mysamples_test_TestActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
