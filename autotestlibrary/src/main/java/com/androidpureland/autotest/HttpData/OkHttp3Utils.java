package com.androidpureland.autotest.HttpData;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * okHttp的配置
 */
public class OkHttp3Utils {
    private static OkHttp3Utils instance;
    private OkHttpClient.Builder builder;


    /**
     * 获取单例
     *
     * @return
     */
    public static OkHttp3Utils getInstance() {
        if (instance == null) {
            instance = new OkHttp3Utils();
        }
        return instance;
    }

    /**
     * 创建http参数构造器
     *
     * @return
     */
    private OkHttp3Utils createBuilder() {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        return this;
    }

    /**
     * 添加拦截器
     *
     * @param interceptor
     */
    public OkHttp3Utils addInterceptor(Interceptor interceptor) {
        createBuilder();
        builder.addInterceptor(interceptor);
        return this;
    }


    /**
     * 设置连接总时间
     *
     * @param second
     */
    public OkHttp3Utils connectTimeout(int second) {
        createBuilder();
        builder.connectTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param second
     */
    public OkHttp3Utils writeTimeout(int second) {
        createBuilder();
        builder.writeTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * @param second
     */
    public OkHttp3Utils readTimeout(int second) {
        createBuilder();
        builder.readTimeout(second, TimeUnit.SECONDS);
        return this;
    }

    /**
     * 获取okHttp对象
     *
     * @return
     */
    public OkHttpClient build() {
        createBuilder();
        return builder.build();
    }
}
