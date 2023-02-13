package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CandidateControllerTest {
    private CandidateService candidateService;
    private CityService cityService;
    private CandidateController candidateController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        candidateService = mock(CandidateService.class);
        cityService = mock(CityService.class);
        candidateController = new CandidateController(candidateService, cityService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestCandidateListPageThenGetPageWithCandidates() {
        Candidate candidate1 = new Candidate(1, "name1", "description1", LocalDateTime.now(), 1, 1);
        Candidate candidate2 = new Candidate(2, "name2", "description2", LocalDateTime.now(), 1, 2);
        List<Candidate> expectedCandidates = List.of(candidate1, candidate2);
        when(candidateService.findAll()).thenReturn(expectedCandidates);

        ConcurrentModel model = new ConcurrentModel();
        String view = candidateController.candidates(model);
        Object actualCandidates = model.getAttribute("candidates");

        assertThat(view).isEqualTo("candidates");
        assertThat(actualCandidates).isEqualTo(expectedCandidates);
    }

    @Test
    public void whenRequestCandidateCreationPageThenGetPageWithCities() {
        City city1 = new City(1, "Москва");
        City city2 = new City(2, "Санкт-Петербург");
        List<City> expectedCities = List.of(city1, city2);
        when(cityService.findAll()).thenReturn(expectedCities);

        ConcurrentModel model = new ConcurrentModel();
        String view = candidateController.addCandidate(model);
        Object actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("addCandidate");
        assertThat(actualCities).isEqualTo(expectedCities);
    }

    @Test
    public void whenPostCandidateWithFileThenSameDataAndRedirectToCandidatesPage() throws Exception {
        Candidate candidate = new Candidate(1, "name", "description", LocalDateTime.now(), 1, 1);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        ArgumentCaptor<Candidate> candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        ArgumentCaptor<FileDto> fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(candidate);

        String view = candidateController.createCandidate(candidate, testFile);
        Candidate actualCandidate = candidateArgumentCaptor.getValue();
        FileDto actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(actualFileDto).usingRecursiveComparison().isEqualTo(fileDto);
    }

    @Test
    public void whenRequestPostUpdatePageThenGetPageWithCities() {
        Candidate candidate = new Candidate(1, "name", "description", LocalDateTime.now(), 1, 1);
        City city1 = new City(1, "Москва");
        City city2 = new City(2, "Санкт-Петербург");
        List<City> expectedCities = List.of(city1, city2);
        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.of(candidate));
        when(cityService.findAll()).thenReturn(expectedCities);

        ConcurrentModel model = new ConcurrentModel();
        String view = candidateController.formUpdateCandidate(model, candidate.getId());
        Object actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("updateCandidate");
        assertThat(actualCities).isEqualTo(expectedCities);
    }

    @Test
    public void whenRequestPostUpdateThenRedirectToPostsPage() throws IOException {
        Candidate candidate = new Candidate(1, "name1", "description1", LocalDateTime.now(), 1, 1);
        Candidate updatedCandidate = new Candidate(2, "name2", "description2", LocalDateTime.now(), 1, 2);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(candidateService.update(updatedCandidate, fileDto)).thenReturn(true);
        when(candidateService.findById(candidate.getId())).thenReturn(Optional.of(updatedCandidate));

        String view = candidateController.updateCandidate(updatedCandidate, testFile);

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(candidateService.findById(candidate.getId())).isEqualTo(Optional.of(updatedCandidate));
    }
}