package com.example.bloging.dto;

import java.time.Instant;
import java.util.List;

import com.example.bloging.entities.Post;
import com.example.bloging.entities.Role;
import com.example.bloging.entities.User;

public class PostDto {


    private Long id;
    private String title;
    private String content;
    private String author;
    private List<String> tags;
    private Boolean isPublished;
    private Instant createdAt;
    private Instant updatedAt;

    public PostDto(Long id, String title, String content, String  author,List<String> tags,Boolean isPublished,Instant createdAt,Instant updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
        this.isPublished = isPublished;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostDto fromPost(Post post) {
        return new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(),post.getTags(),post.getIsPublished(),post.getCreatedAt(),post.getUpdatedAt());
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) { // Updated to use the new field name
        this.author = author;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters for createdAt/updatedAt are generally omitted
}