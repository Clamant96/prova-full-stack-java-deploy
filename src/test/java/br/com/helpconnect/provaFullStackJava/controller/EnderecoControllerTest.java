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

import br.com.helpconnect.provaFullStackJava.model.Endereco;
import br.com.helpconnect.provaFullStackJava.service.EnderecoService;

@ExtendWith(MockitoExtension.class)
public class EnderecoControllerTest {

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    private Endereco endereco;

    @BeforeEach
    void setup() {
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setCep("12345678");
    }

    @Test
    void getAll_ShouldReturnPageOfEnderecos() {
        Page<Endereco> mockPage = new PageImpl<>(Collections.singletonList(endereco));
        when(enderecoService.getAllPagable(anyInt(), anyInt(), anyString(), anyString(), anyString()))
            .thenReturn(mockPage);

        ResponseEntity<Page<Endereco>> response = enderecoController.getAll(0, 10, "cep", "ASC", "");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getAllByUsuarioId_ValidId_ReturnPage() {
        Page<Endereco> mockPage = new PageImpl<>(Collections.singletonList(endereco));
        when(enderecoService.getAllEnderecosByUsuarioId(anyInt(), anyInt(), anyString(), anyString(), anyLong()))
            .thenReturn(mockPage);

        ResponseEntity<Page<Endereco>> response = enderecoController.getAllByUsuarioId(0, 10, "cep", "ASC", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getById_WhenEnderecoExists_ReturnEndereco() {
        when(enderecoService.getById(1L)).thenReturn(Optional.of(endereco));

        ResponseEntity<Endereco> response = enderecoController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(endereco, response.getBody());
    }

    @Test
    void getById_WhenEnderecoNotExists_ReturnNotFound() {
        when(enderecoService.getById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Endereco> response = enderecoController.getById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getByCep_WhenEnderecoExists_ReturnEndereco() {
        when(enderecoService.getByCep("12345678")).thenReturn(Optional.of(endereco));

        ResponseEntity<Endereco> response = enderecoController.getByCep("12345678");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(endereco, response.getBody());
    }

    @Test
    void getByCep_WhenEnderecoNotExists_ReturnNotFound() {
        when(enderecoService.getByCep("00000000")).thenReturn(Optional.empty());

        ResponseEntity<Endereco> response = enderecoController.getByCep("00000000");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void cadastrarEndereco_ValidEndereco_ReturnCreated() {
        when(enderecoService.cadastrarEndereco(any())).thenReturn(Optional.of(endereco));

        ResponseEntity<Optional<Endereco>> response = enderecoController.cadastrarEndereco(endereco);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(Optional.of(endereco), response.getBody());
    }

    @Test
    void atualizarEndereco_ValidEndereco_ReturnOk() {
        when(enderecoService.atualizarEndereco(any())).thenReturn(Optional.of(endereco));

        ResponseEntity<Optional<Endereco>> response = enderecoController.atualizarEndereco(endereco);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.of(endereco), response.getBody());
    }

    @Test
    void deleteById_ShouldCallServiceDelete() {
        enderecoController.deleteById(1L);
        verify(enderecoService).deleteById(1L);
    }
}
