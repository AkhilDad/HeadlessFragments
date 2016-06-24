package com.example.headlessfragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CommentsDialogFragment extends DialogFragment{

    private static final String KEY_COMMENTS = "Comment";

    public CommentsDialogFragment() {
        // Required empty public constructor
    }

    public static CommentsDialogFragment newInstance(String commentString) {
        CommentsDialogFragment fragment = new CommentsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_COMMENTS, commentString);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String comments = getArguments().getString(KEY_COMMENTS);
        TextView commentsTV = ((TextView) getView().findViewById(R.id.tv_comments));
        commentsTV.setText(Html.fromHtml(comments));
        //enable scrolling in text view
        commentsTV.setMovementMethod(new ScrollingMovementMethod());
    }
}
