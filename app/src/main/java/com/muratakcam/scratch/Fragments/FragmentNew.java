package com.muratakcam.scratch.Fragments;

import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.muratakcam.scratch.Adapters.ImageAdapter;
import com.muratakcam.scratch.Models.PostModel;
import com.muratakcam.scratch.R;

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
        Query query = postRef.orderBy("time", Query.Direction.DESCENDING);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(1)
                .setPageSize(4)
                .build();
        FirestorePagingOptions<PostModel> options = new FirestorePagingOptions.Builder<PostModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, PostModel.class)
                .build();


        imageAdapter = new ImageAdapter(options);
        RecyclerView recycleView = view.findViewById(R.id.recyclerview_new);
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
