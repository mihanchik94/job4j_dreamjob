package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Post;

import java.util.Collection;
import java.util.Optional;

public interface PostService {
    Post save(Post post, FileDto image);
    boolean deleteById(int id);
    boolean update(Post post, FileDto image);
    Collection<Post> findAll();
    Optional<Post> findById(int id);
}