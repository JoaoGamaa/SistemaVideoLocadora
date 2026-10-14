package com.example.back_end.controller;

import com.example.back_end.model.domain.Titulo;
import com.example.back_end.model.service.TituloService;
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
@RequestMapping("/api/titulos")
@Tag(name = "Títulos", description = "Operações para administrar as obras do catálogo")
public class TituloController {

    private final TituloService tituloService;

    public TituloController(TituloService tituloService) {
        this.tituloService = tituloService;
    }

    @GetMapping
    @Operation(summary = "Listar títulos", description = "Retorna todos os títulos em ordem de cadastro.")
    @ApiResponse(responseCode = "200", description = "Títulos listados com sucesso")
    public List<Titulo> listarTodos() {
        return tituloService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar título", description = "Busca um título pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Título encontrado"),
            @ApiResponse(responseCode = "404", description = "Título não encontrado")
    })
    public Titulo buscarPorId(
            @Parameter(description = "Identificador do título", example = "1") @PathVariable Long id
    ) {
        return tituloService.buscarPorId(id);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar título",
            description = "Adiciona uma obra ao catálogo e a vincula a um diretor e a uma classe existentes."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Título cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados obrigatórios inválidos ou duplicados"),
            @ApiResponse(responseCode = "404", description = "Diretor ou classe não encontrado")
    })
    public ResponseEntity<Titulo> cadastrar(@RequestBody Titulo dadosTitulo) {
        Titulo tituloCadastrado = tituloService.cadastrar(dadosTitulo);
        return ResponseEntity.created(URI.create("/api/titulos/" + tituloCadastrado.getId()))
                .body(tituloCadastrado);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar título",
            description = "Altera os dados e os vínculos de um título existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Título atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados obrigatórios inválidos ou duplicados"),
            @ApiResponse(responseCode = "404", description = "Título, diretor ou classe não encontrado")
    })
    public Titulo atualizar(
            @Parameter(description = "Identificador do título", example = "1") @PathVariable Long id,
            @RequestBody Titulo dadosTitulo
    ) {
        return tituloService.atualizar(id, dadosTitulo);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir título",
            description = "Remove um título, desde que ele não possua itens vinculados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Título excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "O título possui itens cadastrados"),
            @ApiResponse(responseCode = "404", description = "Título não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do título", example = "1") @PathVariable Long id
    ) {
        tituloService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
