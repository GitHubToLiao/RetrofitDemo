package com.oubi.netlibrary.utils

import com.oubi.netlibrary.net.TIME_OUT
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * internal模块可见
 */
internal fun okHttpClient(interceptor: Interceptor): OkHttpClient {
    val builder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(interceptor)

    //忽略证书安全
    try {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        builder.sslSocketFactory(sslSocketFactory)
        builder.hostnameVerifier { _, _ -> true }

    } catch (e: Exception) {

    }

    return builder.build()

}