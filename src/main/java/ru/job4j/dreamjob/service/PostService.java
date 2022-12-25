package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;

import java.util.Collection;

public interface PostService {
    void save(Post post);
    void update(Post post);
    Collection<Post> findAll();
    Post findById(int id);
}