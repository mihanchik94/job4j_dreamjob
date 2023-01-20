package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class CandidateStore implements CandidateRepository {
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(3);

    public CandidateStore() {
        candidates.put(1, new Candidate(1, "Semenov Igor", "Without experience", LocalDateTime.now(), 1, 0));
        candidates.put(2, new Candidate(2, "Gerasimova Ekaterina", "Experience: 3 years", LocalDateTime.now(), 2, 0));
        candidates.put(3, new Candidate(3, "Sidorova Elena", "Experience: 1 year", LocalDateTime.now(), 3, 0));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    @Override
    public Candidate save(Candidate candidate) {
        int id = counter.incrementAndGet();
        candidate.setId(id);
        candidate.setCreated(LocalDateTime.now());
        candidates.put(id, candidate);
        return candidate;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        Candidate candidate = candidates.get(id);
        return Optional.ofNullable(candidate);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.replace(candidate.getId(), candidates.get(candidate.getId()), candidate);
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }
}