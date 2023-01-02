package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.repository.CandidateStore;

import java.util.Collection;

@ThreadSafe
@Service
public class SimpleCandidateService  implements CandidateService {
    private final CandidateStore candidateRepository;

    public SimpleCandidateService(CandidateStore candidateRepository) {
        this.candidateRepository = candidateRepository;
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