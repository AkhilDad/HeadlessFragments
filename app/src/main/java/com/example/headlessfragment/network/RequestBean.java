package com.example.headlessfragment.network;

import android.support.annotation.IntDef;

/**
 * Created by akhil on 02/02/16.
 */
public class RequestBean {

    private String mUrl;

    public RequestBean(String url, int requestType) {
        mUrl = url;
        mRequestType = requestType;
    }

    private int mRequestType;
    public static final int TYPE_ISSUE = 0;
    public static final int TYPE_COMMENTS = 1;

    @IntDef({TYPE_ISSUE, TYPE_COMMENTS})
    public @interface RequestType {
    }

    @RequestType
    public int getRequestType() {
        return mRequestType;
    }

    public void setRequestType(@RequestType int requestType) {
        this.mRequestType = requestType;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
