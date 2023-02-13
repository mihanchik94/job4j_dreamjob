package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostControllerTest {
    private PostService postService;
    private CityService cityService;
    private PostController postController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        postService = mock(PostService.class);
        cityService = mock(CityService.class);
        postController = new PostController(postService, cityService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestPostListPageThenGetPageWithPosts() {
        Post post1 = new Post(1, "test1", "desc1", now(), true, 1, 2);
        Post post2 = new Post(2, "test2", "desc2", now(), false, 3, 4);
        List<Post> expectedPosts = List.of(post1, post2);
        when(postService.findAll()).thenReturn(expectedPosts);

        ConcurrentModel model = new ConcurrentModel();
        String view = postController.posts(model);
        Object actualPosts = model.getAttribute("posts");

        assertThat(view).isEqualTo("posts");
        assertThat(actualPosts).isEqualTo(expectedPosts);
    }

    @Test
    public void whenRequestPostCreationPageThenGetPageWithCities() {
        City city1 = new City(1, "Москва");
        City city2 = new City(2, "Санкт-Петербург");
        List<City> expectedCities = List.of(city1, city2);
        when(cityService.findAll()).thenReturn(expectedCities);

        ConcurrentModel model = new ConcurrentModel();
        String view = postController.addPost(model);
        Object actualPosts = model.getAttribute("cities");

        assertThat(view).isEqualTo("addPost");
        assertThat(actualPosts).isEqualTo(expectedCities);
    }

    @Test
    public void whenPostPostWithFileThenSameDataAndRedirectToPostsPage() throws Exception {
        Post post = new Post(1, "test1", "desc1", now(), true, 1, 2);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        ArgumentCaptor<Post> postArgumentCapture = ArgumentCaptor.forClass(Post.class);
        ArgumentCaptor<FileDto> fileDtoArgumentCapture = ArgumentCaptor.forClass(FileDto.class);
        when(postService.save(postArgumentCapture.capture(), fileDtoArgumentCapture.capture())).thenReturn(post);

        String view = postController.createPost(post, testFile);
        Post actualPost = postArgumentCapture.getValue();
        FileDto actualFileDto = fileDtoArgumentCapture.getValue();

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(actualPost).isEqualTo(post);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenRequestPostUpdatePageThenGetPageWithCities() {
        Post post = new Post(1, "test1", "desc1", now(), true, 1, 2);
        City city1 = new City(1, "Москва");
        City city2 = new City(2, "Санкт-Петербург");
        List<City> expectedCities = List.of(city1, city2);
        when(postService.findById(any(Integer.class))).thenReturn(Optional.of(post));
        when(cityService.findAll()).thenReturn(expectedCities);

        ConcurrentModel model = new ConcurrentModel();
        String view = postController.formUpdatePost(model, post.getId());
        Object actualCities = model.getAttribute("cities");

        assertThat(view).isEqualTo("updatePost");
        assertThat(actualCities).isEqualTo(expectedCities);
    }

    @Test
    public void whenRequestPostUpdateThenRedirectToPostsPage() throws IOException {
        Post post = new Post(1, "test1", "desc1", now(), true, 1, 2);
        Post updatedPost = new Post(post.getId(), "test2", "desc2", now(), false, 3, 4);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(postService.update(updatedPost, fileDto)).thenReturn(true);
        when(postService.findById(post.getId())).thenReturn(Optional.of(updatedPost));

        String view = postController.updatePost(updatedPost, testFile);

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(postService.findById(post.getId())).isEqualTo(Optional.of(updatedPost));
    }
}