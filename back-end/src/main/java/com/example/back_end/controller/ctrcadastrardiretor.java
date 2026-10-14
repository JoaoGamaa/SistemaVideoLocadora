package com.example.back_end.controller;

import com.example.back_end.model.domain.diretor;
import com.example.back_end.model.service.srvcadastrardiretor;
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
@RequestMapping("/api/diretores")
public class ctrcadastrardiretor {

    private final srvcadastrardiretor servicoDiretor;

    public ctrcadastrardiretor(srvcadastrardiretor servicoDiretor) {
        this.servicoDiretor = servicoDiretor;
    }

    @GetMapping
    public List<diretor> listarTodos() {
        return servicoDiretor.listarTodos();
    }

    @GetMapping("/{id}")
    public diretor buscarPorId(@PathVariable Long id) {
        return servicoDiretor.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<diretor> cadastrar(@RequestBody diretor novoDiretor) {
        diretor diretorCadastrado = servicoDiretor.cadastrar(novoDiretor);
        return ResponseEntity.created(URI.create("/api/diretores/" + diretorCadastrado.getId()))
                .body(diretorCadastrado);
    }

    @PutMapping("/{id}")
    public diretor atualizar(@PathVariable Long id, @RequestBody diretor dadosDiretor) {
        return servicoDiretor.atualizar(id, dadosDiretor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoDiretor.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
