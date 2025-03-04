package org.anonymous.forum.DAO;

import org.anonymous.forum.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    Optional<User> findById(String id);
}
