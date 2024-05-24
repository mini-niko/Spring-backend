package br.minikoapi.controller;

import br.minikoapi.dtos.UserDTO;
import br.minikoapi.entities.user.User;
import br.minikoapi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    public void shoudCreateNewUser() throws Exception {

        System.out.println("[Test] Testing shoudCreateNewUser()");

        User user = new User(new UserDTO("Example", "example@email.com", "12345678"));

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(user);

        String userJson = mapper.writeValueAsString(user);

        mvc.perform(
            post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", Matchers.is(user.getId())))
            .andExpect(jsonPath("$.name", Matchers.is(user.getName())));

        System.out.println("[Test] Test shoudCreateNewUser() passed");
    }

    @Test
    public void shoudReturnInvalidFields() throws Exception {

        System.out.println("[Test] Testing shoudReturnInvalidFields()");

        User userNameWrong = new User(new UserDTO("Ex", "example@email.com", "12345678"));
        User userEmailWrong = new User(new UserDTO("Example", "example", "12345678"));
        User userPasswordWrong = new User(new UserDTO("Example", "example@email.com", "123"));

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenThrow(new Exception("Invalid fields"));

        String userJsonNameWrong = mapper.writeValueAsString(userNameWrong);
        String userJsonEmailWrong = mapper.writeValueAsString(userEmailWrong);
        String userJsonPasswordWrong = mapper.writeValueAsString(userPasswordWrong);

        mvc.perform(
            post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonNameWrong)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status", Matchers.is("500")))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid fields")));

        mvc.perform(
            post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonEmailWrong)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status", Matchers.is("500")))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid fields")));

        mvc.perform(
            post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJsonPasswordWrong)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status", Matchers.is("500")))
            .andExpect(jsonPath("$.message", Matchers.is("Invalid fields")));

        System.out.println("[Test] Test shoudCreateNewUser() passed");

    }

    @Test
    public void shouldReturnDataAlreadySaved() throws Exception {

        System.out.println("[Test] Testing shoudReturnDataAlreadySaved()");

        User user = new User(new UserDTO("Example", "example@email.com", "12345678"));

        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenThrow(new DataIntegrityViolationException("Data already saved"));

        String userJson = mapper.writeValueAsString(user);

        mvc.perform(
            post("/api/users/create-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status", Matchers.is("400")))
            .andExpect(jsonPath("$.message", Matchers.is("Data already saved")));

        System.out.println("[Test] Test shoudReturnDataAlreadySaved() passed");

    }

    @Test
    public void shouldReturnFound() throws Exception {

        System.out.println("[Test] Testing shoudReturnFound()");

        String id = "id-example";
        User user = new User(new UserDTO("Example", "example@email.com", "12345678"));

        Mockito.when(userService.findUserById(Mockito.any())).thenReturn(user);

        String userJson = mapper.writeValueAsString(user);

        mvc.perform(
            get("/api/users/find-user")
                .accept(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(user.getId())))
            .andExpect(jsonPath("$.name", Matchers.is(user.getName())));

        System.out.println("[Test] Test shoudReturnFound() passed");
    }

    @Test
    public void shouldReturnNotFound() throws Exception {

        System.out.println("[Test] Testing shoudReturnNotFound()");

        String id = "id-example";
        User user = new User(new UserDTO("Example", "example@email.com", "12345678"));

        Mockito.when(userService.findUserById(Mockito.any())).thenThrow(new EntityNotFoundException());

        String userJson = mapper.writeValueAsString(user);

        mvc.perform(
            get("/api/users/find-user")
                .accept(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
            .andExpect(status().isNotFound());

        System.out.println("[Test] Test shoudReturnNotFound() passed");
    }
}
