package com.example.back_end.model.repository;

import com.example.back_end.model.domain.classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface repositoryclasse extends JpaRepository<classe, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<classe> findAllByOrderByIdAsc();

    default classe salvar(classe classe) {
        return save(classe);
    }

    default boolean existePorNome(String nome, Long idIgnorado) {
        return idIgnorado == null
                ? existsByNomeIgnoreCase(nome)
                : existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);
    }

    default Optional<classe> buscarPorId(Long id) {
        return findById(id);
    }

    default List<classe> listarTodos() {
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
