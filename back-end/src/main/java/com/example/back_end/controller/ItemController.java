package com.example.back_end.controller;

import com.example.back_end.model.domain.Item;
import com.example.back_end.model.service.ItemService;
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
@RequestMapping("/api/itens")
@Tag(name = "Itens", description = "Operações para administrar os exemplares físicos do estoque")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Listar itens", description = "Retorna todos os exemplares em ordem de cadastro.")
    @ApiResponse(responseCode = "200", description = "Itens listados com sucesso")
    public List<Item> listarTodos() {
        return itemService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar item", description = "Busca um exemplar pelo identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public Item buscarPorId(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id
    ) {
        return itemService.buscarPorId(id);
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar item",
            description = "Adiciona ao estoque um exemplar de um título já cadastrado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou número de série duplicado"),
            @ApiResponse(responseCode = "404", description = "Título não encontrado")
    })
    public ResponseEntity<Item> cadastrar(@RequestBody Item dadosItem) {
        Item itemCadastrado = itemService.cadastrar(dadosItem);
        return ResponseEntity.created(URI.create("/api/itens/" + itemCadastrado.getId()))
                .body(itemCadastrado);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar item",
            description = "Altera identificação, mídia, aquisição, disponibilidade ou título do exemplar."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou número de série duplicado"),
            @ApiResponse(responseCode = "404", description = "Item ou título não encontrado")
    })
    public Item atualizar(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id,
            @RequestBody Item dadosItem
    ) {
        return itemService.atualizar(id, dadosItem);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir item", description = "Remove definitivamente um exemplar do estoque.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do item", example = "1") @PathVariable Long id
    ) {
        itemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
