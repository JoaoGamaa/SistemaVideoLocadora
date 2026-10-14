package com.example.back_end.model.service;

import com.example.back_end.model.domain.diretor;
import com.example.back_end.model.repository.repositorydiretor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class srvcadastrardiretor {

    private final repositorydiretor repositorioDiretor;

    public srvcadastrardiretor(repositorydiretor repositorioDiretor) {
        this.repositorioDiretor = repositorioDiretor;
    }

    public diretor cadastrar(diretor novoDiretor) {
        String nome = validarNome(novoDiretor);
        if (repositorioDiretor.existePorNome(nome, null)) {
            throw new IllegalArgumentException("Já existe um diretor cadastrado com esse nome");
        }
        novoDiretor.setId(null);
        novoDiretor.setNome(nome);
        return repositorioDiretor.salvar(novoDiretor);
    }

    public List<diretor> listarTodos() {
        return repositorioDiretor.listarTodos();
    }

    public diretor buscarPorId(Long id) {
        return repositorioDiretor.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Diretor não encontrado"));
    }

    public diretor atualizar(Long id, diretor dadosDiretor) {
        diretor diretorExistente = buscarPorId(id);
        String nome = validarNome(dadosDiretor);
        if (repositorioDiretor.existePorNome(nome, id)) {
            throw new IllegalArgumentException("Já existe um diretor cadastrado com esse nome");
        }
        diretorExistente.setNome(nome);
        return repositorioDiretor.salvar(diretorExistente);
    }

    public void excluir(Long id) {
        if (!repositorioDiretor.excluirPorId(id)) {
            throw new NoSuchElementException("Diretor não encontrado");
        }
    }

    private String validarNome(diretor dadosDiretor) {
        if (dadosDiretor == null) {
            throw new IllegalArgumentException("O diretor deve ser informado");
        }
        String nome = dadosDiretor.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do diretor deve ser informado");
        }
        return nome.trim();
    }
}
