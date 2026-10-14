package com.example.back_end.model.repository;

import com.example.back_end.model.domain.Titulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TituloRepository extends JpaRepository<Titulo, Long> {

    boolean existsByNomeIgnoreCaseAndAnoLancamento(String nome, Integer anoLancamento);

    boolean existsByNomeIgnoreCaseAndAnoLancamentoAndIdNot(
            String nome,
            Integer anoLancamento,
            Long id
    );
}
