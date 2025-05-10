package br.com.helpconnect.provaFullStackJava.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.google.gson.Gson;

import br.com.helpconnect.provaFullStackJava.component.CepComponent;
import br.com.helpconnect.provaFullStackJava.model.Endereco;
import br.com.helpconnect.provaFullStackJava.model.EnderecoDTO;
import br.com.helpconnect.provaFullStackJava.repository.EnderecoRepository;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private CepComponent cepProperties;
	
	public Optional<Endereco> cadastrarEndereco(Endereco endereco) {		
		return Optional.of(enderecoRepository.save(endereco));
	
	}
	
	public Optional<Endereco> atualizarEndereco(Endereco endereco) {
		
		Optional<Endereco> buscaEndereco = enderecoRepository.findById(endereco.getId());
		
		if(buscaEndereco.isPresent()) {
			return Optional.of(enderecoRepository.save(endereco));
		}

		return Optional.empty();
	}
	
	public Page<Endereco> getAllPagable(
			int page,
            int size,
            String sortBy,
            String direction,
            String filtro
    ) {
		
		// Validação dos parâmetros de ordenação
        List<String> camposValidos = List.of("cep", "logradouro", "dataCadastro");
        if (!camposValidos.contains(sortBy)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo de ordenação inválido");
        }
        
        Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by(sortDirection, sortBy)
        );

        Page<Endereco> enderecos;
        if (filtro != null && !filtro.isEmpty()) {
        	enderecos = search(filtro, pageable);
        } else {
        	enderecos = getAll(pageable);
        }
        
        return enderecos;
	}
	
	public Page<Endereco> getAllEnderecosByUsuarioId(
			int page,
            int size,
            String sortBy,
            String direction,
			long id
    ) {
		Sort.Direction sortDirection;
        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by(sortDirection, sortBy)
        );
        
		return enderecoRepository.findAllEnderecosByUsuario_Id(id, pageable);
	}
	
	public Page<Endereco> getAll(Pageable pageable) {
        return enderecoRepository.findAll(pageable);
    }
    
    public Page<Endereco> search(String filtro, Pageable pageable) {
        return enderecoRepository.findByCepContainingOrLogradouroContaining(filtro, pageable);
    }
	
	public Optional<Endereco> getById(long id) {
		return enderecoRepository.findById(id);
	}
	
	public Optional<Endereco> getByCep(String cep) {

		try {
			
			// CONSUMO DE SERVIÇO
			String url = cepProperties.getUrlCep() + cep + "/json/";
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "application/json");

			HttpEntity<String> entity = new HttpEntity<String>(headers);
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			
			// SUCESSO
			if (responseEntity.getStatusCode().value() == 200) { 
				
				try {
					// DESERIALIZA
					EnderecoDTO resp = new Gson().fromJson(responseEntity.getBody().toString(), EnderecoDTO.class);
					
					return Optional.of(new Endereco(
							resp.getLogradouro(), 
							resp.getNumero(), 
							resp.getComplemento(), 
							resp.getBairro(), 
							resp.getLocalidade(), 
							resp.getEstado(), 
							resp.getCep()
						)
					);
				} catch (Exception e) {
					System.out.println("ERRO: "+ e.toString());
					return Optional.empty();
				}
			} else {
				// ITEM NAO LOCALIZADO
				return Optional.empty();
			}

		}
		catch (Exception e) {
			// ERRO NA BUSCA OU DESERIALIZA DO OBJ
			return null;
		}
	}
	
	public void deleteById(long id) {
		enderecoRepository.deleteById(id);
    }
	
}
