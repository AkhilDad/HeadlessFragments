package com.example.headlessfragment;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.headlessfragment.dataholder.CommentsDataKeepFragment;
import com.example.headlessfragment.dataholder.IssueDataKeepFragment;
import com.example.headlessfragment.model.Comment;
import com.example.headlessfragment.model.Issue;
import com.example.headlessfragment.network.NetworkResponseListener;
import com.example.headlessfragment.network.NetworkUtils;
import com.example.headlessfragment.network.RequestBean;
import com.example.headlessfragment.network.ResponseBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *Launcher activity which will handle all the issue loading
 */
public class MainActivity extends AppCompatActivity implements NetworkResponseListener {

    private static final String TAG_ISSUE_DATA_FRAGMENT = "ISSUES_DATA_FRAGMENT";
    private static final String TAG_COMMENTS_DATA_FRAGMENT = "COMMENTS_DATA_FRAGMENT" ;
    private static final String TAG_ISSUE_COMMENTS_DIALOG_FRAGMENT = "COMMENTS_DIALOG" ;
    private static final String URL_ISSUES = "https://api.github.com/repos/rails/rails/issues?sort=updated";
    private RecyclerView mIssueListRV;
    private IssuesListAdapter mIssuesListAdapter;
    private List<Issue> mIssueList;
    private FragmentManager mFragmentManager;
    private Button mRetryBtn;
    private TextView mErrorTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        mIssueListRV = (RecyclerView) findViewById(R.id.rv_issue_list);
        mRetryBtn = (Button)findViewById(R.id.btn_retry);
        mErrorTV = (TextView)findViewById(R.id.tv_error_message);
        mIssueList = new ArrayList<>();
        mIssuesListAdapter = new IssuesListAdapter(mIssueList, getApplicationContext(),new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mIssueListRV.getChildAdapterPosition(v);
                Issue issue = mIssueList.get(position);
                if (issue.getComments() <= 0) {
                    showToast(R.string.toast_no_comments_for_this_issue);
                } else if (issue.getCommentList() == null){
                    requestCommentLoading(position, issue);
                } else if (issue.getCommentList() != null) {
                    showCommentsDialog(issue.getCommentList());
                }
            }
        });
        mIssueListRV.setLayoutManager(new LinearLayoutManager(this));
        mIssueListRV.setAdapter(mIssuesListAdapter);
        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestIssueLoading();
            }
        });

        requestIssueLoading();
        resumeCommentsRequestIfExists();

    }

    /**
     * attach this activity to existing comment request if any
     */
    private void resumeCommentsRequestIfExists() {
        CommentsDataKeepFragment commentsDataKeepFragment= (CommentsDataKeepFragment)mFragmentManager.findFragmentByTag(TAG_COMMENTS_DATA_FRAGMENT);
        if(commentsDataKeepFragment != null) {
            commentsDataKeepFragment.setActivityNetworkResponseListenerWeakReference(new WeakReference<NetworkResponseListener>(MainActivity.this));
        }
    }

    /**
     * @param position
     * @param issue
     */
    private void requestCommentLoading(int position, Issue issue) {
        if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
            CommentsDataKeepFragment commentsDataKeepFragment = CommentsDataKeepFragment.newInstance(issue.getCommentsUrl(), position, getString(R.string.dialog_message_fetching_comments));
            mFragmentManager.beginTransaction().add(commentsDataKeepFragment, TAG_COMMENTS_DATA_FRAGMENT).commit();
            commentsDataKeepFragment.setActivityNetworkResponseListenerWeakReference(new WeakReference<NetworkResponseListener>(MainActivity.this));
        } else {
            showToast(R.string.err_no_network_connection);
        }
    }

    private void showToast(@StringRes int stringResId) {
        Toast.makeText(this, stringResId, Toast.LENGTH_SHORT).show();
    }

    /**
     * request issue loading from git hub using issue data keep fragment, if one already exists then
     * attach this activity to existing request
     */
    private void requestIssueLoading() {
        IssueDataKeepFragment issueDataKeepFragment = (IssueDataKeepFragment) mFragmentManager.findFragmentByTag(TAG_ISSUE_DATA_FRAGMENT);
        if (issueDataKeepFragment == null) {
            if (NetworkUtils.isNetworkConnected(getApplicationContext())) {
                hideError();
                issueDataKeepFragment = IssueDataKeepFragment.newInstance(URL_ISSUES, getString(R.string.dialog_message_fetching_issues));
                mFragmentManager.beginTransaction().add(issueDataKeepFragment, TAG_ISSUE_DATA_FRAGMENT).commit();
            } else {
                showError(R.string.err_no_network_connection);
            }
        }
        if (issueDataKeepFragment != null) {
            issueDataKeepFragment.setActivityNetworkResponseListenerWeakReference(new WeakReference<NetworkResponseListener>(this));
        }

    }

    private void showCommentsDialog(List<Comment> commentList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < commentList.size(); i++) {
            stringBuilder.append("<B>User : ").append(commentList.get(i).getUserName()).append("</B><br/>").append(commentList.get(i).getBody()).append("<br/><br/>");
        }
        CommentsDialogFragment.newInstance(stringBuilder.toString()).show(mFragmentManager, TAG_ISSUE_COMMENTS_DIALOG_FRAGMENT);
    }

    @Override
    public void onResponse(ResponseBean responseBean,@RequestBean.RequestType int reqType) {
        if (reqType == RequestBean.TYPE_ISSUE) {
            processIssueResponse(responseBean);
        }
        else if (reqType == RequestBean.TYPE_COMMENTS) {
            processCommentsResponse(responseBean);
        }
    }

    /**
     * Process the response of comments fetching request
     * @param responseBean
     */
    private void processCommentsResponse(ResponseBean responseBean) {
        CommentsDataKeepFragment commentsDataKeepFragment = (CommentsDataKeepFragment) mFragmentManager.findFragmentByTag(TAG_COMMENTS_DATA_FRAGMENT);
        if (responseBean.isSuccess()) {
            Object object = responseBean.getData();
            if (object != null && object instanceof Collection<?>) {
                List<Comment> commentList = (List<Comment>) object;
                mIssueList.get(commentsDataKeepFragment.getPosition()).setCommentList(commentList);
                showCommentsDialog(commentList);
            }
        } else {
            showToast(R.string.toast_no_comments_for_this_issue);
        }
        if (commentsDataKeepFragment != null) {
            mFragmentManager.beginTransaction().remove(commentsDataKeepFragment).commit();
        }
    }

    /**
     * Process the response of issue fetching request
     * @param responseBean
     */
    private void processIssueResponse(ResponseBean responseBean) {
        if (responseBean.isSuccess()) {
            Object object = responseBean.getData();
            if (object != null && object instanceof Collection<?>) {
                mIssueList.clear();
                mIssueList.addAll((Collection<? extends Issue>) object);
                mIssuesListAdapter.notifyDataSetChanged();
            }
        } else {
            IssueDataKeepFragment issueDataKeepFragment = (IssueDataKeepFragment) mFragmentManager.findFragmentByTag(TAG_ISSUE_DATA_FRAGMENT);
            if (issueDataKeepFragment != null) {
                mFragmentManager.beginTransaction().remove(issueDataKeepFragment).commit();
                mRetryBtn.setVisibility(View.VISIBLE);
            }
            showError(R.string.toast_issues_loading_failed);
        }
    }

    /**
     * show error and make retry button visible
     * @param errorMsgResId error message resource id
     */
    private void showError(@StringRes int errorMsgResId) {
        mRetryBtn.setVisibility(View.VISIBLE);
        mErrorTV.setVisibility(View.VISIBLE);
        mErrorTV.setText(errorMsgResId);
    }

    /**
     * hide error message and retry button
     */
    private void hideError() {
        mRetryBtn.setVisibility(View.GONE);
        mErrorTV.setVisibility(View.GONE);
    }
}
