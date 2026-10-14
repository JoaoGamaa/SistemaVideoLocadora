package com.example.back_end.model.repository;

import com.example.back_end.model.domain.diretor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface repositorydiretor extends JpaRepository<diretor, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<diretor> findAllByOrderByIdAsc();

    default diretor salvar(diretor diretor) {
        return save(diretor);
    }

    default boolean existePorNome(String nome, Long idIgnorado) {
        return idIgnorado == null
                ? existsByNomeIgnoreCase(nome)
                : existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);
    }

    default Optional<diretor> buscarPorId(Long id) {
        return findById(id);
    }

    default List<diretor> listarTodos() {
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
