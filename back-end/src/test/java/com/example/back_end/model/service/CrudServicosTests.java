package com.example.back_end.model.service;

import com.example.back_end.model.domain.ator;
import com.example.back_end.model.domain.classe;
import com.example.back_end.model.domain.diretor;
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
    private srvcadastrarator servicoAtor;

    @Autowired
    private srvcadastrardiretor servicoDiretor;

    @Autowired
    private srvcadastrarclasse servicoClasse;

    @Test
    void deveExecutarCrudDeAtor() {
        ator cadastrado = servicoAtor.cadastrar(new ator("Fernanda Montenegro"));

        ator atualizado = servicoAtor.atualizar(cadastrado.getId(), new ator("Fernanda M. Torres"));

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
        diretor cadastrado = servicoDiretor.cadastrar(new diretor("Walter Salles"));

        diretor atualizado = servicoDiretor.atualizar(
                cadastrado.getId(),
                new diretor("Walter Moreira Salles")
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
        classe cadastrada = servicoClasse.cadastrar(
                new classe("Lançamento", new BigDecimal("12.50"))
        );

        classe atualizada = servicoClasse.atualizar(
                cadastrada.getId(),
                new classe("Super lançamento", new BigDecimal("15.00"))
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
