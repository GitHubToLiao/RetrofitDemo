package com.oubi.netlibrary.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * =======================================
 * 创建日期:2019-08-27 on 14:54
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:
 * =======================================
 */
class DefaultInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newBuilder = original.newBuilder()
        return chain.proceed(newBuilder.build())
    }
}