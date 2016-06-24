package com.example.headlessfragment.model;

/**
 * Created by akhil on 02/02/16.
 */
public class Comment {
    private long mId;
    private String mUserName;
    private String mBody;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }
}
