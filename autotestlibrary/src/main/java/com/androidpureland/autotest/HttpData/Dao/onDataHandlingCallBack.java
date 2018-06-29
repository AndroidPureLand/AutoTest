package com.androidpureland.autotest.HttpData.Dao;

import com.androidpureland.autotest.Entity.APIConfig;

public interface onDataHandlingCallBack {
    APIConfig onNext(APIConfig apiConfig,Object message);
    APIConfig onError(APIConfig apiConfig,Throwable e);
}
