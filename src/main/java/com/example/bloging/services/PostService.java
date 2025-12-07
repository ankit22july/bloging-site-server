package com.example.bloging.services;

import com.example.bloging.dto.CreatePostDto;
import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;
import com.example.bloging.repositories.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    // Constructor manually created (no Lombok)
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(CreatePostDto dto, User user ) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(user);
        return postRepository.save(post);
    }
}
