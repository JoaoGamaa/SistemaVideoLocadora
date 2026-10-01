package com.example.back_end.model.service;

import com.example.back_end.model.domain.ator;
import com.example.back_end.model.repository.repositoryator;
import org.springframework.stereotype.Service;

@Service
public class srvcadastrarator {

    private final repositoryator repositorioAtor;

    public srvcadastrarator(repositoryator repositorioAtor) {
        this.repositorioAtor = repositorioAtor;
    }

    public ator cadastrar(ator novoAtor) {
        if (novoAtor == null) {
            throw new IllegalArgumentException("O ator deve ser informado");
        }

        String nome = novoAtor.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ator deve ser informado");
        }

        nome = nome.trim();
        if (repositorioAtor.existePorNome(nome)) {
            throw new IllegalArgumentException("Já existe um ator cadastrado com esse nome");
        }

        novoAtor.setId(null);
        novoAtor.setNome(nome);
        return repositorioAtor.salvar(novoAtor);
    }

    public ator cadastrarAtor(ator novoAtor) {
        return cadastrar(novoAtor);
    }
}
