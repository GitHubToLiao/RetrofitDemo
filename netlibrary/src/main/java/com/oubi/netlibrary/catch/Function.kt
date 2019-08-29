package com.oubi.netlibrary.catch

import android.accounts.NetworkErrorException
import android.content.Context
import com.oubi.netlibrary.utils.isNetworkConnected
import io.paperdb.Paper
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * 当有缓存时永远不执行onError方法
 */
internal fun <T> notExecutedFunction(): Function<Throwable, ObservableSource<T>> {
    return Function { Observable.never() }
}

/**
 * 线程切换
 */
internal fun <T> io2MainObservable(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
                .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI
    }
}

/**
 * 
 */
internal fun <T> catchPublishFunction(cacheObservable: Observable<T>): Function<Observable<T>, ObservableSource<T>> {
    return Function { networkObservable ->
        Observable
                .merge(networkObservable, cacheObservable.takeUntil(networkObservable))
                .subscribeOn(Schedulers.io())
    }
}

/**
 * 数据存储
 */
internal fun <T> savCatchMap(key: String): Function<T, T> {
    return Function { t ->
        Paper.book().write(key, t)
        t
    }
}

/**
 * 获取缓存数据
 */
internal fun <T> cacheObservable(key: String, context: Context?): Observable<T> {
    return Observable.create<T> { emitter ->
        try {
            val readData = Paper.book().read<T>(key)
            if (readData != null) {
                emitter.onNext(readData)
            } else {
                if (context != null && !isNetworkConnected(context))
                    emitter.onError(NetworkErrorException())
            }
            emitter.onComplete()
        } catch (e: Exception) {
        }
    }.subscribeOn(Schedulers.io())//请求数据的事件发生在io线程
            .observeOn(AndroidSchedulers.mainThread())//请求完成后在主线程更显UI;
}