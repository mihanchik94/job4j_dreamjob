package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(3);

    public PostStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Beginner vacancy", LocalDateTime.now(), true, 2));
        posts.put(2, new Post(2, "Middle Java Job", "Position for people with 2+ years of experience", LocalDateTime.now(), true, 1));
        posts.put(3, new Post(3, "Senior Java Job", "Position for people with 5+ years of experience", LocalDateTime.now(), true, 3));
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

    public Post findById(int id) {
        return posts.get(id);
    }

    public void updatePost(Post post) {
        posts.replace(post.getId(), posts.get(post.getId()), post);
    }
}