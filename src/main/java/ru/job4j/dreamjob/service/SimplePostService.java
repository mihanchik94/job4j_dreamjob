package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.repository.PostRepository;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;

    public SimplePostService(PostRepository sql2oPostRepository, FileService fileService) {
        this.postRepository = sql2oPostRepository;
        this.fileService = fileService;
    }

    @Override
    public Post save(Post post, FileDto image) {
        saveNewFile(post, image);
        return postRepository.add(post);
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Post> post = findById(id);
        if (post.isEmpty()) {
            return false;
        }
        boolean isDeleted = postRepository.deleteById(id);
        fileService.deleteById(post.get().getFileId());
        return isDeleted;
    }

    private void saveNewFile(Post post, FileDto image) {
        File file = fileService.save(image);
        post.setFileId(file.getId());
    }

    @Override
    public boolean update(Post post, FileDto image) {
        if (image.getContent().length != 0) {
            int oldFieldId = post.getFileId();
            saveNewFile(post, image);
            fileService.deleteById(oldFieldId);
        }
        return postRepository.updatePost(post);
    }

    @Override
    public Collection<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post>findById(int id) {
        return postRepository.findById(id);
    }
}