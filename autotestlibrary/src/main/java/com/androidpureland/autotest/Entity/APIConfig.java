package com.androidpureland.autotest.Entity;


import com.androidpureland.autotest.HttpData.Bean.NetworkResponseEntity;

import rx.Observable;

public class APIConfig {
    //Response信息
    private NetworkResponseEntity networkResponseEntity;
    //接口描述
    private String introduce;
    //接口地址
    private String address;
    //测试连接
    private Observable observable;
    //返回代码（-1表示为经过测试）
    private int code = -1;
    //测试所花的时间（-1表示为经过测试）
    private long time = -1;
    //是否选中该接口测试（方便adapter中使用）
    private boolean isChecked = true;
    //该接口是否测试成功
    private boolean successOrFail;
    //服务器返回的消息
    private String httpMessage;
    //服务器返回状态
    private int httpCode=-1;

    /**
     * 开始测试的时候先来一个数据初始化
     */
    public void init() {
        code = -1;
        time = -1;
        successOrFail = false;
        httpMessage = "";
        httpCode=-1;
        networkResponseEntity=null;
    }

    public NetworkResponseEntity getNetworkResponseEntity() {
        return networkResponseEntity;
    }

    public void setNetworkResponseEntity(NetworkResponseEntity networkResponseEntity) {
        this.networkResponseEntity = networkResponseEntity;
    }


    public boolean isSuccessOrFail() {
        return successOrFail;
    }

    public void setSuccessOrFail(boolean successOrFail) {
        this.successOrFail = successOrFail;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Observable getObservable() {
        return observable;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(String httpMessage) {
        this.httpMessage = httpMessage;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public String toString() {
        return "APIConfig{" +
                "networkResponseEntity=" + networkResponseEntity +
                ", introduce='" + introduce + '\'' +
                ", address='" + address + '\'' +
                ", observable=" + observable +
                ", code=" + code +
                ", time=" + time +
                ", isChecked=" + isChecked +
                ", successOrFail=" + successOrFail +
                ", httpMessage='" + httpMessage + '\'' +
                ", httpCode=" + httpCode +
                '}';
    }
}
