package com.muratakcam.scratch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentNew extends android.support.v4.app.Fragment {
    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postRef = db.collection("Post");
    private ImageAdapter imageAdapter;



    public FragmentNew() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.new_fragment, container, false);
        if(container!=null)
        setUpRecycleView();

        return view;
    }

    private void setUpRecycleView() {
        Query query = postRef.orderBy("postTitle", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PostModel> options = new FirestoreRecyclerOptions.Builder<PostModel>()
                .setQuery(query, PostModel.class)
                .build();
        imageAdapter = new ImageAdapter(options);
        RecyclerView recycleView=view.findViewById(R.id.recyclerview_new);

        recycleView.setHasFixedSize(true);
        recycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycleView.setAdapter(imageAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        imageAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        imageAdapter.stopListening();
    }
}
