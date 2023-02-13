package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private UserService userService;
    private UserController userController;
    private HttpServletRequest request;
    private HttpSession session;

    @BeforeEach
    public void initServices() {
        userService = mock(UserService.class);
        request = mock(HttpServletRequest.class);
        userController = new UserController(userService);
        session = mock(HttpSession.class);
    }

    @Test
    public void whenPostLoginUserAndSuccessThenGetPostsPage() {
        User user = new User(1, "test@ex.ru", "test", "111");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);


        ConcurrentModel model = new ConcurrentModel();
        String view = userController.loginUser(user, model, request);
        Object actualUser = session.getAttribute("user");

        assertThat(view).isEqualTo("redirect:/posts");
        assertThat(actualUser).isEqualTo(user);
    }

    @Test
    public void whenPostLoginUserAndNotSuccessThenGetLoginPage() {
        User user = new User(1, "test@ex.ru", "test", "111");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);


        ConcurrentModel model = new ConcurrentModel();
        String view = userController.loginUser(user, model, request);

        assertThat(view).isEqualTo("login");
    }

    @Test
    public void whenPostUserRegistrationPageAndSuccessThenGetPostsPage() {
        User user = new User(1, "test@ex.ru", "test", "111");
        when(userService.save(user)).thenReturn(Optional.of(user));

        ConcurrentModel model = new ConcurrentModel();
        String view = userController.register(user, model);

        assertThat(view).isEqualTo("redirect:/posts");
    }

    @Test
    public void whenPostUserRegistrationPageAndNotSuccessThenGetLoginPage() {
        User user = new User(1, "test@ex.ru", "test", "111");
        when(userService.save(user)).thenReturn(Optional.empty());

        ConcurrentModel model = new ConcurrentModel();
        String view = userController.register(user, model);

        assertThat(view).isEqualTo("errors/404");
    }
}