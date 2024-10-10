package com.example.btljava.domain.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {

    @NotBlank(message = "Content cannot be empty")
    private String content;

    // Constructors
    public CommentRequestDTO() {
    }

}
