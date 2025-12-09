package com.example.bloging.services;

import com.example.bloging.dto.CreatePostDto;
import com.example.bloging.dto.PostDto;
import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;
import com.example.bloging.exceptions.ResourceNotFoundException;
import com.example.bloging.repositories.PostRepository;

import jakarta.transaction.Transactional;

import java.nio.file.AccessDeniedException;
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



    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_MAINTAINER = "ROLE_MAINTAINER";

    @Transactional
    public PostDto deletePostById(Long postId, User author) throws AccessDeniedException {
        Post postToDelete = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        boolean isAdminOrMaintainer = author.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN) ||
                        authority.getAuthority().equals(ROLE_MAINTAINER));
        if (!isAdminOrMaintainer) {

            if (!postToDelete.getAuthor().getId().equals(author.getId())) {

                throw new AccessDeniedException("User is not authorized to delete post with id: " + postId);
            }
        }

        PostDto deletedPostDto = PostDto.fromPost(postToDelete);

        postRepository.delete(postToDelete);

        return deletedPostDto;

    }
}