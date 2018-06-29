package apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI;

import com.androidpureland.autotest.Entity.APIConfig;

import java.lang.reflect.Proxy;

public class HttpRequestMethodConfig {

    //API接口类
    public ApiService service;

    //绑定API接口代理
    public void bindAPIProxy(Proxy proxy) {
        this.service = (ApiService) proxy;
    }

    //天气查询
    public APIConfig getAlarm() {
        APIConfig apiConfig = new APIConfig();
        apiConfig.setIntroduce("天气查询");
        apiConfig.setAddress("/apis/alarm/");
        apiConfig.setObservable(service.getAlarm("长春"));
        return apiConfig;
    }

    //汽柴油价格
    public APIConfig getYouJia() {
        APIConfig apiConfig = new APIConfig();
        apiConfig.setIntroduce("汽柴油价格");
        apiConfig.setAddress("/apis/youjia/");
        apiConfig.setObservable(service.getYouJia());
        return apiConfig;
    }

    //货币兑换
    public APIConfig getCash() {
        APIConfig apiConfig = new APIConfig();
        apiConfig.setIntroduce("货币兑换");
        apiConfig.setAddress("/apis/cash/");
        apiConfig.setObservable(service.getCash(20,"人民币","加元"));
        return apiConfig;
    }

    //农历查询
    public APIConfig getNongLi() {
        APIConfig apiConfig = new APIConfig();
        apiConfig.setIntroduce("农历查询");
        apiConfig.setAddress("/apis/nongli/");
        apiConfig.setObservable(service.getNongLi("beijing","2018-6-29 14:02:52"));
        return apiConfig;
    }

    //空气质量
    public APIConfig getAqilist() {
        APIConfig apiConfig = new APIConfig();
        apiConfig.setIntroduce("空气质量");
        apiConfig.setAddress("/apis/aqilist/");
        apiConfig.setObservable(service.getAqilist());
        return apiConfig;
    }

}
