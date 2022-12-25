package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;

public class SimplePostService implements PostService {
    private static final SimplePostService INSTANCE = new SimplePostService();
    private final PostStore postRepository = PostStore.instOf();


    public static SimplePostService getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Post post) {
        postRepository.add(post);
    }

    @Override
    public void update(Post post) {
        postRepository.updatePost(post);
    }

    @Override
    public Collection<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id);
    }
}