package apiautotest.youzy.eagersoft.com.githubautotest.Application;

import android.app.Application;

import com.androidpureland.autotest.Constant;
import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.Dao.onDataHandlingCallBack;
import com.androidpureland.autotest.HttpData.HttpData;

import apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.HttpMessage;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        HttpData.getInstance().builder(new HttpData.Bulder()
                .setBaseUrlEntity(new BaseUrlEntity("http://api.help.bj.cn","测试地址1"))
                .setBaseUrlEntity(new BaseUrlEntity("http://api.help.bj.cn","测试地址2"))
                .setApiService("apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.ApiService")
                .setRequest("apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI.HttpRequestMethodConfig")
                .onDataHandlingCallBack(new onDataHandlingCallBack() {
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
                }).build()
        );

    }
}
