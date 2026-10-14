package com.example.back_end.model.service;

import com.example.back_end.model.domain.ator;
import com.example.back_end.model.repository.repositoryator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class srvcadastrarator {

    private final repositoryator repositorioAtor;

    public srvcadastrarator(repositoryator repositorioAtor) {
        this.repositorioAtor = repositorioAtor;
    }

    public ator cadastrar(ator novoAtor) {
        String nome = validarNome(novoAtor);
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

    public List<ator> listarTodos() {
        return repositorioAtor.listarTodos();
    }

    public ator buscarPorId(Long id) {
        return repositorioAtor.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Ator não encontrado"));
    }

    public ator atualizar(Long id, ator dadosAtor) {
        ator atorExistente = buscarPorId(id);
        String nome = validarNome(dadosAtor);

        if (repositorioAtor.existePorNome(nome, id)) {
            throw new IllegalArgumentException("Já existe um ator cadastrado com esse nome");
        }

        atorExistente.setNome(nome);
        return repositorioAtor.salvar(atorExistente);
    }

    public void excluir(Long id) {
        if (!repositorioAtor.excluirPorId(id)) {
            throw new NoSuchElementException("Ator não encontrado");
        }
    }

    private String validarNome(ator dadosAtor) {
        if (dadosAtor == null) {
            throw new IllegalArgumentException("O ator deve ser informado");
        }

        String nome = dadosAtor.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ator deve ser informado");
        }
        return nome.trim();
    }
}
