package com.example.bloging.entities;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200) // Matching VARCHAR(200)
    private String title;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Defines the Many-to-One relationship to the User entity (the Author)
    // The name attribute specifies the foreign key column in the 'posts' table.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private User author; // Renamed to 'author' for better object-oriented naming

    @Type(value = ListArrayType.class, // ⬅️ Use the standard ListArrayType
            parameters = @Parameter( // ⬅️ Pass a parameter to specify the database array type
                    name = ListArrayType.SQL_ARRAY_TYPE, value = "text" // ⬅️ This is the key: specifies it's an array
                                                                        // of 'text'
            ))
    @Column(name = "tags", columnDefinition = "text[]")
    private List<String> tags;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // --- Lifecycle Callbacks ---

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        // SQL default is FALSE, so we ensure the object mirrors that unless set
        // otherwise.
        if (isPublished == null) {
            isPublished = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    // ID setter often omitted for auto-generated fields
    public void setId(Long id) {
        this.id = id;
    }

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) { // Updated to use the new field name
        this.author = author;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    // Setters for createdAt/updatedAt are generally omitted
}