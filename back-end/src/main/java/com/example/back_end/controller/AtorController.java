package com.example.back_end.controller;

import com.example.back_end.model.domain.Ator;
import com.example.back_end.model.service.AtorService;
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
@RequestMapping("/api/atores")
@Tag(name = "Atores", description = "Operações para administrar os atores cadastrados")
public class AtorController {

    private final AtorService atorService;

    public AtorController(AtorService atorService) {
        this.atorService = atorService;
    }

    @GetMapping
    @Operation(summary = "Listar atores", description = "Retorna todos os atores em ordem de cadastro.")
    @ApiResponse(responseCode = "200", description = "Atores listados com sucesso")
    public List<Ator> listarTodos() {
        return atorService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar ator", description = "Busca um ator pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ator encontrado"),
            @ApiResponse(responseCode = "404", description = "Ator não encontrado")
    })
    public Ator buscarPorId(
            @Parameter(description = "Identificador do ator", example = "1") @PathVariable Long id
    ) {
        return atorService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Cadastrar ator", description = "Cria um ator com nome único no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ator cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado")
    })
    public ResponseEntity<Ator> cadastrar(@RequestBody Ator dadosAtor) {
        Ator atorCadastrado = atorService.cadastrar(dadosAtor);
        return ResponseEntity.created(URI.create("/api/atores/" + atorCadastrado.getId()))
                .body(atorCadastrado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ator", description = "Altera o nome de um ator existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ator atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Ator não encontrado")
    })
    public Ator atualizar(
            @Parameter(description = "Identificador do ator", example = "1") @PathVariable Long id,
            @RequestBody Ator dadosAtor
    ) {
        return atorService.atualizar(id, dadosAtor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir ator", description = "Remove definitivamente um ator cadastrado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ator excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ator não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do ator", example = "1") @PathVariable Long id
    ) {
        atorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
