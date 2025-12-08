package com.example.bloging.services;

import com.example.bloging.dto.CreatePostDto;
import com.example.bloging.dto.PostDto;
import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;
import com.example.bloging.exceptions.ResourceNotFoundException;
import com.example.bloging.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    // Constructor manually created (no Lombok)
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(CreatePostDto dto, User user) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(user);
        post.setTags(dto.getTags());
        post.setIsPublished(dto.getIsPublished());
        return postRepository.save(post);
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("ID not found with post: " + postId));
    }

    public List<Post> findPostsByUserId(User author) {
        return postRepository.findByAuthor(author);
    }
}
