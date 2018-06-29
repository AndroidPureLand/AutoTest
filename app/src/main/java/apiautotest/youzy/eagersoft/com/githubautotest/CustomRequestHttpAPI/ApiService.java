package apiautotest.youzy.eagersoft.com.githubautotest.CustomRequestHttpAPI;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    //天气查询
    @GET("/apis/alarm/")
    Observable<HttpMessage> getAlarm(@Query("id") String id);

    //汽柴油价格
    @GET("/apis/youjia/")
    Observable<HttpMessage> getYouJia();

    //货币兑换
    @GET("/apis/cash/")
    Observable<HttpMessage> getCash(@Query("num")int num,@Query("in")String in,@Query("out")String out);


    //农历查询
    @GET("/apis/nongli/")
    Observable<HttpMessage> getNongLi(@Query("id")String id,@Query("now")String now);

    //空气质量
    @GET("/apis/aqilist/")
    Observable<HttpMessage> getAqilist();

}
