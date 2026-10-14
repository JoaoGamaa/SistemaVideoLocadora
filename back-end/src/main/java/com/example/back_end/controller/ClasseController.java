package com.example.back_end.controller;

import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.service.ClasseService;
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
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @GetMapping
    public List<Classe> listarTodos() {
        return classeService.listarTodos();
    }

    @GetMapping("/{id}")
    public Classe buscarPorId(@PathVariable Long id) {
        return classeService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Classe> cadastrar(@RequestBody Classe dadosClasse) {
        Classe classeCadastrada = classeService.cadastrar(dadosClasse);
        return ResponseEntity.created(URI.create("/api/classes/" + classeCadastrada.getId()))
                .body(classeCadastrada);
    }

    @PutMapping("/{id}")
    public Classe atualizar(@PathVariable Long id, @RequestBody Classe dadosClasse) {
        return classeService.atualizar(id, dadosClasse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        classeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
