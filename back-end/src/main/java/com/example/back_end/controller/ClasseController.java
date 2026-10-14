package com.example.back_end.controller;

import com.example.back_end.model.domain.Classe;
import com.example.back_end.model.service.ClasseService;
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
@RequestMapping("/api/classes")
@Tag(name = "Classes", description = "Operações para administrar categorias e valores de diária")
public class ClasseController {

    private final ClasseService classeService;

    public ClasseController(ClasseService classeService) {
        this.classeService = classeService;
    }

    @GetMapping
    @Operation(summary = "Listar classes", description = "Retorna todas as classes em ordem de cadastro.")
    @ApiResponse(responseCode = "200", description = "Classes listadas com sucesso")
    public List<Classe> listarTodos() {
        return classeService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar classe", description = "Busca uma classe pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classe encontrada"),
            @ApiResponse(responseCode = "404", description = "Classe não encontrada")
    })
    public Classe buscarPorId(
            @Parameter(description = "Identificador da classe", example = "1") @PathVariable Long id
    ) {
        return classeService.buscarPorId(id);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar classe",
            description = "Cria uma categoria de locação com nome único e valor de diária."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Classe cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado")
    })
    public ResponseEntity<Classe> cadastrar(@RequestBody Classe dadosClasse) {
        Classe classeCadastrada = classeService.cadastrar(dadosClasse);
        return ResponseEntity.created(URI.create("/api/classes/" + classeCadastrada.getId()))
                .body(classeCadastrada);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar classe",
            description = "Altera o nome e o valor da diária de uma classe existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classe atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou nome já cadastrado"),
            @ApiResponse(responseCode = "404", description = "Classe não encontrada")
    })
    public Classe atualizar(
            @Parameter(description = "Identificador da classe", example = "1") @PathVariable Long id,
            @RequestBody Classe dadosClasse
    ) {
        return classeService.atualizar(id, dadosClasse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir classe", description = "Remove definitivamente uma classe cadastrada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Classe excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Classe não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador da classe", example = "1") @PathVariable Long id
    ) {
        classeService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
