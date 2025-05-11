package br.com.helpconnect.provaFullStackJava.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.helpconnect.provaFullStackJava.model.UserLogin;
import br.com.helpconnect.provaFullStackJava.model.Usuario;
import br.com.helpconnect.provaFullStackJava.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;
    private UserLogin userLogin;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);

        userLogin = new UserLogin();
        userLogin.setEmail("test@example.com");
        userLogin.setSenha("senha");
    }

    @Test
    void getAll_ShouldReturnPageOfUsuarios() {
        Page<Usuario> mockPage = new PageImpl<>(Collections.singletonList(usuario));
        when(usuarioService.getAllPagable(anyInt(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockPage);

        ResponseEntity<Page<Usuario>> response = usuarioController.getAll(0, 10, "nome", "ASC", "");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getById_WhenUsuarioExists_ReturnUsuario() {
        when(usuarioService.getById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = usuarioController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
    }

    @Test
    void getById_WhenUsuarioNotExists_ReturnNotFound() {
        when(usuarioService.getById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = usuarioController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void autenticarUsuario_ValidCredentials_ReturnOk() {
        when(usuarioService.autenticarUsuario(any())).thenReturn(Optional.of(userLogin));

        ResponseEntity<UserLogin> response = usuarioController.autenticarUsuario(Optional.of(userLogin));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userLogin, response.getBody());
    }

    @Test
    void autenticarUsuario_InvalidCredentials_ReturnUnauthorized() {
        when(usuarioService.autenticarUsuario(any())).thenReturn(Optional.empty());

        ResponseEntity<UserLogin> response = usuarioController.autenticarUsuario(Optional.of(userLogin));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void cadastrarUsuario_ValidUsuario_ReturnCreated() {
        when(usuarioService.cadastrarUsuario(any())).thenReturn(Optional.of(usuario));

        ResponseEntity<Optional<Usuario>> response = usuarioController.cadastrarUsuario(usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Optional.of(usuario), response.getBody());
    }

    @Test
    void atualizarUsuario_ValidUsuario_ReturnOk() {
        when(usuarioService.atualizarUsuario(any())).thenReturn(Optional.of(usuario));

        ResponseEntity<Optional<Usuario>> response = usuarioController.atualizarUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(usuario), response.getBody());
    }

    @Test
    void deleteById_ShouldCallServiceDelete() {
        usuarioController.deleteById(1L);
        verify(usuarioService).deleteById(1L);
    }
}
