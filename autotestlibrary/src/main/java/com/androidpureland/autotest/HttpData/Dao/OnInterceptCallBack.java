package com.androidpureland.autotest.HttpData.Dao;


import com.androidpureland.autotest.HttpData.Bean.NetworkResponseEntity;

/**
 * 拦截器回调
 * Created by Administrator on 2018/6/25.
 */

public interface OnInterceptCallBack {

    void onIntercept(NetworkResponseEntity networkResponseInfo);
}
