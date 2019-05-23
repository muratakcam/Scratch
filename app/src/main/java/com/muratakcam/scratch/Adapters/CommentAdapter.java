package com.muratakcam.scratch.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.muratakcam.scratch.Models.CommentModel;
import com.muratakcam.scratch.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private Context context;
    private ArrayList<CommentModel> newsList;

    public CommentAdapter(Context mContext, ArrayList<CommentModel> mNewsList) {
        context=mContext;
        newsList=mNewsList;
    }



    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
        return new CommentAdapter.CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int i) {
        holder.commentContent.setText(newsList.get(i).getComment());
        holder.commentUname.setText(newsList.get(i).getUname());
        Log.i("Just checkin'111111", "  "+newsList.get(i).getComment()+"  "+newsList.get(i).getUname());

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
    TextView commentUname;
    TextView commentContent;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentContent=itemView.findViewById(R.id.comment_content);
            commentUname=itemView.findViewById(R.id.comment_uname);
        }
    }
}
