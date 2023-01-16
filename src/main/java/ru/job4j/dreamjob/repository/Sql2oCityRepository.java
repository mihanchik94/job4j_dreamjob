package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.dreamjob.model.City;

import java.util.Collection;

@ThreadSafe
@Repository
public class Sql2oCityRepository implements CityRepository {

    private final Sql2o sql2o;

    public Sql2oCityRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<City> findAll() {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * from cities");
            return query.executeAndFetch(City.class);
        }
    }
}