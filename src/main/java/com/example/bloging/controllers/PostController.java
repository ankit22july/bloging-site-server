package com.example.bloging.controllers;

import com.example.bloging.dto.CreatePostDto;
import com.example.bloging.dto.PostDto;
import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;
import com.example.bloging.services.PostService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    // Constructor injection (recommended)
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post createdPost = postService.createPost(req, user);
        return new ResponseEntity<>(PostDto.fromPost(createdPost), HttpStatus.CREATED);
    }
}
