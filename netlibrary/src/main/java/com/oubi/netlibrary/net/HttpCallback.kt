package com.oubi.netlibrary.net
/**
 * =======================================
 * 创建日期:2019-08-27 on 11:19
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:网络请求结果回掉
 * =======================================
 */
abstract class HttpCallback<T>  {
    //成功
    abstract fun onSuccess(t: T)
    //失败
    abstract fun onError(code: Int, message: String)
    //网络错误
    abstract fun onFailure(e: Throwable)
}