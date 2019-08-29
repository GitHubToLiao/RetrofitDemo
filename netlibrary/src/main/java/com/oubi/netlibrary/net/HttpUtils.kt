package com.oubi.netlibrary.net

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import com.oubi.netlibrary.bean.BaseResponse
import com.oubi.netlibrary.catch.cacheObservable
import com.oubi.netlibrary.catch.catchPublishFunction
import com.oubi.netlibrary.catch.io2MainObservable
import com.oubi.netlibrary.catch.notExecutedFunction
import com.oubi.netlibrary.catch.savCatchMap
import com.oubi.netlibrary.converter.FastJsonConvertFactory
import com.oubi.netlibrary.interceptor.DefaultInterceptor
import com.oubi.netlibrary.utils.okHttpClient
import com.oubi.netlibrary.utils.setRxJavaErrorHandler
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * =======================================
 * 创建日期:2019-08-26 on 16:23
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:网络请求：带缓存
 * 使   用:
 * 1.在MyApp中调用init方法进行初始化
 * 2.通过getRetrofitService获取服务
 * 3.调用startRequest方法开始请求
 * 注意：HttpUtils中所有方法都必须在startRequest方法之前调用
 *
 *
 * =======================================
 */
@SuppressLint("StaticFieldLeak")
object HttpUtils {

    private lateinit var retrofit: Retrofit
    private var context: Context? = null
    private var isCatchData = false
    private var catchKey = ""
    private var loadingAnimation: ILoadingAnimation? = null
    /**
     * 初始化
     *
     * @param baseUrl
     * @return
     */
    fun init(baseUrl: String, context: Context): Retrofit {
        this.context = context
        //初始化非关系型数据库
        Paper.init(context)
        //设置RxJava 全局错误捕捉方法
        setRxJavaErrorHandler()
        val okHttpClient = okHttpClient(DefaultInterceptor())
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .client(okHttpClient)
                .build()
        return retrofit
    }


    /**
     * 初始化
     *
     * @param baseUrl
     * @param interceptor 拦截器
     * @return
     */
    fun init(baseUrl: String, context: Context, interceptor: Interceptor): Retrofit {
        this.context = context
        //初始化非关系型数据库
        Paper.init(context)
        //设置RxJava 全局错误捕捉方法
        setRxJavaErrorHandler()
        val okHttpClient = okHttpClient(interceptor)
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConvertFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .client(okHttpClient)
                .build()
        return retrofit
    }

    /**
     * 是否缓存数据
     */
    fun isCatchData(isCatchData: Boolean, catchKey: String): HttpUtils {
        this.isCatchData = isCatchData
        this.catchKey = catchKey
        return this
    }

    /**
     * 设置请求加载动画
     */
    fun setLoadAnim(loadingAnimation: ILoadingAnimation): HttpUtils {
        this.loadingAnimation = loadingAnimation
        return this
    }

    fun <T> getRetrofitService(cls: Class<T>): T {
        return retrofit.create(cls)
    }

    /**
     * 开始请求数据
     */
    @SuppressLint("CheckResult")
    fun <T> startRequest(observable: Observable<BaseResponse<T>>, callback: HttpCallback<T>) {
        var formatObservable: Observable<BaseResponse<T>>
        if (isCatchData && !TextUtils.isEmpty(catchKey)) {
            formatObservable = observable
                    .onErrorResumeNext(notExecutedFunction())
                    .compose(io2MainObservable())
                    .map(savCatchMap(catchKey))
                    .publish(catchPublishFunction(cacheObservable(catchKey, context)))
        } else {
            formatObservable = observable.compose(io2MainObservable())
        }
        if (loadingAnimation != null) {
            formatObservable = formatObservable.compose(loadingAnimationObservable(loadingAnimation!!))
        }
        formatObservable.subscribe(object : Observer<BaseResponse<T>> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: BaseResponse<T>) {
                if (t.code == 200) {
                    callback?.onSuccess(t.result)
                } else {
                    callback?.onError(t.code, t.msg)
                }
                loadingAnimation?.loadingAnimationDismiss()
            }

            override fun onError(e: Throwable) {
                callback?.onFailure(e)
            }

        })
    }

}