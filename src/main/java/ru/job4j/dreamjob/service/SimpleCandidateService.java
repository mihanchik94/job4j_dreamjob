package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.File;
import ru.job4j.dreamjob.repository.CandidateStore;

import java.util.Collection;

@ThreadSafe
@Service
public class SimpleCandidateService  implements CandidateService {
    private final CandidateStore candidateRepository;
    private final FileService fileService;

    public SimpleCandidateService(CandidateStore candidateRepository, FileService fileService) {
        this.candidateRepository = candidateRepository;
        this.fileService = fileService;
    }

    @Override
    public void save(Candidate candidate, FileDto image) {
        saveNewFile(candidate, image);
        candidateRepository.add(candidate);
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
    public Candidate findById(int id) {
        return candidateRepository.findById(id);
    }

    private void saveNewFile(Candidate candidate, FileDto image) {
        File file = fileService.save(image);
        candidate.setFileId(file.getId());
    }
}