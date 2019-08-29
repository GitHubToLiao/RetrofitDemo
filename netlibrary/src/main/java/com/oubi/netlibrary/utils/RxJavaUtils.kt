package com.oubi.netlibrary.utils

import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins

/**
 * =======================================
 * 创建日期:2019-08-27 on 15:39
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:
 * =======================================
 */
//设置RxJava 全局错误捕捉方法
fun setRxJavaErrorHandler() {
    RxJavaPlugins.setErrorHandler { throwable -> throwable.printStackTrace() }
}