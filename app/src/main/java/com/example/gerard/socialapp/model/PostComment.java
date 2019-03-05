package com.example.gerard.socialapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PostComment {
    public String uid;
    public String author;
    public String authorPhotoUrl;
    public String content;

    public Map<String, Boolean> likes = new HashMap<>();

    public PostComment(){}

    public PostComment(String uid, String author, String authorPhotoUrl, String content) {
        this.uid = uid;
        this.author = author;
        this.authorPhotoUrl = authorPhotoUrl;
        this.content = content;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("authorPhotoUrl", authorPhotoUrl);
        result.put("content", content);
        result.put("likes", likes);

        return result;
    }
}
