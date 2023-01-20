package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.File;

import ru.job4j.dreamjob.configuration.DataSourceConfiguration;
import ru.job4j.dreamjob.model.Post;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import static java.util.Collections.emptyList;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;

class Sql2oPostRepositoryTest {
    static Sql2oPostRepository sql2oPostRepository;
    static Sql2oFileRepository sql2oFileRepository;
    static File file;

    @BeforeAll
    public static void initRepository() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oPostRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DataSourceConfiguration configuration = new DataSourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(dataSource);

        sql2oPostRepository = new Sql2oPostRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    void clearPosts() {
        Collection<Post> posts = sql2oPostRepository.findAll();
        for (Post post : posts) {
            sql2oPostRepository.deleteById(post.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Post post = sql2oPostRepository.add(new Post(0, "name", "description", created, true, 1, file.getId()));
        Post findingPost = sql2oPostRepository.findById(post.getId()).get();
        assertThat(findingPost).usingRecursiveComparison().isEqualTo(post);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Post post1 = sql2oPostRepository.add(new Post(0, "name1", "description1", created, true, 1, file.getId()));
        Post post2 = sql2oPostRepository.add(new Post(1, "name2", "description2", created, true, 1, file.getId()));
        Post post3 = sql2oPostRepository.add(new Post(2, "name3", "description3", created, true, 1, file.getId()));
        Collection<Post> result = sql2oPostRepository.findAll();
        assertThat(result).isEqualTo(List.of(post1, post2, post3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oPostRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oPostRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Post post = sql2oPostRepository.add(new Post(0, "name1", "description1", created, true, 1, file.getId()));
        boolean isDeleted = sql2oPostRepository.deleteById(post.getId());
        Optional<Post> savedVacancy = sql2oPostRepository.findById(post.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedVacancy).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oPostRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Post post = sql2oPostRepository.add(new Post(0, "name1", "description1", created, true, 1, file.getId()));
        Post updatedPost = new Post(
                post.getId(), "new title", "new description", created.plusDays(1),
                !post.getVisible(), 1, file.getId()
        );
        boolean isUpdated = sql2oPostRepository.updatePost(updatedPost);
        Post savedPost = sql2oPostRepository.findById(updatedPost.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedPost).usingRecursiveComparison().isEqualTo(updatedPost);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Post post = new Post(0, "name1", "description1", created, true, 1, file.getId());
        boolean isUpdated = sql2oPostRepository.updatePost(post);
        assertThat(isUpdated).isFalse();
    }
}