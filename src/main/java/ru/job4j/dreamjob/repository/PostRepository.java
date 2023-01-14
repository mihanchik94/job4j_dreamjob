package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;

public interface PostRepository {
    void add(Post post);
    boolean deleteById(int id);
    Collection<Post> findAll();
    Post findById(int id);
    void updatePost(Post post);
}