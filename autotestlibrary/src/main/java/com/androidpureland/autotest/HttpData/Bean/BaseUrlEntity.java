package com.androidpureland.autotest.HttpData.Bean;

/**
 * 服务器域名
 * Created by Administrator on 2018/6/23.
 */

public class BaseUrlEntity {
    private String url; //域名地址
    private String urlName;//自定义名称

    public BaseUrlEntity(String url, String urlName) {
        this.url = url;
        this.urlName = urlName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }
}
