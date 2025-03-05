package org.anonymous.forum.Service;

import org.anonymous.forum.DAO.PostDAO;
import org.anonymous.forum.DAO.UserDAO;
import org.anonymous.forum.Entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostDAO postDAO;

    @Autowired
    private UserDAO userDAO;

    public boolean createPost(String author, String title, String content) {
        if (userDAO.findById(author).isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            Post post = new Post();
            post.setAuthor(author);
            post.setTitle(title);
            post.setContent(content);
            post.setTime(now);
            postDAO.save(post);
            return true;
        }
        return false;
    }

    public List<Post> getAllPosts() {
        return postDAO.findAll();
    }

    public List<Post> getPostsByAuthor(String author) {
        return postDAO.findAllByAuthor(author);
    }

    public Post getPostById(int id) {
        return postDAO.findById(id).orElse(null);
    }

    public List<Post> getPostsByTitleContaining(String title) {
        return postDAO.findByTitleContaining(title);
    }
}
