package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostRepository {
    Post add(Post post);
    boolean deleteById(int id);
    Collection<Post> findAll();
    Optional <Post> findById(int id);
    boolean updatePost(Post post);
}