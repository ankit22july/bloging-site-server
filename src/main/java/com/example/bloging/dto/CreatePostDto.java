package com.example.bloging.dto;

import java.time.Instant;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class CreatePostDto {

        @NotBlank(message = "Post Title cannot be blank")
        private String title;

        @NotBlank(message = "Post Content cannot be blank")
        private String content;

        private List<String> tags;

       private Boolean isPublished;
    
        // Getters and Setters
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

    }
