package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.repository.CandidateRepository;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleCandidateService implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final FileService fileService;

    public SimpleCandidateService(CandidateRepository sql2oCandidateRepository, FileService fileService) {
        this.candidateRepository = sql2oCandidateRepository;
        this.fileService = fileService;
    }

    @Override
    public void save(Candidate candidate, FileDto image) {
        saveNewFile(candidate, image);
        candidateRepository.save(candidate);
    }

    @Override
    public void update(Candidate candidate, FileDto image) {
        if (image.getContent().length == 0) {
            candidateRepository.update(candidate);
        } else {
            int oldFileId = candidate.getFileId();
            saveNewFile(candidate, image);
            fileService.deleteById(oldFileId);
            candidateRepository.update(candidate);
        }
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return candidateRepository.findById(id);
    }

    private void saveNewFile(Candidate candidate, FileDto image) {
        File file = fileService.save(image);
        candidate.setFileId(file.getId());
    }
}