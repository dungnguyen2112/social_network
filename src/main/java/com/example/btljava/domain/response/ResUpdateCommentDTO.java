package com.example.btljava.domain.response;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCommentDTO {
    private Long id;
    private Long postId;
    private String content;
    private Instant updatedAt;
}
