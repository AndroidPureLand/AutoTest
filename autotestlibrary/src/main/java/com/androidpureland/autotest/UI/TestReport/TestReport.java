package com.androidpureland.autotest.UI.TestReport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidpureland.autotest.HttpData.Bean.BaseUrlEntity;
import com.androidpureland.autotest.HttpData.Bean.NetworkResponseEntity;
import com.androidpureland.autotest.HttpData.HttpData;
import com.androidpureland.autotest.R;
import com.androidpureland.autotest.Util.StringUtils;


/**
 * Created by Administrator on 2018/6/24.
 */
@SuppressLint("all")
public class TestReport extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;
    private TextView testFullUrl;
    private TextView txt;
    NetworkResponseEntity networkResponseEntity;
    String httpMessage;
    int httpCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);

        back = (ImageButton) findViewById(R.id.back);
        testFullUrl = (TextView) findViewById(R.id.test_full_url);
        txt = (TextView) findViewById(R.id.txt);
        back.setOnClickListener(this);
        testFullUrl.setOnClickListener(this);

        networkResponseEntity = getIntent().getExtras().getParcelable("networkResponseEntity");
        httpMessage = getIntent().getExtras().getString("result_message");
        httpCode=getIntent().getExtras().getInt("result_code");
        if (networkResponseEntity!=null){
            //详细报告
            testFullUrl.setText(networkResponseEntity.getUrl());
            txt.setText(
                    "测试域名：" + ((BaseUrlEntity) HttpData.getInstance().getBaseUrlEntityList().get(HttpData.getInstance().getWhichOne())).getUrl() + "\n" +
                            "接口地址：" + getIntent().getExtras().getString("address") + "\n" +
                            "接口描述：" + getIntent().getExtras().getString("introduce") + "\n" +
                            "耗费时间：" + getIntent().getExtras().getLong("time") + "(ms)\n" +
                            "是否与服务器交互成功：" + networkResponseEntity.isSuccessful() + "\n" +
                            "连接是否被重定向：" + networkResponseEntity.isRedirect() + "\n" +
                            "是否为https连接：" + networkResponseEntity.isHttps() + "\n" +
                            "请求类型：" + networkResponseEntity.getMethod() + "\n" +
                            "头部信息：" + networkResponseEntity.getHeaders() + "\n" +
                            "请求协议：" + networkResponseEntity.getProtocol() + "\n" +
                            "请求代码：" + networkResponseEntity.getCode() + "\n" +
                            "服务器返回代码：" + httpCode + "\n" +
                            "请求返回信息：" + networkResponseEntity.getMessage() + "\n" +
                            "错误信息：" + httpMessage + "\n"+
                            "服务器返回信息："  + "\n"
            );
        }else {
            testFullUrl.setText(((BaseUrlEntity)HttpData.getInstance().getBaseUrlEntityList().get( HttpData.getInstance().getWhichOne())).getUrl()+
                    getIntent().getExtras().getString("address"));
            //简单报告
            txt.setText(
                    "测试域名：" + ((BaseUrlEntity)HttpData.getInstance().getBaseUrlEntityList().get( HttpData.getInstance().getWhichOne())).getUrl() + "\n" +
                    "接口地址：" + getIntent().getExtras().getString("address") + "\n" +
                    "接口描述：" + getIntent().getExtras().getString("introduce") + "\n" +
                    "请求结果：" + getIntent().getExtras().getBoolean("successOrFail") + "\n" +
                    "耗费时间：" + getIntent().getExtras().getLong("time") + "\n" +
                    "服务器返回代码：" + httpMessage + "\n" +
                    "错误信息：" + httpCode + "\n"
            );
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.back) {
            finish();

        } else if (i == R.id.test_full_url) {
            if (!StringUtils.isEmpty(testFullUrl.getText().toString().trim())) {
                startActivity(new Intent().setAction("android.intent.action.VIEW").setData(Uri.parse(testFullUrl.getText().toString().trim())));
            }

        }
    }
}
