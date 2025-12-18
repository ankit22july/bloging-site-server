package com.example.bloging.controllers;

import com.example.bloging.dto.CreatePostDto;
import com.example.bloging.dto.PostDto;
import com.example.bloging.dto.UserDto;
import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;
import com.example.bloging.services.PostService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Post API", description = "Create, view, and delete blog posts")
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    // Constructor injection (recommended)
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a post", description = "Create a new blog post for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Post created successfully")
    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post createdPost = postService.createPost(req, user);
        return new ResponseEntity<>(PostDto.fromPost(createdPost), HttpStatus.CREATED);
    }

    @Operation(summary = "Get post by ID", description = "Fetch a blog post using post ID")
    @ApiResponse(responseCode = "200", description = "Post fetched successfully")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        return new ResponseEntity<>(PostDto.fromPost(post), HttpStatus.OK);
    }

    @Operation(summary = "Get my posts", description = "Get all posts created by the logged-in user")
    @GetMapping("/myposts")
    public ResponseEntity<List<PostDto>> getAllPostsByUserId() {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Post> userPosts = postService.findPostsByUserId(author);
        List<PostDto> userPostsDto = userPosts.stream()
                .map(PostDto::fromPost)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userPostsDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete post", description = "Delete a post created by the authenticated user")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) throws AccessDeniedException {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        postService.deletePostById(id, author);
        String successMessage = "Post ID " + id + " deleted successfully.";
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

}
