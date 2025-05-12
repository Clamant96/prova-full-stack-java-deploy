package br.com.helpconnect.provaFullStackJava.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.helpconnect.provaFullStackJava.model.UserLogin;
import br.com.helpconnect.provaFullStackJava.model.Usuario;
import br.com.helpconnect.provaFullStackJava.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Endpoint de Teste", description = "Verificação básica da API")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Endpoint de teste", description = "Retorna uma mensagem simples")
	public ResponseEntity<Page<Usuario>> getAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "nome") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "filtro", required = false) String filtro
            ){
		
		return ResponseEntity.ok(usuarioService.getAllPagable(
				page,
	            size,
	            sortBy,
	            direction,
	            filtro
            )
		);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER') or #id == principal.id")
	public ResponseEntity<Usuario> getById(@PathVariable("id") long id){
		
		return usuarioService.getById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/login")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<UserLogin> autenticarUsuario(@Valid @RequestBody Optional<UserLogin> usuarioLogin){
		
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/cadastrar")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Optional<Usuario>> cadastrarUsuario(@Valid @RequestBody Usuario usuario) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(usuarioService.cadastrarUsuario(usuario));
	}
	
	@PutMapping("/atualizar")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER') or #id == principal.id")
	public ResponseEntity<Optional<Usuario>> atualizarUsuario(@Valid @RequestBody Usuario usuario) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(usuarioService.atualizarUsuario(usuario));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteById(@PathVariable("id") long id){
		usuarioService.deleteById(id);
	}
	
}
