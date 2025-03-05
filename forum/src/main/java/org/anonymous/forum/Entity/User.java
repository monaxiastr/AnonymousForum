package org.anonymous.forum.Entity;


import jakarta.persistence.*;
import lombok.Data;
import org.anonymous.forum.Enum.Gender;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;

    private boolean isAdmin = false;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private byte[] salt;

    private String hashedPassword;

    private String avatarUrl;
}
