package com.muratakcam.scratch.Models;

import com.google.firebase.database.ServerValue;

public class PostModel {
    private String postId;

    private String postTitle;

    private String postImageLink;

    private String postOwner;

    private int postUpvote;

    private int postDownvote;

    private Object time;

    public PostModel() {
    }

    public PostModel(String postTitle, String postImageLink, String postOwner) {
        this.postTitle = postTitle;
        this.postImageLink = postImageLink;
        this.postOwner = postOwner;
        this.postUpvote = 0;
        this.postDownvote = 0;
        this.time = System.currentTimeMillis();
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostImageLink() {
        return postImageLink;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostImageLink(String postImageLink) {
        this.postImageLink = postImageLink;
    }

    public String getPostOwner() {
        return postOwner;
    }

    public void setPostOwner(String postOwner) {
        this.postOwner = postOwner;
    }

    public int getPostUpvote() {
        return postUpvote;
    }

    public void setPostUpvote(int postUpvote) {
        this.postUpvote = postUpvote;
    }

    public int getPostDownvote() {
        return postDownvote;
    }

    public void setPostDownvote(int postDownvote) {
        this.postDownvote = postDownvote;
    }

    public Object getTime() {
        return time;
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
