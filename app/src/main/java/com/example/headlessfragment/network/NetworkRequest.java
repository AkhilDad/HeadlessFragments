package com.example.headlessfragment.network;

import android.os.AsyncTask;

import com.example.headlessfragment.model.Comment;
import com.example.headlessfragment.model.Issue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by akhil on 02/02/16.
 */
public class NetworkRequest extends AsyncTask<Void, Void, ResponseBean> {

    private static final int TIMEOUT_MILLIS = 30 * 1000; // 30 seconds
    private NetworkResponseListener mNetworkResponseListener;
    private RequestBean mRequestBean;
    public NetworkRequest(RequestBean requestBean,NetworkResponseListener networkResponseListener) {
        mRequestBean = requestBean;
        mNetworkResponseListener = networkResponseListener;
    }

    @Override
    protected ResponseBean doInBackground(Void... params) {
        String url = mRequestBean.getUrl();
        ResponseBean responseBean = new ResponseBean();
        HttpURLConnection respObj = sendGetRequest(url);
        InputStream inputStream;
        if (respObj != null) {
            try {
                int statusCode = respObj.getResponseCode();
                responseBean.setResponseCode(statusCode);
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = respObj.getInputStream();
                    if (inputStream != null) {
                        responseBean.setData(processResponse(NetworkUtils.readAsString(inputStream),
                                mRequestBean.getRequestType()));
                        inputStream.close();
                    }

                } else {
                    inputStream = respObj.getErrorStream();
                    if (inputStream != null) {
                        responseBean.setErrorString(NetworkUtils.readAsString(inputStream));
                        inputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return responseBean;
    }

    @Override
    protected void onPostExecute(ResponseBean responseBean) {
        super.onPostExecute(responseBean);
        mNetworkResponseListener.onResponse(responseBean, mRequestBean.getRequestType());
    }

    private Object processResponse(String response, int requestType) {
        if (requestType == RequestBean.TYPE_ISSUE) {
            ArrayList<Issue> issueList = null;
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    Issue issue;
                    int length = jsonArray.length();
                    issueList = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        issue = new Issue();
                        if (!jsonObject.isNull(ResponseKeys.ID)) {
                            issue.setId(jsonObject.getLong(ResponseKeys.ID));
                        }
                        if (!jsonObject.isNull(ResponseKeys.BODY)) {
//                        issue.setBody(getSubString(jsonObject.getString(ResponseKeys.BODY), 140));
                            issue.setBody(jsonObject.getString(ResponseKeys.BODY));
                        }
                        if (!jsonObject.isNull(ResponseKeys.TITLE)) {
                            issue.setTitle(jsonObject.getString(ResponseKeys.TITLE));
                        }
                        if (!jsonObject.isNull(ResponseKeys.COMMENTS_URL)) {
                            issue.setCommentsUrl(jsonObject.getString(ResponseKeys.COMMENTS_URL));
                        }
                        if (!jsonObject.isNull(ResponseKeys.COMMENTS)) {
                            issue.setComments(jsonObject.getInt(ResponseKeys.COMMENTS));
                        }
                        issueList.add(issue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return issueList;
        } else if (requestType == RequestBean.TYPE_COMMENTS) {
            ArrayList<Comment> commentList = null;
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    Comment comment;
                    int length = jsonArray.length();
                    commentList = new ArrayList<>(length);
                    for (int i = 0; i < length; i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        comment = new Comment();
                        if (!jsonObject.isNull(ResponseKeys.ID)) {
                            comment.setId(jsonObject.getLong(ResponseKeys.ID));
                        }
                        if (!jsonObject.isNull(ResponseKeys.BODY)) {
                            comment.setBody(jsonObject.getString(ResponseKeys.BODY));
                        }
                        if (!jsonObject.isNull(ResponseKeys.USER)) {
                            JSONObject userObject = jsonObject.getJSONObject(ResponseKeys.USER);
                            if (!userObject.isNull(ResponseKeys.LOGIN)) {
                                comment.setUserName(userObject.getString(ResponseKeys.LOGIN));
                            }

                        }
                        commentList.add(comment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return commentList;
        }
        return null;
    }

    private String getSubString(String string, int limit) {
        return string.length() > limit ? string.substring(0, limit-1) : string;
    }

    public static HttpURLConnection sendGetRequest(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url).openConnection());
            connection.setConnectTimeout(TIMEOUT_MILLIS);
            connection.setReadTimeout(TIMEOUT_MILLIS);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");
            return connection;

        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
