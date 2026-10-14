package com.example.back_end.model.service;

import com.example.back_end.model.domain.Ator;
import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.domain.Diretor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CrudServicosTests {

    @Autowired
    private AtorService servicoAtor;

    @Autowired
    private DiretorService servicoDiretor;

    @Autowired
    private ClasseService servicoClasse;

    @Test
    void deveExecutarCrudDeAtor() {
        Ator cadastrado = servicoAtor.cadastrar(new Ator("Fernanda Montenegro"));

        Ator atualizado = servicoAtor.atualizar(cadastrado.getId(), new Ator("Fernanda M. Torres"));

        assertEquals("Fernanda M. Torres", atualizado.getNome());
        assertEquals(1, servicoAtor.listarTodos().size());

        servicoAtor.excluir(cadastrado.getId());
        assertThrows(
                NoSuchElementException.class,
                () -> servicoAtor.buscarPorId(cadastrado.getId())
        );
    }

    @Test
    void deveExecutarCrudDeDiretor() {
        Diretor cadastrado = servicoDiretor.cadastrar(new Diretor("Walter Salles"));

        Diretor atualizado = servicoDiretor.atualizar(
                cadastrado.getId(),
                new Diretor("Walter Moreira Salles")
        );

        assertEquals("Walter Moreira Salles", atualizado.getNome());
        assertEquals(1, servicoDiretor.listarTodos().size());

        servicoDiretor.excluir(cadastrado.getId());
        assertThrows(
                NoSuchElementException.class,
                () -> servicoDiretor.buscarPorId(cadastrado.getId())
        );
    }

    @Test
    void deveExecutarCrudDeClasse() {
        Classe cadastrada = servicoClasse.cadastrar(
                new Classe("Lançamento", new BigDecimal("12.50"))
        );

        Classe atualizada = servicoClasse.atualizar(
                cadastrada.getId(),
                new Classe("Super lançamento", new BigDecimal("15.00"))
        );

        assertEquals("Super lançamento", atualizada.getNome());
        assertEquals(new BigDecimal("15.00"), atualizada.getValorDiaria());
        assertEquals(1, servicoClasse.listarTodos().size());

        servicoClasse.excluir(cadastrada.getId());
        assertThrows(
                NoSuchElementException.class,
                () -> servicoClasse.buscarPorId(cadastrada.getId())
        );
    }
}
