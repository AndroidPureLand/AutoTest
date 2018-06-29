package apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI;

import com.androidpureland.autotest.HttpData.OkHttpInterceptor;

import okhttp3.Request;

public class MyOkHttpInterceptor extends OkHttpInterceptor {

    @Override
    public Request addHead(Chain chain) {
        return chain.request().newBuilder()
                .header("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
