package com.example.bloging.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePostDto {

        @NotBlank(message = "Post Title cannot be blank")
        private String title;

        @NotBlank(message = "Post Content cannot be blank")
        private String content;

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
    }
