package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;

public interface CandidateService {
    void save(Candidate candidate);
    void update(Candidate candidate);
    Collection<Candidate> findAll();
    Candidate findById(int id);
}