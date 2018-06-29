package com.androidpureland.autotest.HttpData.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import okhttp3.ResponseBody;

/**
 * 接口请求返回值信息
 * Created by Administrator on 2018/6/25.
 */

public class NetworkResponseEntity implements Parcelable {
    private String message;//访问消息
    private String url;//完整地址
    private int code;//访问代码
    private boolean isRedirect;//访问地址是否被重定向
    private boolean isSuccessful;//访问是否成功
    private boolean isHttps;//是否为https连接
    private String protocol;//协议
    private String method;//get or post
    private String headers;//头部信息
    private byte[] content=new byte[0];//服务器返回内容
    private ResponseBody responseBody;

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHttps() {
        return isHttps;
    }

    public void setHttps(boolean https) {
        isHttps = https;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public static Creator<NetworkResponseEntity> getCREATOR() {
        return CREATOR;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isRedirect() {
        return isRedirect;
    }

    public void setRedirect(boolean redirect) {
        isRedirect = redirect;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.url);
        dest.writeInt(this.code);
        dest.writeByte(this.isRedirect ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSuccessful ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isHttps ? (byte) 1 : (byte) 0);
        dest.writeString(this.protocol);
        dest.writeString(this.method);
        dest.writeString(this.headers);
        dest.writeByteArray(this.content);
    }

    public NetworkResponseEntity() {
    }

    protected NetworkResponseEntity(Parcel in) {
        this.message = in.readString();
        this.url = in.readString();
        this.code = in.readInt();
        this.isRedirect = in.readByte() != 0;
        this.isSuccessful = in.readByte() != 0;
        this.isHttps = in.readByte() != 0;
        this.protocol = in.readString();
        this.method = in.readString();
        this.headers = in.readString();
        this.content = in.createByteArray();
    }

    public static final Creator<NetworkResponseEntity> CREATOR = new Creator<NetworkResponseEntity>() {
        @Override
        public NetworkResponseEntity createFromParcel(Parcel source) {
            return new NetworkResponseEntity(source);
        }

        @Override
        public NetworkResponseEntity[] newArray(int size) {
            return new NetworkResponseEntity[size];
        }
    };

    @Override
    public String toString() {
        return "NetworkResponseEntity{" +
                "message='" + message + '\'' +
                ", url='" + url + '\'' +
                ", code=" + code +
                ", isRedirect=" + isRedirect +
                ", isSuccessful=" + isSuccessful +
                ", isHttps=" + isHttps +
                ", protocol='" + protocol + '\'' +
                ", method='" + method + '\'' +
                ", headers='" + headers + '\'' +
                ", content=" + Arrays.toString(content) +
                ", responseBody=" + responseBody +
                '}';
    }
}
