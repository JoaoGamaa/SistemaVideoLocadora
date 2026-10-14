package com.example.back_end.model.repository;

import com.example.back_end.model.domain.Ator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * O JpaRepository fornece as operações básicas de CRUD, como save, findAll,
 * findById, existsById, delete e deleteById.
 */
@Repository
public interface AtorRepository extends JpaRepository<Ator, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
