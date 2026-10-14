package com.example.back_end.model.service;

import com.example.back_end.model.domain.Ator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AtorServiceTests {

    @Autowired
    private AtorService servico;

    @Test
    void deveCadastrarAtor() {
        Ator atorCadastrado = servico.cadastrar(new Ator("  Fernanda Montenegro  "));

        assertNotNull(atorCadastrado.getId());
        assertEquals("Fernanda Montenegro", atorCadastrado.getNome());
    }

    @Test
    void naoDeveCadastrarAtorSemNome() {
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.cadastrar(new Ator("  "))
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
        servico.cadastrar(new Ator("Fernanda Montenegro"));

        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> servico.cadastrar(new Ator("fernanda montenegro"))
        );

        assertEquals("Já existe um ator cadastrado com esse nome", erro.getMessage());
    }
}
