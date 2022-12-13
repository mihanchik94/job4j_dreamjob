package ru.job4j.dreamjob.store;

import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore INSTANCE = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    public CandidateStore() {
        candidates.put(1, new Candidate(1, "Semenov Igor", "Without experience", LocalDateTime.now()));
        candidates.put(2, new Candidate(2, "Gerasimova Ekaterina", "Experience: 3 years", LocalDateTime.now()));
        candidates.put(3, new Candidate(3, "Sidorova Elena", "Experience: 1 year", LocalDateTime.now()));
    }

    public static CandidateStore instOf() {
        return INSTANCE;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}