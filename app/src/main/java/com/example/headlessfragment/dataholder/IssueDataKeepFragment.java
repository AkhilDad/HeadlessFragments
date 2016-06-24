package com.example.headlessfragment.dataholder;

import android.os.Bundle;

import com.example.headlessfragment.network.NetworkRequest;
import com.example.headlessfragment.network.RequestBean;

/**
 * Created by akhil on 02/02/16.
 */
public class IssueDataKeepFragment extends DataKeepFragment{

    public static final String KEY_URL = "key_url";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getArguments().getString(KEY_URL);
        new NetworkRequest(new RequestBean(url, RequestBean.TYPE_ISSUE),this).execute();
        setRetainInstance(true);
    }

    public static IssueDataKeepFragment newInstance(String url, String progressMessage) {
        IssueDataKeepFragment issueDataKeepFragment= new IssueDataKeepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IssueDataKeepFragment.KEY_URL, url);
        bundle.putString(CommentsDataKeepFragment.KEY_PROGRESS_MESSAGE, progressMessage);
        issueDataKeepFragment.setArguments(bundle);
        return issueDataKeepFragment;
    }
}
