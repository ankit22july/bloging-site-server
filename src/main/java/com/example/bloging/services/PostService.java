package com.example.bloging.services;

import com.example.bloging.dto.CreatePostDto;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    // Constructor manually created (no Lombok)
    public PostService() {
    }

    public String createPost(CreatePostDto dto, String username) {
        return "Post created successfully by " + username;
    }
}
