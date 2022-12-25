package ru.job4j.dreamjob.service;

import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;

@Service
public class SimplePostService implements PostService {
    private final PostStore postRepository;

    public SimplePostService(PostStore postRepository) {
        this.postRepository = postRepository;
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