package com.example.back_end.model.service;

import com.example.back_end.model.domain.classe;
import com.example.back_end.model.repository.repositoryclasse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class srvcadastrarclasse {

    private final repositoryclasse repositorioClasse;

    public srvcadastrarclasse(repositoryclasse repositorioClasse) {
        this.repositorioClasse = repositorioClasse;
    }

    public classe cadastrar(classe novaClasse) {
        validar(novaClasse);
        String nome = novaClasse.getNome().trim();
        if (repositorioClasse.existePorNome(nome, null)) {
            throw new IllegalArgumentException("Já existe uma classe cadastrada com esse nome");
        }
        novaClasse.setId(null);
        novaClasse.setNome(nome);
        return repositorioClasse.salvar(novaClasse);
    }

    public List<classe> listarTodos() {
        return repositorioClasse.listarTodos();
    }

    public classe buscarPorId(Long id) {
        return repositorioClasse.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Classe não encontrada"));
    }

    public classe atualizar(Long id, classe dadosClasse) {
        classe classeExistente = buscarPorId(id);
        validar(dadosClasse);
        String nome = dadosClasse.getNome().trim();
        if (repositorioClasse.existePorNome(nome, id)) {
            throw new IllegalArgumentException("Já existe uma classe cadastrada com esse nome");
        }
        classeExistente.setNome(nome);
        classeExistente.setValorDiaria(dadosClasse.getValorDiaria());
        return repositorioClasse.salvar(classeExistente);
    }

    public void excluir(Long id) {
        if (!repositorioClasse.excluirPorId(id)) {
            throw new NoSuchElementException("Classe não encontrada");
        }
    }

    private void validar(classe dadosClasse) {
        if (dadosClasse == null) {
            throw new IllegalArgumentException("A classe deve ser informada");
        }
        if (dadosClasse.getNome() == null || dadosClasse.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome da classe deve ser informado");
        }
        BigDecimal valorDiaria = dadosClasse.getValorDiaria();
        if (valorDiaria == null || valorDiaria.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da diária deve ser maior que zero");
        }
    }
}
