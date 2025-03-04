package org.anonymous.forum.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    private int id;

    private String author;

    private String title;

    private LocalDateTime time;

    private String content;
}
