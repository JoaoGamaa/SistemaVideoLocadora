package com.example.back_end.controller;

import com.example.back_end.model.domain.classe;
import com.example.back_end.model.service.srvcadastrarclasse;
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
@RequestMapping("/api/classes")
public class ctrcadastrarclasse {

    private final srvcadastrarclasse servicoClasse;

    public ctrcadastrarclasse(srvcadastrarclasse servicoClasse) {
        this.servicoClasse = servicoClasse;
    }

    @GetMapping
    public List<classe> listarTodos() {
        return servicoClasse.listarTodos();
    }

    @GetMapping("/{id}")
    public classe buscarPorId(@PathVariable Long id) {
        return servicoClasse.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<classe> cadastrar(@RequestBody classe novaClasse) {
        classe classeCadastrada = servicoClasse.cadastrar(novaClasse);
        return ResponseEntity.created(URI.create("/api/classes/" + classeCadastrada.getId()))
                .body(classeCadastrada);
    }

    @PutMapping("/{id}")
    public classe atualizar(@PathVariable Long id, @RequestBody classe dadosClasse) {
        return servicoClasse.atualizar(id, dadosClasse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoClasse.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
