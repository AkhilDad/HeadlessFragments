package com.example.headlessfragment.model;

import java.util.List;

/**
 * Created by akhil on 02/02/16.
 */

public class Issue {
    private long mId;
    private int mComments;
    private String mCommentsUrl;
    private String mTitle;
    private String mBody;
    private List<Comment> commentList;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public int getComments() {
        return mComments;
    }

    public void setComments(int comments) {
        mComments = comments;
    }

    public String getCommentsUrl() {
        return mCommentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        mCommentsUrl = commentsUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
