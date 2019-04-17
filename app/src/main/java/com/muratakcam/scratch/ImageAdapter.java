package com.muratakcam.scratch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends FirestoreRecyclerAdapter<PostModel, ImageAdapter.PostHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageAdapter(@NonNull FirestoreRecyclerOptions<PostModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PostHolder holder, int position, @NonNull PostModel model) {

        holder.textView.setText(model.getPostTitle());
        Picasso.get()
                .load(model.getPostImageLink())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item,viewGroup,false);
        return new PostHolder(v);
    }

    class PostHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view_name);
            imageView=itemView.findViewById(R.id.image_view_upload);
        }
    }


}