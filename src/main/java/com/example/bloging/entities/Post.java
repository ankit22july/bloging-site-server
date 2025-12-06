// package com.example.bloging.entities;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;
// import java.util.List;

// @Entity
// @Table(name = "posts")
// public class Post {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false)
//     private String title;

//     @Column(columnDefinition = "TEXT", nullable = false)
//     private String content;

//     @Column(name = "author_id")
//     private long authorId;
   
//     @ElementCollection
//     private List<String> tags;

//     @Column(name = "is_published", nullable = false)
//     private Boolean isPublished;

//     @Column(name = "created_at", updatable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at")
//     private LocalDateTime updatedAt;

//     @PrePersist
//     protected void onCreate() {
//         createdAt = LocalDateTime.now();
//         updatedAt = LocalDateTime.now();
//         isPublished = true;
//     }

//     @PreUpdate
//     protected void onUpdate() {
//         updatedAt = LocalDateTime.now();
//     }
// }


