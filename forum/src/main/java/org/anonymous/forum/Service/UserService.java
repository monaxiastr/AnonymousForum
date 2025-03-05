package org.anonymous.forum.Service;

import org.anonymous.forum.DAO.PostDAO;
import org.anonymous.forum.DAO.UserDAO;
import org.anonymous.forum.Entity.Post;
import org.anonymous.forum.Entity.User;
import org.anonymous.forum.Enum.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PostDAO postDAO;

    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean addUser(String id, String password, Gender gender) {
        if (userDAO.findById(id).isPresent()) {
            return false;
        }
        // 生成盐值
        byte[] salt = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        User user = new User();
        user.setId(id);
        user.setAdmin(false);
        user.setGender(gender);
        user.setSalt(salt);
        user.setHashedPassword(hashPassword(password, salt));
        userDAO.save(user);
        return true;
    }

    public boolean updateId(String oldId, String newId) {
        User user = userDAO.findById(oldId).orElse(null);
        if (user != null) {
            user.setId(newId);
            userDAO.save(user);
            List<Post> posts = postDAO.findAllByAuthor(oldId);
            posts.forEach(post -> post.setAuthor(newId));
            postDAO.saveAll(posts);
            return true;
        } else {
            return false;
        }
    }

    public boolean updatePassword(String id, String oldPassword, String newPassword) {
        if (checkPassword(id, oldPassword)) {
            User user = getUser(id);
            byte[] salt = user.getSalt();
            String hashedPassword = hashPassword(newPassword, salt);
            user.setHashedPassword(hashedPassword);
            userDAO.save(user);
            return true;
        } else {
            return false;
        }
    }

    public void updateGender(String id, Gender gender) {
        User user = userDAO.findById(id).orElse(null);
        if (user != null) {
            user.setGender(gender);
            userDAO.save(user);
        }
    }

    public boolean checkPassword(String id, String password) {
        User user = getUser(id);
        if (user == null)
            return false;
        byte[] salt = user.getSalt();
        String hashedPassword = hashPassword(password, salt);
        return hashedPassword.equals(user.getHashedPassword());
    }

    public User getUser(String id) {
        return userDAO.findById(id).orElse(null);
    }

    public String getAvatarUrl(String id) {
        User user = getUser(id);
        return (user == null) ? null : user.getAvatarUrl();
    }

    public boolean setAvatarUrl(String id, String avatarUrl) {
        User user = userDAO.findById(id).orElse(null);
        if (user != null) {
            user.setAvatarUrl(avatarUrl);
            userDAO.save(user);
            return true;
        }
        return false;
    }
}
