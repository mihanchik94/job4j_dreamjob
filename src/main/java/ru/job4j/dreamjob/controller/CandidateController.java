package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@ThreadSafe
@Controller
public class CandidateController {
    private final CandidateService service;
    private final CityService cityService;

    public CandidateController(CandidateService service, CityService cityService) {
        this.service = service;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("candidates", service.findAll());
        model.addAttribute("cities", cityService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("candidate", new Candidate(0, "Заполните имя", "Заполните описание", LocalDateTime.now(), 1, 0));
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file) {
        try {
            service.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("candidate", service.findById(id).get());
        return "updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file) {
        try {
            service.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/candidates";
    }
}