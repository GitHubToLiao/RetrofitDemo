package com.oubi.retrofitdemo

import com.oubi.netlibrary.bean.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * =======================================
 * 创建日期:2019-08-27 on 14:26
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:
 * =======================================
 */
interface RetrofitService {
    //首页分类
    @GET("get_top_cat")
    fun homeCategory():Observable<BaseResponse<String>>

}