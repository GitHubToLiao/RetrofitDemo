package com.oubi.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.oubi.netlibrary.net.HttpCallback;
import com.oubi.netlibrary.net.HttpUtils;
import com.oubi.netlibrary.net.ILoadingAnimation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RetrofitService retrofitService = HttpUtils.INSTANCE.getRetrofitService(RetrofitService.class);
        HttpUtils.INSTANCE.setLoadAnim(new ILoadingAnimation() {
                    @Override
                    public void loadingAnimationShow() {
                        Log.e("aaaaaaaaaaaaaaaaaaaa","loadingAnim show");
                    }

                    @Override
                    public void loadingAnimationDismiss() {
                        Log.e("aaaaaaaaaaaaaaaaaaaa","loadingAnim dismiss");
                    }
                })
                .isCatchData(true,"BASE_DATA")
                .startRequest(retrofitService.homeCategory(), new HttpCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("aaaaaaaaaaaaaaaaaaaa","s----"+s);
            }

            @Override
            public void onError(int code, @NotNull String message) {
                Log.e("aaaaaaaaaaaaaaaaaaaa","error----"+code);
            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                Log.e("aaaaaaaaaaaaaaaaaaaa","failure----"+e.getLocalizedMessage());
            }
        });

    }
}
