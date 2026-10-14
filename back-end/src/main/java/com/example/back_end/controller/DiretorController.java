package com.example.back_end.controller;

import com.example.back_end.model.domain.Diretor;
import com.example.back_end.model.service.DiretorService;
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
public class DiretorController {

    private final DiretorService diretorService;

    public DiretorController(DiretorService diretorService) {
        this.diretorService = diretorService;
    }

    @GetMapping
    public List<Diretor> listarTodos() {
        return diretorService.listarTodos();
    }

    @GetMapping("/{id}")
    public Diretor buscarPorId(@PathVariable Long id) {
        return diretorService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Diretor> cadastrar(@RequestBody Diretor dadosDiretor) {
        Diretor diretorCadastrado = diretorService.cadastrar(dadosDiretor);
        return ResponseEntity.created(URI.create("/api/diretores/" + diretorCadastrado.getId()))
                .body(diretorCadastrado);
    }

    @PutMapping("/{id}")
    public Diretor atualizar(@PathVariable Long id, @RequestBody Diretor dadosDiretor) {
        return diretorService.atualizar(id, dadosDiretor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        diretorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
