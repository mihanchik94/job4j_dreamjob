package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.configuration.DataSourceConfiguration;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.*;

class Sql2oCandidateRepositoryTest {
    static Sql2oCandidateRepository sql2oCandidateRepository;
    static Sql2oFileRepository sql2oFileRepository;
    static File file;

    @BeforeAll
    public static void initRepository() throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oCandidateRepository.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");

        DataSourceConfiguration configuration = new DataSourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(dataSource);

        sql2oCandidateRepository = new Sql2oCandidateRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);

        file = new File("test", "test2");
        sql2oFileRepository.save(file);
    }

    @AfterAll
    static void deleteFile() {
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    void clearPosts() {
        Collection<Candidate> candidates = sql2oCandidateRepository.findAll();
        for (Candidate candidate : candidates) {
            sql2oCandidateRepository.deleteById(candidate.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Candidate candidate = sql2oCandidateRepository.save(new Candidate(1, "name1", "description1", created, 1, file.getId()));
        Candidate findingCandidate = sql2oCandidateRepository.findById(candidate.getId()).get();
        assertThat(findingCandidate).usingRecursiveComparison().isEqualTo(candidate);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Candidate candidate1 = sql2oCandidateRepository.save(new Candidate(1, "name1", "description1", created, 1, file.getId()));
        Candidate candidate2 = sql2oCandidateRepository.save(new Candidate(2, "name2", "description2", created, 1, file.getId()));
        Candidate candidate3 = sql2oCandidateRepository.save(new Candidate(3, "name3", "description3", created, 1, file.getId()));
        Collection<Candidate> result = sql2oCandidateRepository.findAll();
        assertThat(result).isEqualTo(List.of(candidate1, candidate2, candidate3));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oCandidateRepository.findAll()).isEqualTo(emptyList());
        assertThat(sql2oCandidateRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Candidate candidate = sql2oCandidateRepository.save(new Candidate(1, "name1", "description1", created, 1, file.getId()));
        boolean isDeleted = sql2oCandidateRepository.deleteById(candidate.getId());
        assertThat(isDeleted).isTrue();
        assertThat(sql2oCandidateRepository.findById(candidate.getId())).isEqualTo(empty());
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(sql2oCandidateRepository.deleteById(0)).isFalse();
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Candidate candidate = sql2oCandidateRepository.save(new Candidate(1, "name1", "description1", created, 1, file.getId()));
        Candidate updatedCandidate = new Candidate(candidate.getId(), "new name", "new description", created.plusDays(1), 1, file.getId());
        boolean isUpdated = sql2oCandidateRepository.update(updatedCandidate);
        Candidate savedCandidate = sql2oCandidateRepository.findById(updatedCandidate.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(savedCandidate).usingRecursiveComparison().isEqualTo(updatedCandidate);

    }

    @Test
    public void whenUpdateUnExistingCandidateThenReturnFalse() {
        LocalDateTime created = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Candidate candidate = new Candidate(1, "name1", "description1", created, 1, file.getId());
        boolean isUpdated = sql2oCandidateRepository.update(candidate);
        assertThat(isUpdated).isFalse();
    }
}