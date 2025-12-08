package com.example.bloging.repositories;


import com.example.bloging.entities.Post;
import com.example.bloging.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
   List<Post> findByAuthor(User author);
}
