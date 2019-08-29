package com.oubi.netlibrary.net

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

/**
 * =======================================
 * 创建日期:2019-08-27 on 11:45
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:动画
 * =======================================
 */
fun <T> loadingAnimationObservable(loadingAnimation: ILoadingAnimation): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream
                .subscribeOn(AndroidSchedulers.mainThread())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
                .doOnSubscribe(subscribeConsumer(loadingAnimation))
                .doOnError(errorConsumer(loadingAnimation))
                .doOnTerminate(terminateAction(loadingAnimation))
    }
}

fun subscribeConsumer(loadingAnimation: ILoadingAnimation): Consumer<Disposable> {
    return Consumer {
        loadingAnimation?.loadingAnimationShow()
    }
}

fun terminateAction(loadingAnimation: ILoadingAnimation): Action {
    return Action {
        loadingAnimation?.loadingAnimationDismiss()
    }
}

fun errorConsumer(loadingAnimation: ILoadingAnimation): Consumer<Throwable> {
    return Consumer {
        loadingAnimation?.loadingAnimationDismiss()
    }
}

