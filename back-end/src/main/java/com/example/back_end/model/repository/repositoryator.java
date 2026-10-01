package com.example.back_end.model.repository;

import com.example.back_end.model.domain.ator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class repositoryator {

    private final Map<Long, ator> atores = new ConcurrentHashMap<>();
    private final AtomicLong proximoId = new AtomicLong(1);

    public ator salvar(ator ator) {
        if (ator.getId() == null) {
            ator.setId(proximoId.getAndIncrement());
        }

        atores.put(ator.getId(), ator);
        return ator;
    }

    public boolean existePorNome(String nome) {
        return atores.values().stream()
                .anyMatch(ator -> ator.getNome().equalsIgnoreCase(nome));
    }

    public Optional<ator> buscarPorId(Long id) {
        return Optional.ofNullable(atores.get(id));
    }

    public List<ator> listarTodos() {
        return new ArrayList<>(atores.values());
    }
}
