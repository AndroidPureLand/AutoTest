package com.androidpureland.autotest.HttpData;


import com.androidpureland.autotest.HttpData.Bean.NetworkResponseEntity;
import com.androidpureland.autotest.HttpData.Dao.OnInterceptCallBack;
import com.androidpureland.autotest.Util.sysUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OKHttp拦截器
 * Created by Administrator on 2018/6/25.
 */

public abstract class OkHttpInterceptor implements Interceptor {
    private OnInterceptCallBack callBack;

    public void setCallBack(OnInterceptCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return response(chain.proceed(addHead(chain)));
    }

    public abstract Request addHead(Chain chain);

    /**
     * Response处理
     *
     * @param response
     * @return
     */
    private Response response(Response response) {
        NetworkResponseEntity networkResponseInfo = new NetworkResponseEntity();
        networkResponseInfo.setCode(response.code());
        networkResponseInfo.setMessage(response.message());
        networkResponseInfo.setRedirect(response.isRedirect());
        networkResponseInfo.setProtocol(response.protocol().toString());
        networkResponseInfo.setSuccessful(response.isSuccessful());
        networkResponseInfo.setUrl(response.request().url().toString());
        networkResponseInfo.setHttps(response.request().isHttps());
        networkResponseInfo.setMethod(response.request().method());
        if (response.headers() != null) {
            networkResponseInfo.setHeaders(response.headers().toString());
        } else {
            networkResponseInfo.setHeaders("");
        }
        networkResponseInfo.setResponseBody(response.body());
        if (response != null) {
            if (callBack != null) {
                callBack.onIntercept(networkResponseInfo);
            }
        }
        return response;
    }
}
