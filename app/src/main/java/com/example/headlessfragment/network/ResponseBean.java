package com.example.headlessfragment.network;

import java.net.HttpURLConnection;

/**
 * Created by akhil on 02/02/16.
 */
public class ResponseBean {
    private Object mData;
    private int  mResponseCode;
    private String mErrorString;

    public Object getData() {
        return mData;
    }

    public void setData(Object data) {
        mData = data;
    }
    
    public boolean isSuccess() {
        return mResponseCode == HttpURLConnection.HTTP_OK;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = responseCode;
    }
    
    public String getErrorString() {
        return mErrorString;
    }

    public void setErrorString(String errorString) {
        mErrorString = errorString;
    }
}
