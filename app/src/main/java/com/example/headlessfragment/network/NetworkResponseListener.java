package com.example.headlessfragment.network;

/**
 * Created by akhil on 02/02/16.
 */
public interface NetworkResponseListener {
    void onResponse(ResponseBean responseBean,@RequestBean.RequestType int requestType);
}
