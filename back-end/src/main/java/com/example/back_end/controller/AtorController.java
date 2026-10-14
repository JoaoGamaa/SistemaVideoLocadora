package com.example.back_end.controller;

import com.example.back_end.model.domain.Ator;
import com.example.back_end.model.service.AtorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/atores")
public class AtorController {

    private final AtorService atorService;

    public AtorController(AtorService atorService) {
        this.atorService = atorService;
    }

    @GetMapping
    public List<Ator> listarTodos() {
        return atorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Ator buscarPorId(@PathVariable Long id) {
        return atorService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Ator> cadastrar(@RequestBody Ator dadosAtor) {
        Ator atorCadastrado = atorService.cadastrar(dadosAtor);
        return ResponseEntity.created(URI.create("/api/atores/" + atorCadastrado.getId()))
                .body(atorCadastrado);
    }

    @PutMapping("/{id}")
    public Ator atualizar(@PathVariable Long id, @RequestBody Ator dadosAtor) {
        return atorService.atualizar(id, dadosAtor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        atorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
