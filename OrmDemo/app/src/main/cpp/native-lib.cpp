#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_db_harbor_com_ormdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
