package br.com.helpconnect.provaFullStackJava.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.provaFullStackJava.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
	
	// Consulta JPQL com ordenação dinâmica
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nome) LIKE LOWER(concat('%', :filtro, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(concat('%', :filtro, '%'))")
    Page<Usuario> findByNomeContainingOrEmailContaining(
        @Param("filtro") String filtro,
        Pageable pageable
    );
	
}
