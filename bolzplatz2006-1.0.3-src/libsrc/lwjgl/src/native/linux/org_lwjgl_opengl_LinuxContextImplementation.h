/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_opengl_LinuxContextImplementation */

#ifndef _Included_org_lwjgl_opengl_LinuxContextImplementation
#define _Included_org_lwjgl_opengl_LinuxContextImplementation
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nCreate
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nCreate
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nSwapBuffers
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nSwapBuffers
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nReleaseCurrentContext
 * Signature: (Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nReleaseCurrentContext
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nMakeCurrent
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nMakeCurrent
  (JNIEnv *, jclass, jobject, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nIsCurrent
 * Signature: (Ljava/nio/ByteBuffer;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nIsCurrent
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nSetVSync
 * Signature: (Ljava/nio/ByteBuffer;Z)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nSetVSync
  (JNIEnv *, jclass, jobject, jboolean);

/*
 * Class:     org_lwjgl_opengl_LinuxContextImplementation
 * Method:    nDestroy
 * Signature: (Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxContextImplementation_nDestroy
  (JNIEnv *, jclass, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif