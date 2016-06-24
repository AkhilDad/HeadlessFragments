package com.example.headlessfragment.dataholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.headlessfragment.network.NetworkResponseListener;
import com.example.headlessfragment.network.RequestBean;
import com.example.headlessfragment.network.ResponseBean;

import java.lang.ref.WeakReference;

/**
 * Created by akhil on 03/02/16.
 */

/**
 * This fragment is used to handle network request and persist data during orientation changes
 */
public class DataKeepFragment extends Fragment implements NetworkResponseListener {

    protected WeakReference<NetworkResponseListener> mActivityNetworkResponseListenerWeakReference;
    protected ResponseBean mResponseBean;
    protected @RequestBean.RequestType int mReqType;
    protected Context mContext;
    protected ProgressDialog mProgressDialog;
    protected static final String KEY_PROGRESS_MESSAGE = "KEY_PROGRESS_MESSAGE";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String progressMessage = getArguments().getString(KEY_PROGRESS_MESSAGE);
        if (mResponseBean == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(progressMessage);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResponse(ResponseBean responseBean, @RequestBean.RequestType int reqType) {
        mResponseBean = responseBean;
        mReqType = reqType;
        NetworkResponseListener networkResponseListener = mActivityNetworkResponseListenerWeakReference.get();
        if (networkResponseListener != null && mResponseBean != null) {
            dismissProgressDialog();
            networkResponseListener.onResponse(responseBean, reqType);
        }
    }

    public void setActivityNetworkResponseListenerWeakReference(WeakReference<NetworkResponseListener> activityNetworkResponseListenerWeakReference) {
        mActivityNetworkResponseListenerWeakReference = activityNetworkResponseListenerWeakReference;
        NetworkResponseListener networkResponseListener = mActivityNetworkResponseListenerWeakReference.get();
        if (networkResponseListener != null && mResponseBean != null) {
            networkResponseListener.onResponse(mResponseBean,mReqType);
            dismissProgressDialog();
        }
    }

    @Override
    public void onDestroyView() {
        dismissProgressDialog();
        super.onDestroyView();
    }

    private void dismissProgressDialog() {
        if (mProgressDialog !=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
}
