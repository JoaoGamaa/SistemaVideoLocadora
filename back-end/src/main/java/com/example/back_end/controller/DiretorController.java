package com.example.back_end.controller;

import com.example.back_end.model.domain.Diretor;
import com.example.back_end.model.service.DiretorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Diretores", description = "Operações para administrar os diretores do catálogo")
public class DiretorController {

    private final DiretorService diretorService;

    public DiretorController(DiretorService diretorService) {
        this.diretorService = diretorService;
    }

    @GetMapping
    @Operation(summary = "Listar diretores", description = "Retorna todos os diretores em ordem de cadastro.")
    @ApiResponse(responseCode = "200", description = "Diretores listados com sucesso")
    public List<Diretor> listarTodos() {
        return diretorService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar diretor", description = "Busca um diretor pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diretor encontrado"),
            @ApiResponse(responseCode = "404", description = "Diretor não encontrado")
    })
    public Diretor buscarPorId(
            @Parameter(description = "Identificador do diretor", example = "1") @PathVariable Long id
    ) {
        return diretorService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar diretor", description = "Cria um diretor com nome único no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Diretor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado")
    })
    public ResponseEntity<Diretor> cadastrar(@RequestBody Diretor dadosDiretor) {
        Diretor diretorCadastrado = diretorService.cadastrar(dadosDiretor);
        return ResponseEntity.created(URI.create("/api/diretores/" + diretorCadastrado.getId()))
                .body(diretorCadastrado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar diretor", description = "Altera o nome de um diretor existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diretor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Diretor não encontrado")
    })
    public Diretor atualizar(
            @Parameter(description = "Identificador do diretor", example = "1") @PathVariable Long id,
            @RequestBody Diretor dadosDiretor
    ) {
        return diretorService.atualizar(id, dadosDiretor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir diretor", description = "Remove definitivamente um diretor cadastrado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Diretor excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Diretor não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do diretor", example = "1") @PathVariable Long id
    ) {
        diretorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
