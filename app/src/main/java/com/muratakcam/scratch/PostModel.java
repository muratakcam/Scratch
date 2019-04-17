package com.muratakcam.scratch;

public class PostModel {

    private String postTitle;

    private String postImageLink;

    public PostModel() {
    }

    public PostModel(String postTitle, String postImageLink) {
        this.postTitle = postTitle;
        this.postImageLink = postImageLink;
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

}
