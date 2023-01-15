package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;

@ThreadSafe
@Repository
public class Sql2oPostRepository implements PostRepository {

    private final Sql2o sql2o;

    public Sql2oPostRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Post post) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    INSERT INTO posts(name, description, creation_date, visible, city_id, file_id)
                    VALUES (:name, :description, :created, :visible, :cityId, :fileId)
                    """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("name", post.getName())
                    .addParameter("description", post.getDescription())
                    .addParameter("created", post.getCreated())
                    .addParameter("visible", post.getVisible())
                    .addParameter("cityId", post.getCityId())
                    .addParameter("fileId", post.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            post.setId(generatedId);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("DELETE from posts where id = :id");
            query.addParameter("id", id);
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }

    @Override
    public Collection<Post> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * from posts");
            return query.setColumnMappings(Post.COLUMN_MAPPING).executeAndFetch(Post.class);
        }
    }

    @Override
    public Post findById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * from posts where id = :id");
            query.addParameter("id", id);
            return query.setColumnMappings(Post.COLUMN_MAPPING).executeAndFetchFirst(Post.class);
        }
    }

    @Override
    public void updatePost(Post post) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    UPDATE posts
                    SET name = :name, description = :description, creation_date = :created, 
                    visible = :visible, city_id = :cityId, file_id = :fileId
                    WHERE id = :id
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("name", post.getName())
                    .addParameter("description", post.getDescription())
                    .addParameter("created", post.getCreated())
                    .addParameter("visible", post.getVisible())
                    .addParameter("cityId", post.getCityId())
                    .addParameter("fileId", post.getFileId())
                    .addParameter("id", post.getId());
            query.executeUpdate();
        }
    }
}