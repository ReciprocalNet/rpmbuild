/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_recipnet_site_core_util_SyslogHandler */

#ifndef _Included_org_recipnet_site_core_util_SyslogHandler
#define _Included_org_recipnet_site_core_util_SyslogHandler
#ifdef __cplusplus
extern "C" {
#endif
#undef org_recipnet_site_core_util_SyslogHandler_SYSLOG_MESSAGE_LIMIT
#define org_recipnet_site_core_util_SyslogHandler_SYSLOG_MESSAGE_LIMIT 950L
/*
 * Class:     org_recipnet_site_core_util_SyslogHandler
 * Method:    initLogger
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_recipnet_site_core_util_SyslogHandler_initLogger
  (JNIEnv *, jobject, jstring, jstring);

/*
 * Class:     org_recipnet_site_core_util_SyslogHandler
 * Method:    doLogger
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_recipnet_site_core_util_SyslogHandler_doLogger
  (JNIEnv *, jobject, jint, jstring);

/*
 * Class:     org_recipnet_site_core_util_SyslogHandler
 * Method:    closeLogger
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_recipnet_site_core_util_SyslogHandler_closeLogger
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
