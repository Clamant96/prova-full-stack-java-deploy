package br.com.helpconnect.provaFullStackJava.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.helpconnect.provaFullStackJava.model.UserLogin;
import br.com.helpconnect.provaFullStackJava.model.Usuario;
import br.com.helpconnect.provaFullStackJava.repository.UsuarioRepository;
import br.com.helpconnect.provaFullStackJava.security.JwtService;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;

    public Optional<UserLogin> autenticarUsuario(Optional<UserLogin> userLogin) {
        var credenciais = new UsernamePasswordAuthenticationToken(
            userLogin.get().getEmail(), 
            userLogin.get().getSenha()
        );
        
        Authentication authentication = authenticationManager.authenticate(credenciais);
        
        if (authentication.isAuthenticated()) {
            // Obtém o UserDetails do contexto de segurança
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Gera o token com as roles corretas
            String token = "Bearer " + jwtService.generateToken(userDetails);
            
            // Busca dados adicionais do usuário
            Optional<Usuario> usuario = usuarioRepository.findByEmail(userLogin.get().getEmail());
            
            if (usuario.isPresent()) {
                userLogin.get().setId(usuario.get().getId());
                userLogin.get().setNome(usuario.get().getNome());
                userLogin.get().setEmail(usuario.get().getEmail());
                userLogin.get().setToken(token);
                userLogin.get().setSenha(null);
                
                // Não retornar a senha por segurança
                userLogin.get().setRole(usuario.get().getRole().name());
                
                return userLogin;
            }
        }
        
        return Optional.empty();
    }

    public Optional<Usuario> cadastrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return Optional.empty();
        }
        
        usuario.setSenha(criptografarSenha(usuario.getSenha()));
        return Optional.of(usuarioRepository.save(usuario));
    }

    public Optional<Usuario> atualizarUsuario(Usuario usuario) {
        if (usuarioRepository.findById(usuario.getId()).isPresent()) {
            Optional<Usuario> buscaUsuario = usuarioRepository.findByEmail(usuario.getEmail());

            if (buscaUsuario.isPresent() && buscaUsuario.get().getId() != usuario.getId()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!");
            }

            usuario.setSenha(criptografarSenha(usuario.getSenha()));
            return Optional.of(usuarioRepository.save(usuario));
        }
        
        return Optional.empty();
    }
    
    public Page<Usuario> getAllPagable(
    		int page,
            int size,
            String sortBy,
            String direction,
            String filtro
    ) {
    	
    	// Validação dos parâmetros de ordenação
        List<String> camposValidos = List.of("nome", "email", "dataCadastro");
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

        Page<Usuario> usuarios;
        if (filtro != null && !filtro.isEmpty()) {
            usuarios = search(filtro, pageable);
        } else {
            usuarios = getAll(pageable);
        }
        
        return usuarios;
    }

    public Page<Usuario> getAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }
    
    public Page<Usuario> search(String filtro, Pageable pageable) {
        return usuarioRepository.findByNomeContainingOrEmailContaining(filtro, pageable);
    }

    public Optional<Usuario> getById(long id) {
        return usuarioRepository.findById(id);
    }
    
    public void deleteById(long id) {
        usuarioRepository.deleteById(id);
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }
}