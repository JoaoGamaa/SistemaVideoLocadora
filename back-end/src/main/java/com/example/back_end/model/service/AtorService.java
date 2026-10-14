package com.example.back_end.model.service;

import com.example.back_end.model.domain.Ator;
import com.example.back_end.model.repository.AtorRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class AtorService {

    private static final int TAMANHO_MAXIMO_NOME = 100;

    private final AtorRepository atorRepository;

    public AtorService(AtorRepository atorRepository) {
        this.atorRepository = atorRepository;
    }

    public Ator cadastrar(Ator dadosAtor) {
        String nome = validarNome(dadosAtor);
        validarNomeDuplicado(nome, null);
        return atorRepository.save(new Ator(nome));
    }

    @Transactional(readOnly = true)
    public List<Ator> listarTodos() {
        return atorRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public Ator buscarPorId(Long id) {
        return atorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ator não encontrado"));
    }

    public Ator atualizar(Long id, Ator dadosAtor) {
        Ator atorExistente = buscarPorId(id);
        String nome = validarNome(dadosAtor);
        validarNomeDuplicado(nome, id);

        atorExistente.setNome(nome);
        return atorRepository.save(atorExistente);
    }

    public void excluir(Long id) {
        Ator atorExistente = buscarPorId(id);
        atorRepository.delete(atorExistente);
    }

    private String validarNome(Ator dadosAtor) {
        if (dadosAtor == null) {
            throw new IllegalArgumentException("O ator deve ser informado");
        }

        String nome = dadosAtor.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do ator deve ser informado");
        }

        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException("O nome do ator deve ter no máximo 100 caracteres");
        }
        return nome;
    }

    private void validarNomeDuplicado(String nome, Long idIgnorado) {
        boolean duplicado = idIgnorado == null
                ? atorRepository.existsByNomeIgnoreCase(nome)
                : atorRepository.existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);

        if (duplicado) {
            throw new IllegalArgumentException("Já existe um ator cadastrado com esse nome");
        }
    }
}
