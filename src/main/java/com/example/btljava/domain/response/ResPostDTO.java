package com.example.btljava.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResPostDTO {
    private long id;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
    private String userName;
}
