package com.example.back_end.model.repository;

import com.example.back_end.model.domain.ator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface repositoryator extends JpaRepository<ator, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<ator> findAllByOrderByIdAsc();

    default ator salvar(ator ator) {
        return save(ator);
    }

    default boolean existePorNome(String nome, Long idIgnorado) {
        return idIgnorado == null
                ? existsByNomeIgnoreCase(nome)
                : existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);
    }

    default boolean existePorNome(String nome) {
        return existsByNomeIgnoreCase(nome);
    }

    default Optional<ator> buscarPorId(Long id) {
        return findById(id);
    }

    default List<ator> listarTodos() {
        return findAllByOrderByIdAsc();
    }

    default boolean excluirPorId(Long id) {
        if (!existsById(id)) {
            return false;
        }
        deleteById(id);
        return true;
    }
}
