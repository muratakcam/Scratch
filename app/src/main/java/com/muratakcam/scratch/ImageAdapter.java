package com.muratakcam.scratch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends FirestoreRecyclerAdapter<PostModel, ImageAdapter.PostHolder> {
    private boolean isLiked = false;
    private DatabaseReference databaseReferenceLikes;
    private DatabaseReference databaseReferenceDislikes;
    private View.OnClickListener likeButtonListener;
    private View.OnClickListener dislikeButtonListener;

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
    protected void onBindViewHolder(@NonNull final PostHolder holder, final int position, @NonNull PostModel model) {
        databaseReferenceLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseReferenceDislikes = FirebaseDatabase.getInstance().getReference().child("Dislikes");

        holder.textView.setText(model.getPostTitle());
        Picasso.get()
                .load(model.getPostImageLink())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imageView);

        likeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceLikes.keepSynced(true);
                if (isLiked) {
                    holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
                } else {
                    holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
                }
                isLiked = true;
                databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isLiked) {
                            if (dataSnapshot.child(getSnapshots().getSnapshot(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                databaseReferenceLikes
                                        .child(getSnapshots().getSnapshot(position).getId())
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .removeValue();
                                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
                                isLiked = false;
                            } else {
                                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
                                isLiked = false;
                                databaseReferenceLikes
                                        .child(getSnapshots().getSnapshot(position).getId())
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        };
        dislikeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceDislikes.keepSynced(true);
                if (isLiked) {
                    holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_red);

                } else {
                    holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_white);
                }
                isLiked = true;
                databaseReferenceDislikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isLiked) {
                            if (dataSnapshot.child(getSnapshots().getSnapshot(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                databaseReferenceDislikes
                                        .child(getSnapshots().getSnapshot(position).getId())
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .removeValue();
                                holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_white);
                                isLiked = false;
                            } else {
                                holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_red);
                                isLiked = false;
                                databaseReferenceDislikes
                                        .child(getSnapshots().getSnapshot(position).getId())
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setLikeButton(holder);
            setDislikeButton(holder);
            holder.imageButton.setOnClickListener(likeButtonListener);
            holder.textView2.setOnClickListener(likeButtonListener);
            holder.imageButton2.setOnClickListener(dislikeButtonListener);
            holder.textView3.setOnClickListener(dislikeButtonListener);
        }


    }


    private void setLikeButton(final PostHolder holder) {
        databaseReferenceLikes.keepSynced(true);
        databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.textView2.setText((int) dataSnapshot.child(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()).getChildrenCount() + "");

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (dataSnapshot.child(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
                    } else {
                        holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setDislikeButton(final PostHolder holder) {
        databaseReferenceDislikes.keepSynced(true);
        databaseReferenceDislikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.textView3.setText((int) dataSnapshot.child(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()).getChildrenCount() + "");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (dataSnapshot.child(getSnapshots().getSnapshot(holder.getAdapterPosition()).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_red);
                    } else {
                        holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_white);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, viewGroup, false);
        return new PostHolder(v);
    }

    class PostHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView textView3;
        ImageButton imageButton;
        ImageButton imageButton2;
        TextView textView;
        ImageView imageView;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view_name);
            textView2 = itemView.findViewById(R.id.likecounttext);
            textView3 = itemView.findViewById(R.id.dislikecounttext);
            imageView = itemView.findViewById(R.id.image_view_upload);
            imageButton = itemView.findViewById(R.id.post_like);
            imageButton2 = itemView.findViewById(R.id.post_dislike);


        }
    }


}