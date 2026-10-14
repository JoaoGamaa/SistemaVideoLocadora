package com.example.back_end.model.service;

import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.repository.ClasseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ClasseService {

    private static final int TAMANHO_MAXIMO_NOME = 60;

    private final ClasseRepository classeRepository;

    public ClasseService(ClasseRepository classeRepository) {
        this.classeRepository = classeRepository;
    }

    public Classe cadastrar(Classe dadosClasse) {
        String nome = validar(dadosClasse);
        validarNomeDuplicado(nome, null);
        return classeRepository.save(new Classe(nome, dadosClasse.getValorDiaria()));
    }

    @Transactional(readOnly = true)
    public List<Classe> listarTodos() {
        return classeRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Classe buscarPorId(Long id) {
        return classeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Classe não encontrada"));
    }

    public Classe atualizar(Long id, Classe dadosClasse) {
        Classe classeExistente = buscarPorId(id);
        String nome = validar(dadosClasse);
        validarNomeDuplicado(nome, id);

        classeExistente.setNome(nome);
        classeExistente.setValorDiaria(dadosClasse.getValorDiaria());
        return classeRepository.save(classeExistente);
    }

    public void excluir(Long id) {
        Classe classeExistente = buscarPorId(id);
        classeRepository.delete(classeExistente);
    }

    private String validar(Classe dadosClasse) {
        if (dadosClasse == null) {
            throw new IllegalArgumentException("A classe deve ser informada");
        }

        String nome = dadosClasse.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da classe deve ser informado");
        }

        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException("O nome da classe deve ter no máximo 60 caracteres");
        }

        BigDecimal valorDiaria = dadosClasse.getValorDiaria();
        if (valorDiaria == null || valorDiaria.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da diária deve ser maior que zero");
        }
        return nome;
    }

    private void validarNomeDuplicado(String nome, Long idIgnorado) {
        boolean duplicado = idIgnorado == null
                ? classeRepository.existsByNomeIgnoreCase(nome)
                : classeRepository.existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);

        if (duplicado) {
            throw new IllegalArgumentException("Já existe uma classe cadastrada com esse nome");
        }
    }
}
