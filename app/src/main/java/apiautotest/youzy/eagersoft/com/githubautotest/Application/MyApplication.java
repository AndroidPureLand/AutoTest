package apiautotest.youzy.eagersoft.com.githubautotest.Application;

import android.app.Application;

import com.androidpureland.autotest.Constant;
import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.Dao.onDataHandlingCallBack;
import com.androidpureland.autotest.HttpData.HttpData;

import apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.HttpMessage;
import apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.MyOkHttpInterceptor;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HttpData.getInstance().builder(new HttpData.Bulder()
                .setBaseUrlEntity(new BaseUrlEntity("http://api.help.bj.cn","测试地址1"))//添加测试地址
                .setBaseUrlEntity(new BaseUrlEntity("http://api.help.bj.cn","测试地址2"))//添加测试地址 可添加多个
                .setApiService("apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.ApiService")//反射ApiService
                .setRequest("apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.HttpRequestMethodConfig")//反射ApiServiceHttpRequestMethodConfig
                .onDataHandlingCallBack(new onDataHandlingCallBack() {//设置请求数据返回设置
                    @Override
                    public APIConfig onNext(APIConfig apiConfig, Object message) {
                        HttpMessage httpMessage = (HttpMessage) message;

                        apiConfig.setHttpCode(httpMessage.getStatus().equals("0")?0:1);
                        apiConfig.setCode(httpMessage.getStatus().equals("0")?0:1);
                        apiConfig.setHttpMessage(httpMessage.getMsg());

                        if (httpMessage.getStatus().equals("0")) {
                            apiConfig.setSuccessOrFail(true);
                        } else {
                            apiConfig.setSuccessOrFail(false);
                        }

                        apiConfig.setTime(System.currentTimeMillis() - apiConfig.getTime());
                        return apiConfig;
                    }

                    @Override
                    public APIConfig onError(APIConfig apiConfig, Throwable e) {
                        apiConfig.setHttpCode(Constant.CUSTOM_ERROR_CODE);
                        apiConfig.setCode(Constant.CUSTOM_ERROR_CODE);
                        apiConfig.setSuccessOrFail(false);
                        apiConfig.setTime(System.currentTimeMillis() - apiConfig.getTime());
                        apiConfig.setHttpCode(Constant.CUSTOM_ERROR_CODE);
                        apiConfig.setHttpMessage(e.getMessage().toString());
                        return apiConfig;
                    }
                })
                .setOkHttpInterceptor(new MyOkHttpInterceptor())//请求头
                .build()//构建
        );

    }
}
