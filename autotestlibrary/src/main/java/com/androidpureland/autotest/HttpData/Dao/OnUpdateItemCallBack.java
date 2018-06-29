package com.androidpureland.autotest.HttpData.Dao;

import com.androidpureland.autotest.Entity.APIConfig;

public interface OnUpdateItemCallBack {
    void onStart();
    void onError(APIConfig apiConfig);
    void onNext(APIConfig apiConfig);
}
