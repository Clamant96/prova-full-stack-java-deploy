package br.com.helpconnect.provaFullStackJava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.helpconnect.provaFullStackJava.model.Endereco;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

	// Consulta JPQL com ordenação dinâmica
	@Query("SELECT e FROM Endereco e WHERE " +
		       "LOWER(e.cep) LIKE LOWER(concat('%', :filtro, '%')) OR " +
		       "LOWER(e.logradouro) LIKE LOWER(concat('%', :filtro, '%')) OR " +
		       "CAST(e.usuario AS string) LIKE LOWER(concat('%', :filtro, '%'))")
    Page<Endereco> findByCepContainingOrLogradouroContaining(
        @Param("filtro") String filtro,
        Pageable pageable
    );
	
	@Query("SELECT e FROM Endereco e WHERE e.usuario.id = :id")
	Page<Endereco> findAllEnderecosByUsuario_Id(
	     @Param("id") long id,
	     Pageable pageable
	);
    
}
