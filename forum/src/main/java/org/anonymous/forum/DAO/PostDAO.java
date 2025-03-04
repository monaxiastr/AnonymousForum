package org.anonymous.forum.DAO;

import org.anonymous.forum.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends JpaRepository<Post, Integer> {

    List<Post> findAllByAuthor(String author);

    List<Post> findByTitleLikeIgnoreCase(String title);
}
