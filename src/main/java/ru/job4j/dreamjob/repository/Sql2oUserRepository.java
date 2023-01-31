package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.User;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Repository
public class Sql2oUserRepository implements UserRepository {
    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      INSERT INTO users(email, name, password)
                      VALUES (:email, :name, :password)
                      """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("email", user.getEmail())
                    .addParameter("name", user.getName())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                    SELECT * from users where email = :email and password = :password
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("email", email)
                    .addParameter("password", password);
            User user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * from users where id = :id")
                    .addParameter("id", id);
            User user = query.executeAndFetchFirst(User.class);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Collection<User> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * from users");
            return query.executeAndFetch(User.class);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("DELETE from users where id = :id")
                    .addParameter("id", id);
            int affectedRows = query.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}