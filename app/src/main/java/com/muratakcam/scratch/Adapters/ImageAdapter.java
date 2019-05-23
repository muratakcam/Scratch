package com.muratakcam.scratch.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.arch.paging.PagedList;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muratakcam.scratch.CapsleApplication;
import com.muratakcam.scratch.CustomViews.CustomLinearLayoutManager;
import com.muratakcam.scratch.Models.CommentModel;
import com.muratakcam.scratch.Models.PostModel;
import com.muratakcam.scratch.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends FirestorePagingAdapter<PostModel, ImageAdapter.PostHolder> {

    private boolean isLiked = false;
    private DatabaseReference databaseReferenceLikes;
    private DatabaseReference databaseReferenceDislikes;
    private DatabaseReference databaseReferenceComments;
    private DatabaseReference databaseReferenceUsernames;
    private FirebaseFirestore databaseRef;
    private View.OnClickListener likeButtonListener;
    private View.OnClickListener dislikeButtonListener;
    private View view;
    private Boolean isUp;
    ArrayList<CommentModel> list;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ImageAdapter(@NonNull FirestorePagingOptions<PostModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PostHolder holder, final int position, @NonNull final PostModel model) {
        isUp = false;

        databaseReferenceLikes = FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseReferenceDislikes = FirebaseDatabase.getInstance().getReference().child("Dislikes");
        databaseReferenceComments = FirebaseDatabase.getInstance().getReference().child("Comments");
        databaseReferenceUsernames = FirebaseDatabase.getInstance().getReference().child("Usernames");
        databaseRef = FirebaseFirestore.getInstance();

        holder.textView.setText(model.getPostTitle());
        Picasso.get()
                .load(model.getPostImageLink())
                .placeholder(R.drawable.untitled1).fit()
                .into(holder.imageView);
        likeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReferenceLikes.keepSynced(true);
                isLiked = true;
                databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            if ((int) dataSnapshot.child(getItem(position).getId()).getChildrenCount() > 0) {
                                databaseRef.collection("Post").document(getItem(position).getId()).update("postPopular", model.getTime() + "");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            Log.i("2312312312312", "sdasda");
                        }

                        if (isLiked) {
                            //removing like
                            if (dataSnapshot.child(getItem(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                removeLikePost(holder, position);
                                isLiked = false;
                            }
                            //adding like
                            else {
                                likePost(holder, position);
                                removeDislikePost(holder, position);
                                isLiked = false;
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
                isLiked = true;
                databaseReferenceDislikes.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (isLiked) {
                            if (dataSnapshot.child(getItem(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                removeDislikePost(holder, position);
                                isLiked = false;
                            } else {
                                dislikePost(holder, position);
                                removeLikePost(holder, position);
                                isLiked = false;
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

            holder.imageButton.setOnClickListener(likeButtonListener);
            holder.textView2.setOnClickListener(likeButtonListener);
            holder.imageButton2.setOnClickListener(dislikeButtonListener);
            holder.textView3.setOnClickListener(dislikeButtonListener);
            if (holder.getAdapterPosition() != -1) {
                setLikeButton(holder, position);
                setDislikeButton(holder, position);
            }
        }
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.button.setEnabled(false);
                databaseReferenceComments
                        .child(getItem(position).getId())
                        .child(System.currentTimeMillis() + "")
                        .setValue(
                                new CommentModel(
                                        holder.editText.getText().toString()
                                        , getItem(position).getId()
                                        , FirebaseAuth.getInstance().getCurrentUser().getUid()
                                        , CapsleApplication.uname
                                        , System.currentTimeMillis())).addOnSuccessListener(new
                                                                                                    OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                            holder.button.setEnabled(true);

                                                                                                            holder.editText.setText("");
                                                                                                            setUpRecycleView(position, holder);
                                                                                                        }
                                                                                                    });


            }
        });
        holder.openCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.commentLayout.getVisibility() == View.GONE) {
                    holder.commentLayout.animate()
                            .translationY(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    holder.commentLayout.setVisibility(View.VISIBLE);
                                }
                            });
                } else {
                    holder.commentLayout.animate()
                            .translationY(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    holder.commentLayout.setVisibility(View.GONE);

                                }
                            });
                }
                //onSlideViewButtonClick(holder.commentLayout);
            }
        });
        setUpRecycleView(position, holder);
    }

    private void likePost(final PostHolder holder, final int position) {
        holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
        databaseReferenceLikes
                .child(getItem(position).getId())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
            }
        });
    }

    private void removeLikePost(final PostHolder holder, final int position) {
        databaseReferenceLikes
                .child(getItem(position).getId())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
            }
        });
        holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
    }

    private void dislikePost(final PostHolder holder, final int position) {
        holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_red);
        databaseReferenceDislikes
                .child(getItem(position).getId())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_down_white);
            }
        });
    }

    private void removeDislikePost(final PostHolder holder, final int position) {
        databaseReferenceDislikes
                .child(getItem(position).getId())
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .removeValue().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_down_red);
            }
        });
        holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_white);
    }


    private void setLikeButton(final PostHolder holder, final int position) {
        databaseReferenceLikes.keepSynced(true);
        databaseReferenceLikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    holder.textView2.setText((int) dataSnapshot.child(getItem(position).getId()).getChildrenCount() + "");
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (dataSnapshot.child(getItem(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_orange);
                        } else {
                            holder.imageButton.setImageResource(R.drawable.ic_arrow_drop_up_white);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.i("2312312312312", "sdasda");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setDislikeButton(final PostHolder holder, final int position) {
        databaseReferenceDislikes.keepSynced(true);
        databaseReferenceDislikes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    holder.textView3.setText((int) dataSnapshot.child(getItem(position).getId()).getChildrenCount() + "");
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (dataSnapshot.child(getItem(position).getId()).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_red);
                        } else {
                            holder.imageButton2.setImageResource(R.drawable.ic_arrow_drop_down_white);
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
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
        view = v;
        return new PostHolder(v);
    }

    private void setUpRecycleView(int position, final PostHolder holder) {
        Query query1 = databaseReferenceComments.child(getItem(position).getId()).orderByChild("time");
        list = new ArrayList<>();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.i("Just checkin'*****",dataSnapshot.toString());
                list.clear();
                for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                    try {
                        if (userSnapShot.getValue(CommentModel.class).getUname() != null)
                            list.add(userSnapShot.getValue(CommentModel.class));
                    } catch (NullPointerException n) {
                    }


                }
                //Log.i("Just checkin'*****", list.toString() + " - " + list.get(2).getComment() + " - " + list.get(2).getUname() + " - " + list.get(1).getComment() + " - " + list.get(1).getUname());


                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

                if (list.size() > 1)
                    holder.recyclerView.setAdapter(new CommentAdapter(view.getContext(), new ArrayList<>(list.subList(0, 2))));
                else holder.recyclerView.setAdapter(new CommentAdapter(view.getContext(), list));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                view.getHeight());                // toYDelta
        animate.setDuration(500);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(view);
        } else {
            slideUp(view);
        }
        isUp = !isUp;
    }

    class PostHolder extends RecyclerView.ViewHolder {
        TextView textView2;
        TextView textView3;
        ImageButton imageButton;
        ImageButton imageButton2;
        TextView textView;
        ImageView imageView;
        EditText editText;
        Button button;
        RecyclerView recyclerView;
        Button openCommentButton;
        View commentLayout;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.comment_button);
            editText = itemView.findViewById(R.id.comment_edit);
            textView = itemView.findViewById(R.id.text_view_name);
            textView2 = itemView.findViewById(R.id.likecounttext);
            textView3 = itemView.findViewById(R.id.dislikecounttext);
            imageView = itemView.findViewById(R.id.image_view_upload);
            imageButton = itemView.findViewById(R.id.post_like);
            imageButton2 = itemView.findViewById(R.id.post_dislike);
            recyclerView = itemView.findViewById(R.id.recyclerview_comment);
            openCommentButton = itemView.findViewById(R.id.opencommmentbutton);
            commentLayout = itemView.findViewById(R.id.comment_layout);

        }
    }
}