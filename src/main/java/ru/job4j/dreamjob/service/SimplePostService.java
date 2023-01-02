package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.repository.PostStore;

import java.util.Collection;

@ThreadSafe
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