package com.oubi.netlibrary.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * =======================================
 * 创建日期:2019-08-27 on 16:06
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:网络是否链接
 * =======================================
 */
fun isNetworkConnected(context: Context): Boolean {
    if (context != null) {
        var manager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var mNetworkInfo = manager.activeNetworkInfo
        if (mNetworkInfo != null) {
            return true
        }
    }
    return false
}