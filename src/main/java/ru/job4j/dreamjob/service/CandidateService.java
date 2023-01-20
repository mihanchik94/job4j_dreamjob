package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.Optional;

public interface CandidateService {
    void save(Candidate candidate, FileDto image);
    void update(Candidate candidate, FileDto image);
    Collection<Candidate> findAll();
    Optional<Candidate> findById(int id);
    boolean deleteById(int id);
}