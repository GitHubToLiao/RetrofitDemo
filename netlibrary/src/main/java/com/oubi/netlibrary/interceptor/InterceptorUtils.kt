package com.oubi.netlibrary.interceptor

import com.oubi.netlibrary.net.UTF_8
import okhttp3.Headers
import okhttp3.Response
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * =======================================
 * 创建日期:2019-08-27 on 12:12
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:
 * =======================================
 */
private fun bodyEncoded(headers: Headers): Boolean {
    val contentEncoding = headers.get("Content-Encoding")
    return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
}

private fun isPlaintext(buffer: Buffer): Boolean {
    try {
        val prefix = Buffer()
        val byteCount = if (buffer.size() < 64) buffer.size() else 64
        buffer.copyTo(prefix, 0, byteCount)
        for (i in 0..15) {
            if (prefix.exhausted()) {
                break
            }
            val codePoint = prefix.readUtf8CodePoint()
            if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                return false
            }
        }
        return true
    } catch (e: EOFException) {
        return false
    }

}

/**
 * 获取请求数据，处理流只能使用一次
 *
 * @param response
 * @return
 * @throws IOException
 */
@Throws(IOException::class)
fun getResponseStr(response: Response): String {
    var responseStr = ""
    val body = response.body()
    val contentLength = body!!.contentLength()
    if (!bodyEncoded(response.headers())) {
        val source = body.source()
        source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer()

        var charset: Charset? = UTF_8
        val contentType = body.contentType()
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF_8)
            } catch (e: UnsupportedCharsetException) {
                return responseStr
            }

        }

        if (!isPlaintext(buffer)) {
            return responseStr
        }

        if (contentLength != 0L) {
            responseStr = buffer.clone().readString(charset!!)
        }
    }
    return responseStr
}