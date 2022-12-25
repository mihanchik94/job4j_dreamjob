package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;

public class SimpleCandidateService  implements CandidateService {
    private static final SimpleCandidateService INSTANCE = new SimpleCandidateService();
    private final CandidateStore candidateRepository = CandidateStore.instOf();

    public static SimpleCandidateService getInstance() {
        return INSTANCE;
    }

    @Override
    public void save(Candidate candidate) {
        candidateRepository.add(candidate);
    }

    @Override
    public void update(Candidate candidate) {
        candidateRepository.update(candidate);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    @Override
    public Candidate findById(int id) {
        return candidateRepository.findById(id);
    }
}