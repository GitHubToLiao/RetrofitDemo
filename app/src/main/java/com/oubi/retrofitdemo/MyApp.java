package com.oubi.retrofitdemo;

import android.app.Application;

import com.oubi.netlibrary.net.HttpUtils;


/**
 * =======================================
 * 创建日期:2019-08-27 on 14:21
 * 作   者:张辽
 * 邮   箱:Zl13484407109@sina.com
 * 描   述:
 * =======================================
 */
public class MyApp extends Application {
    public static final String BASE_URL = "https://newproject.dev.unexplainablestore.cn/";

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.INSTANCE.init(BASE_URL, this);
    }
}
