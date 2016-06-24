package com.example.headlessfragment.dataholder;

import android.os.Bundle;

import com.example.headlessfragment.network.NetworkRequest;
import com.example.headlessfragment.network.RequestBean;

/**
 * Created by akhil on 02/02/16.
 */
public class CommentsDataKeepFragment extends DataKeepFragment{


    public static final String KEY_URL = "key_url";
    private static final String KEY_POSITION = "key_position";
    private int mPosition;

    public int getPosition() {
        return mPosition;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getArguments().getString(KEY_URL);
        mPosition = getArguments().getInt(KEY_POSITION);
        new NetworkRequest(new RequestBean(url, RequestBean.TYPE_COMMENTS),  this).execute();
        setRetainInstance(true);
    }

    public static CommentsDataKeepFragment newInstance(String url, int position, String progressMessage) {
        CommentsDataKeepFragment issueDataKeepFragment= new CommentsDataKeepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommentsDataKeepFragment.KEY_URL, url);
        bundle.putInt(CommentsDataKeepFragment.KEY_POSITION, position);
        bundle.putString(CommentsDataKeepFragment.KEY_PROGRESS_MESSAGE, progressMessage);
        issueDataKeepFragment.setArguments(bundle);
        return issueDataKeepFragment;
    }
}
