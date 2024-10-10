package com.example.btljava.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreatePostDTO {
    private long id;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String userName; // Assuming the post is tied to a user
}
