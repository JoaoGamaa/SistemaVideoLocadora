package com.example.back_end.model.service;

import com.example.back_end.model.domain.ator;
import com.example.back_end.model.repository.repositoryator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SrvcadastraratorTests {

    private srvcadastrarator servico;

    @BeforeEach
    void configurar() {
        servico = new srvcadastrarator(new repositoryator());
    }

    @Test
    void deveCadastrarAtor() {
        ator atorCadastrado = servico.cadastrar(new ator("  Fernanda Montenegro  "));

        assertNotNull(atorCadastrado.getId());
        assertEquals("Fernanda Montenegro", atorCadastrado.getNome());
    }

    @Test
    void naoDeveCadastrarAtorSemNome() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.cadastrar(new ator("  "))
        );

        assertEquals("O nome do ator deve ser informado", erro.getMessage());
    }

    @Test
    void naoDeveCadastrarAtorNulo() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.cadastrar(null)
        );

        assertEquals("O ator deve ser informado", erro.getMessage());
    }

    @Test
    void naoDeveCadastrarAtorComNomeDuplicado() {
        servico.cadastrar(new ator("Fernanda Montenegro"));

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.cadastrar(new ator("fernanda montenegro"))
        );

        assertEquals("Já existe um ator cadastrado com esse nome", erro.getMessage());
    }
}
