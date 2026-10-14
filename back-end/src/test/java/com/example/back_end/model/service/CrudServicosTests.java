package com.example.back_end.model.service;

import com.example.back_end.model.domain.Ator;
import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.domain.Diretor;
import com.example.back_end.model.domain.Item;
import com.example.back_end.model.domain.Titulo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Autowired
    private TituloService servicoTitulo;

    @Autowired
    private ItemService servicoItem;

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

    @Test
    void deveExecutarCrudDeTitulo() {
        Diretor diretor = servicoDiretor.cadastrar(new Diretor("Fernando Meirelles"));
        Classe classe = servicoClasse.cadastrar(
                new Classe("Catálogo", new BigDecimal("8.00"))
        );
        Titulo cadastrado = servicoTitulo.cadastrar(
                new Titulo("Cidade de Deus", 2002, "Drama", diretor, classe)
        );

        Titulo atualizado = servicoTitulo.atualizar(
                cadastrado.getId(),
                new Titulo("Cidade de Deus", 2002, "Drama policial", diretor, classe)
        );

        assertEquals("Drama policial", atualizado.getGenero());
        assertEquals(diretor.getId(), atualizado.getDiretor().getId());
        assertEquals(1, servicoTitulo.listarTodos().size());

        servicoTitulo.excluir(cadastrado.getId());
        assertThrows(
                NoSuchElementException.class,
                () -> servicoTitulo.buscarPorId(cadastrado.getId())
        );
    }

    @Test
    void deveExecutarCrudDeItem() {
        Diretor diretor = servicoDiretor.cadastrar(new Diretor("Anna Muylaert"));
        Classe classe = servicoClasse.cadastrar(
                new Classe("Nacional", new BigDecimal("9.50"))
        );
        Titulo titulo = servicoTitulo.cadastrar(
                new Titulo("Que Horas Ela Volta?", 2015, "Drama", diretor, classe)
        );
        Item cadastrado = servicoItem.cadastrar(
                new Item("DVD-0001", "DVD", LocalDate.of(2024, 1, 10), true, titulo)
        );

        Item atualizado = servicoItem.atualizar(
                cadastrado.getId(),
                new Item(
                        "BLU-0001",
                        "BLU_RAY",
                        LocalDate.of(2024, 2, 20),
                        false,
                        titulo
                )
        );

        assertEquals("BLU-0001", atualizado.getNumeroSerie());
        assertEquals("BLU_RAY", atualizado.getTipo());
        assertFalse(atualizado.isDisponivel());
        assertEquals(1, servicoItem.listarTodos().size());

        servicoItem.excluir(cadastrado.getId());
        assertThrows(
                NoSuchElementException.class,
                () -> servicoItem.buscarPorId(cadastrado.getId())
        );
    }
}
