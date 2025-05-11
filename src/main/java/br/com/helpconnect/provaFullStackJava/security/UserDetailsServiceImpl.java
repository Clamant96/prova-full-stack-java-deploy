package br.com.helpconnect.provaFullStackJava.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.helpconnect.provaFullStackJava.model.Usuario;
import br.com.helpconnect.provaFullStackJava.repository.UsuarioRepository;
import jakarta.transaction.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
    @Transactional // Garante transação ativa
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(username);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }

        return new UserDetailsImpl(usuario.get());
    }
}