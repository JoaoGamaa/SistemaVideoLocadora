package com.example.back_end.controller;

import com.example.back_end.model.domain.ator;
import com.example.back_end.model.service.srvcadastrarator;
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
public class ctrcadastrarator {

    private final srvcadastrarator servicoAtor;

    public ctrcadastrarator(srvcadastrarator servicoAtor) {
        this.servicoAtor = servicoAtor;
    }

    @GetMapping
    public List<ator> listarTodos() {
        return servicoAtor.listarTodos();
    }

    @GetMapping("/{id}")
    public ator buscarPorId(@PathVariable Long id) {
        return servicoAtor.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ator> cadastrar(@RequestBody ator novoAtor) {
        ator atorCadastrado = servicoAtor.cadastrar(novoAtor);
        return ResponseEntity.created(URI.create("/api/atores/" + atorCadastrado.getId()))
                .body(atorCadastrado);
    }

    @PutMapping("/{id}")
    public ator atualizar(@PathVariable Long id, @RequestBody ator dadosAtor) {
        return servicoAtor.atualizar(id, dadosAtor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoAtor.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
