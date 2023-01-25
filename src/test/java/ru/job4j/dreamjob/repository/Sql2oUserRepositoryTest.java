package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.configuration.DataSourceConfiguration;
import ru.job4j.dreamjob.model.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {
    static Sql2oUserRepository userRepository;

    @BeforeAll
    public static void initRepository() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oUserRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String userName = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DataSourceConfiguration configuration = new DataSourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, userName, password);
        Sql2o sql2o = configuration.databaseClient(dataSource);

        userRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    void clearUsers() {
        Collection<User> users = userRepository.findAll();
        for (User user : users) {
            userRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        User user = userRepository.save(new User(1, "email", "name", "password")).get();
        User findingUser = userRepository.findById(user.getId()).get();
        assertThat(findingUser).usingRecursiveComparison().isEqualTo(user);
    }


    @Test
    public void whenTheSameEmailThenFail() {
        User user = userRepository.save(new User(1, "email", "name", "password")).get();
        User findingUser = userRepository.findById(user.getId()).get();
        assertThat(findingUser).usingRecursiveComparison().isEqualTo(user);
        assertThatThrownBy(() ->
                userRepository.save(new User(1, "email", "name", "password")).get()).isInstanceOf(RuntimeException.class);
    }

}