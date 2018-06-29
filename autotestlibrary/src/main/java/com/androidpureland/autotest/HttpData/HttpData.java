package com.androidpureland.autotest.HttpData;

import android.os.Handler;
import android.os.Looper;

import com.androidpureland.autotest.Entity.APIConfig;
import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.Bean.NetworkResponseEntity;
import com.androidpureland.autotest.HttpData.Dao.OnInterceptCallBack;
import com.androidpureland.autotest.HttpData.Dao.OnInterfaceTestPrepareCallBack;
import com.androidpureland.autotest.HttpData.Dao.OnUpdateItemCallBack;
import com.androidpureland.autotest.HttpData.Dao.onDataHandlingCallBack;
import com.androidpureland.autotest.Util.StringUtils;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Http测试配置层
 */
public class HttpData {
    //Retrofit构造器
    private Retrofit.Builder retrofitBuilder;
    //OKHttp实例
    private OkHttpClient okHttpClient;
    //OKhttp拦截器
    private OkHttpInterceptor okHttpInterceptor;
    //OKhttp拦截器回调
    private OnInterceptCallBack onInterceptCallBack;
    //主线程切换器
    private Handler handler = new Handler(Looper.myLooper());
    //初始化测试回调
    private OnInterfaceTestPrepareCallBack callBack;
    //接口请求队列
    private List<APIConfig> observableList = new ArrayList<>();
    //服务器域名队列
    private List<BaseUrlEntity> baseUrlEntityList = new ArrayList<>();
    //参数构造器
    private Bulder bulder;
    //API接口代理
    private Proxy proxy;
    //当前选中的队列索引
    private int whichOne;


