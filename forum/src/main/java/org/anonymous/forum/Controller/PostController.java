package org.anonymous.forum.Controller;

import org.anonymous.forum.Entity.Post;
import org.anonymous.forum.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/addPost")
    public ResponseEntity<String> addPost(@RequestBody Map<String, String> requestBody) {
        String author = requestBody.get("author");
        String title = requestBody.get("title");
        String content = requestBody.get("content");
        if (postService.createPost(author, title, content)) {
            return ResponseEntity.ok("发布成功！");
        }
        else {
            return ResponseEntity.badRequest().body("作者不存在！");
        }
    }

    @PostMapping("/getPost")
    public Post getPost(@RequestBody Map<String, String> requestBody) {
        int id = Integer.parseInt(requestBody.get("id"));
        return postService.getPostById(id);
    }

    @PostMapping("/userPosts")
    public List<Post> getUserPosts(@RequestBody Map<String, String> requestBody) {
        String author = requestBody.get("author");
        return postService.getPostsByAuthor(author);
    }

    @PostMapping("/searchPosts")
    public List<Post> searchPosts(@RequestBody Map<String, String> requestBody) {
        String title = requestBody.get("query");
        return postService.getPostsByTitleLike(title);
    }
}
