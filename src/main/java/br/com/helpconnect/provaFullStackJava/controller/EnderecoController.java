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

import br.com.helpconnect.provaFullStackJava.model.Endereco;
import br.com.helpconnect.provaFullStackJava.service.EnderecoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/endereco")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<Endereco>> getAll(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "cep") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "filtro", required = false) String filtro
        ){
		
		return ResponseEntity.ok(enderecoService.getAllPagable(
				page,
	            size,
	            sortBy,
	            direction,
	            filtro
            )
		);
	}
	
	@GetMapping("/enderecos/usuario/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Page<Endereco>> getAllByUsuarioId(
			@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "cep") String sortBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @PathVariable("id") long id
	){
		
		return ResponseEntity.ok(enderecoService.getAllEnderecosByUsuarioId(
				page,
	            size,
	            sortBy,
	            direction,
	            id
            )
		);
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Endereco> getById(@PathVariable("id") long id){
		
		return enderecoService.getById(id)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/cep/{cep}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Endereco> getByCep(@PathVariable("cep") String cep){
		
		return enderecoService.getByCep(cep)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping("/cadastrar")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Optional<Endereco>> cadastrarEndereco(@Valid @RequestBody Endereco endereco) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(enderecoService.cadastrarEndereco(endereco));
	}
	
	@PutMapping("/atualizar")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Optional<Endereco>> atualizarEndereco(@Valid @RequestBody Endereco endereco) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(enderecoService.atualizarEndereco(endereco));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteById(@PathVariable("id") long id){
		System.out.println("DELETAR ITEM: "+ id);
		enderecoService.deleteById(id);
	}
	
}
