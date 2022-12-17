package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PostStore {
    private static final PostStore INSTANCE = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Beginner vacancy", LocalDateTime.now()));
        posts.put(2, new Post(2, "Middle Java Job", "Position for people with 2+ years of experience", LocalDateTime.now()));
        posts.put(3, new Post(3, "Senior Java Job", "Position for people with 5+ years of experience", LocalDateTime.now()));
    }

    public static PostStore instOf() {
        return INSTANCE;
    }

    public void add(Post post) {
        int id = counter.incrementAndGet();
        post.setId(id);
        post.setCreated(LocalDateTime.now());
        posts.put(id, post);
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}