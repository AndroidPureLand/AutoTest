package com.androidpureland.autotest.HttpData.Dao;


import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;

import java.util.List;

/**
 * 接口测试回调
 * Created by Administrator on 2018/6/21.
 */

public interface OnInterfaceTestPrepareCallBack<T> {

    /**
     * 开始准备
     * @param baseUrlEntity 测试的服务器域名
     */
    void onPrepareStart(BaseUrlEntity baseUrlEntity);

    /**
     * 准备完成
     * @param baseUrlEntity 测试的服务器域名
     * @param list 测试的接口
     */
    void onPrepareComplete(BaseUrlEntity baseUrlEntity, List<T> list);
}
