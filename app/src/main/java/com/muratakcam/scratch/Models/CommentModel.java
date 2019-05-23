package com.muratakcam.scratch.Models;

public class CommentModel {
    private String comment, postId, uid, uname, time;

    public CommentModel() {
    }

    public CommentModel(String comment, String postId, String uid, String uname, Long time) {
        this.comment = comment;
        this.postId = postId;
        this.uid = uid;
        this.uname = uname;
        this.time = time+"";
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