    /**
     * 初始化API接口代理
     *
     * @param whichOne 服务器域名队列中的某一个域名
     * @return
     */
    public void initURL(int whichOne) {
        if (null == baseUrlEntityList || baseUrlEntityList.size() == 0) {
            throw new UnsupportedOperationException("未在服务器域名队列中添加域名");
        }
        if (whichOne > baseUrlEntityList.size() - 1 || whichOne < 0) {
            throw new UnsupportedOperationException("定义的下标超出服务器域名队列数量");
        }
        this.whichOne = whichOne;
        if (null == retrofitBuilder) {
            //初始化OKHttp，可以由用户在Application中自定义配置好传进来，这里仅仅提供一个默认配置好的OKHttp
            if (null == bulder.okHttpClient) {
                //初始化OKHttp拦截器，不对外暴露设置接口，可以由用户在Application中跟随OKHttp设置好，使用决定权在于用户
                if (null == okHttpInterceptor) {
                    okHttpInterceptor = new TacitlyOkHttpInterceptor();
                    okHttpInterceptor.setCallBack(onInterceptCallBack);
                }
                okHttpClient = OkHttp3Utils
                        .getInstance()
                        .writeTimeout(60)
                        .readTimeout(30)
                        .writeTimeout(30)
                        .addInterceptor(okHttpInterceptor)
                        .build();

            } else {
                okHttpClient = bulder.okHttpClient;
            }
            retrofitBuilder = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(baseUrlEntityList.get(this.whichOne).getUrl())
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //设置使用okhttp网络请求
                    .client(okHttpClient);
            if (okHttpInterceptor != null) {
                okHttpInterceptor.setCallBack(new OnInterceptCallBack() {
                    @Override
                    public void onIntercept( NetworkResponseEntity networkResponseInfo) {
                        for (APIConfig apiConfig:observableList){
                            if(networkResponseInfo.getUrl().contains(apiConfig.getAddress())){
                                apiConfig.setNetworkResponseEntity(networkResponseInfo);
                            }
                        }
                    }
                });
            }
        }
        retrofitBuilder.baseUrl(baseUrlEntityList.get(this.whichOne).getUrl());
        try {
            proxy = (Proxy) retrofitBuilder.build().create(Class.forName(bulder.apiServiceClassPath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("未找到API接口类，请检查Class路径是否错误");
        }

    }

    /**
     * 在访问HttpMethods时创建单例
     */
    private static class SingletonHolder {
        private static final HttpData INSTANCE = new HttpData();
    }

    /**
     * 设置测试回调
     *
     * @param callBack
     */
    public void setOnInterfaceTestCallBack(OnInterfaceTestPrepareCallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * 设置OKHttp拦截器回调
     *
     */
    public void setOnOkHttpInterceptor(OkHttpInterceptor onOkHttpInterceptor) {
        this.okHttpInterceptor = onOkHttpInterceptor;
        if(okHttpInterceptor!=null) okHttpInterceptor.setCallBack(onInterceptCallBack);
    }
    /**
     * 获取单例
     *
     * @return
     */
    public static HttpData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 获取服务器域名队列
     *
     * @return
     */
    public List<BaseUrlEntity> getBaseUrlEntityList() {
        return baseUrlEntityList;
    }

    /**
     * 获取当前请求的域名下标
     *
     * @return
     */
    public int getWhichOne() {
        return whichOne;
    }

    /**
     * 设置参数
     *
     * @param bulder
     */
    public void builder(Bulder bulder) {
        this.bulder = bulder;
        baseUrlEntityList = bulder.baseUrlEntityList;

    }

    /**
     * 获取所有接口
     *
     * @return
     */
    public List getObservableList() {
        return observableList;
    }


    /**
     * 检查参数是否设置正确
     */
    protected void check() {
        if (bulder == null) {
            throw new UnsupportedOperationException("未在Application中初始化参数构造器");
        }
        if (bulder.apiServiceClassPath == null || bulder.requestClassPath == null) {
            throw new UnsupportedOperationException("未在Application中初始化API接口类或参数请求类的Class路径");
        }
        if (baseUrlEntityList == null || baseUrlEntityList.size() == 0) {
            throw new UnsupportedOperationException("未在Application中初始化服务器域名");
        }
        if (proxy == null) {
            throw new UnsupportedOperationException("请在使用前先调用 initURL（int whichOne）方法");
        }
    }

    /**
     * 遍历要测试的接口信息
     */
    public void prepareTest() {
        check();
        observableList.clear();
        if (callBack != null) {
            callBack.onPrepareStart(baseUrlEntityList.get(whichOne));
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Class c = Class.forName(bulder.requestClassPath);
                    Method[] ms = c.getDeclaredMethods();
                    Object o = c.newInstance();
                    boolean isExist = false;
                    //查找用户自定义的参数请求类中是否存在绑定代理的方法
                    for (int i = 0; i < ms.length; i++) {
                        if ("bindAPIProxy".equals(ms[i].getName())) {
                            c.getMethod("bindAPIProxy", Proxy.class).invoke(o, proxy);
                            isExist = true;
                            break;
                        }
                    }
                    if (isExist) {
                        //如果存在绑定代理的方法，则循环遍历用户自定义参数请求类中的除bindAPIProxy之外的所有无参方法，并将其返回的自定义的泛型加入到请求队列中
                        for (int i = 0; i < ms.length; i++) {
                            if (!"bindAPIProxy".equals(ms[i].getName())) {
                                observableList.add((APIConfig) c.getDeclaredMethod(ms[i].getName()).invoke(o));
                            }
                        }
                    } else {
                        throw new UnsupportedOperationException(
                                "未在参数请求类中找到bindAPIProxy（Proxy proxy）方法，请检查是否包含此方法。\n" +
                                        "请注意：proxy参数即为API接口类，通过强转即可\n" +
                                        "例：\n" +
                                        " public void bindAPIProxy(Proxy proxy) {\n" +
                                        "    this.apiService = (ApiService) proxy;\n" +
                                        " }\n"
                        );
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("发现自定义的参数请求类中存在有参方法，请将所有方法都设置成无参方法，错误信息：\n" + e.getLocalizedMessage());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("参数请求类中方法未设置public修饰符，错误信息：\n" + e.getLocalizedMessage());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("类实例化出错，错误信息：" + e.getLocalizedMessage());
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("参数请求类中的方法调用错误，请检查方法是否为无参方法，错误信息：\n" + e.getLocalizedMessage());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new UnsupportedOperationException("未找到参数请求类，请检查Class路径是否错误，错误信息：\n" + e.getLocalizedMessage());
                }
                if (callBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onPrepareComplete(baseUrlEntityList.get(whichOne), observableList);
                        }
                    });
                }
            }
        }.start();

    }


    /**
     * 单条测试
     */
    public void test(final APIConfig apiConfig, final OnUpdateItemCallBack onUpdateItemCallBack) {
        setSubscribe(apiConfig.getObservable(), new Subscriber() {
            @Override
            public void onStart() {
                super.onStart();
                onUpdateItemCallBack.onStart();
                apiConfig.setTime(System.currentTimeMillis());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                bulder.dataHandlingCallBack.onError(apiConfig,e);
                onUpdateItemCallBack.onError(apiConfig);
            }

            @Override
            public void onNext(Object message) {
                bulder.dataHandlingCallBack.onNext(apiConfig,message);
                onUpdateItemCallBack.onError(apiConfig);

            }
        });
    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    private <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

    /**
     * 参数构造器
     */
    public static class Bulder {
        //OKHttp实例
        private OkHttpClient okHttpClient;
        //API接口类Class路径
        private String apiServiceClassPath;
        //参数请求类Class路径
        private String requestClassPath;
        //OKhttp拦截器
        private OkHttpInterceptor okHttpInterceptor;
        //服务器域名队列
        private List<BaseUrlEntity> baseUrlEntityList = new ArrayList<>();
        //数据处理回调
        private onDataHandlingCallBack dataHandlingCallBack;
        /**
         * 设置OKHttp实例
         *
         * @param okHttpClient
         */
        public Bulder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }


        /**
         * 设置服务器API类名路径
         *
         * @param apiServiceClassPath
         * @return
         */
        public Bulder setApiService(String apiServiceClassPath) {
            this.apiServiceClassPath = apiServiceClassPath;
            return this;
        }

        /**
         * 设置请求类名路径
         *
         * @param requestClassPath
         * @return
         */
        public Bulder setRequest(String requestClassPath) {
            this.requestClassPath = requestClassPath;
            return this;
        }

        /*
        * 处理数据回调
        * */
        public Bulder onDataHandlingCallBack(onDataHandlingCallBack dataHandlingCallBack){
            this.dataHandlingCallBack=dataHandlingCallBack;
            return this;
        }
        /**
         * 设置服务器域名信息
         *
         * @param baseUrlEntity
         * @return
         */
        public Bulder setBaseUrlEntity(BaseUrlEntity baseUrlEntity) {
            if (baseUrlEntity != null) {
                if (!StringUtils.isEmpty(baseUrlEntity.getUrl()) && !StringUtils.isEmpty(baseUrlEntity.getUrlName()))
                    if (baseUrlEntity.getUrl().startsWith("http://") || baseUrlEntity.getUrl().startsWith("https://")) {
                        baseUrlEntityList.add(baseUrlEntity);
                    }
            }
            return this;
        }
        /*
        * 设置OKhttp拦截器
        * */
        public Bulder setOkHttpInterceptor(OkHttpInterceptor okHttpInterceptor) {
            this.okHttpInterceptor = okHttpInterceptor;
            return this;
        }
        public Bulder build(){
            if(okHttpInterceptor!=null){
                HttpData.getInstance().setOnOkHttpInterceptor(okHttpInterceptor);
            }
            return this;
        }
    }

    /**
     * 临时销毁
     */
    public void clear() {
        if (observableList != null) {
            observableList.clear();
        }
        callBack = null;
        retrofitBuilder = null;
        okHttpClient = null;
    }

    /**
     * 释放所有垃圾
     */
    public void release() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
        if (observableList != null) {
            observableList.clear();
        }
        observableList = null;
        if (baseUrlEntityList != null) {
            baseUrlEntityList.clear();
        }
        baseUrlEntityList = null;
        callBack = null;
        retrofitBuilder = null;
        okHttpClient = null;
        bulder = null;
        proxy = null;
    }
}
