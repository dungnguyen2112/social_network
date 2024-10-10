package com.example.btljava.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePostRequestDTO {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    // Constructors
    public CreatePostRequestDTO() {
    }

    public CreatePostRequestDTO(String title, String content, Long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
