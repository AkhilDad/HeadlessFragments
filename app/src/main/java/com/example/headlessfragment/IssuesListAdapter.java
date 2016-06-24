package com.example.headlessfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.headlessfragment.model.Issue;

import java.util.List;

/**
 * Created by akhil on 02/02/16.
 */
public class IssuesListAdapter extends RecyclerView.Adapter<IssuesListAdapter.IssueItemViewHolder> {

    private final List<Issue> mIssueList;
    private final View.OnClickListener mOnClickListener;
    private Context mContext;

    public IssuesListAdapter(List<Issue> issueList, Context context, View.OnClickListener onClickListener) {
        this.mIssueList = issueList;
        mContext = context;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public IssueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue_list, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new IssueItemViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(IssueItemViewHolder holder, int position) {
        holder.bind(mIssueList.get(position));
    }

    @Override
    public int getItemCount() {
        return mIssueList == null ? 0 : mIssueList.size();
    }

    public static class IssueItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTV;
        private TextView mBodyTV;
        private TextView mCommentsCountTV;
        private Context mContext;
        public IssueItemViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mTitleTV = (TextView) itemView.findViewById(R.id.tv_title);
            mBodyTV = (TextView) itemView.findViewById(R.id.tv_body);
            mCommentsCountTV = (TextView) itemView.findViewById(R.id.tv_comments_count);
        }

        public void bind(Issue issue) {
            mTitleTV.setText(issue.getTitle());
            mBodyTV.setText(issue.getBody());
            mCommentsCountTV.setText(mContext.getResources().getQuantityString(R.plurals.comments_count, issue.getComments(), issue.getComments()));
        }
    }

}
