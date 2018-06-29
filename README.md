### ApiAutoTest-Android
---
###### 初衷
> 每次做一个小的功能迭代在上线前，为保证线上产品的稳定性，严谨的小伙伴们总会跑一边所有的功能，其实自己很清楚业务方面只要自己没动是不会出什么问题的，怕的就是api!俗话说的好，不会偷懒的程序员不是好的程序员，我和小伙伴带来的这个框架就是为了节约大家上线前API测试的时间
---
##### 题外话 这里有一群志同道合的小伙伴，欢迎大家加入交流，QQ群:173999252

<img width="400" height="548" src="Image/qqqun.png"/>

---
##### 老套路 No picture you say a jb? 懒得录制gif 懂的人自然懂

<div align:left;display:inline;>
<img width="200" height="360" src="Image/1.jpg"/>
<img width="200" height="360" src="Image/2.jpg"/>
<img width="200" height="360" src="Image/3.jpg"/>
<img width="200" height="360" src="Image/4.jpg"/>
</div>

---
# 最重要的 怎么玩 在瞎逼逼什么
#### 1. 添加依赖在Module的build.gradle文件中：
``` groovy
    dependencies {

        //rxjava+retrofit2
        implementation 'io.reactivex:rxjava:1.3.0'
        implementation 'io.reactivex:rxandroid:1.2.1'
        implementation 'com.squareup.retrofit2:retrofit:2.0.1'
        implementation 'com.squareup.retrofit2:converter-gson:2.0.1'
        implementation 'com.squareup.retrofit2:adapter-rxjava:2.0.1'
        //基础框架
        implementation 'com.github.AndroidPureLand:AutoTest:v1.2'

    }
```
#### 2. 配置通用返回模型
```java
    //根据自己服务端的实际模型编写
    public class HttpMessage {

        private String status;
        private String msg;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
```
#### 3. 配置API地址
```java
    //使用retrofit的 自己项目里面肯定有现成的
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
```
#### 4. 配置请求列表
```java
    public class HttpRequestMethodConfig {

        //API接口类
        public ApiService service;

        //绑定API接口代理  该方法名 请勿修改
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
```
#### 5. Application初始化
```java
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
```
#### 6. 就是骚骚的使用了（你可以偷偷的在app里面加一个入口 只有你知道的那种 比如搜索 超哥你是大帅哥 就进入自动化测试 想想都爽）
```java
    <application
        android:name=".Application.MyApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.NoActionBar" >
        //毕竟是mode直接添加到AndroidManifest.xml 你们想怎么玩 你们随意啊
        <activity android:name="com.androidpureland.autotest.UI.Launcher.MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
```
#### 7. 看不明白就直接把代码拉下来看看  很简单的 祝你装逼成功

---

# 结语
> 框架还有诸多不完善的地方，欢迎大家反馈和意见，我们会逐步完善，当然如果你也感兴趣的话，我们欢迎你的加入，为开源贡献一份力量