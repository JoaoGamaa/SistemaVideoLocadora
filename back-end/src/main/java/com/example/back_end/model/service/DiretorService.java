package com.example.back_end.model.service;

import com.example.back_end.model.domain.Diretor;
import com.example.back_end.model.repository.DiretorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class DiretorService {

    private static final int TAMANHO_MAXIMO_NOME = 100;

    private final DiretorRepository diretorRepository;

    public DiretorService(DiretorRepository diretorRepository) {
        this.diretorRepository = diretorRepository;
    }

    public Diretor cadastrar(Diretor dadosDiretor) {
        String nome = validarNome(dadosDiretor);
        validarNomeDuplicado(nome, null);
        return diretorRepository.save(new Diretor(nome));
    }

    @Transactional(readOnly = true)
    public List<Diretor> listarTodos() {
        return diretorRepository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Diretor buscarPorId(Long id) {
        return diretorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Diretor não encontrado"));
    }

    public Diretor atualizar(Long id, Diretor dadosDiretor) {
        Diretor diretorExistente = buscarPorId(id);
        String nome = validarNome(dadosDiretor);
        validarNomeDuplicado(nome, id);

        diretorExistente.setNome(nome);
        return diretorRepository.save(diretorExistente);
    }

    public void excluir(Long id) {
        Diretor diretorExistente = buscarPorId(id);
        diretorRepository.delete(diretorExistente);
    }

    private String validarNome(Diretor dadosDiretor) {
        if (dadosDiretor == null) {
            throw new IllegalArgumentException("O diretor deve ser informado");
        }

        String nome = dadosDiretor.getNome();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do diretor deve ser informado");
        }

        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new IllegalArgumentException("O nome do diretor deve ter no máximo 100 caracteres");
        }
        return nome;
    }

    private void validarNomeDuplicado(String nome, Long idIgnorado) {
        boolean duplicado = idIgnorado == null
                ? diretorRepository.existsByNomeIgnoreCase(nome)
                : diretorRepository.existsByNomeIgnoreCaseAndIdNot(nome, idIgnorado);

        if (duplicado) {
            throw new IllegalArgumentException("Já existe um diretor cadastrado com esse nome");
        }
    }
}
