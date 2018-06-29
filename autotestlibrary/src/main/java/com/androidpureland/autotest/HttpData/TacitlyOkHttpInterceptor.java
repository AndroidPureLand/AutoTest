package com.androidpureland.autotest.HttpData;

import com.androidpureland.autotest.Util.sysUtil;

import okhttp3.Interceptor;
import okhttp3.Request;

public class TacitlyOkHttpInterceptor extends OkHttpInterceptor {
    @Override
    public Request addHead(Chain chain) {
        return chain.request().newBuilder()
                .header("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
