package com.example.back_end.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "itens")
@Schema(description = "Exemplar físico de um título disponível no estoque")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador gerado pelo sistema", example = "1")
    private Long id;

    @Column(name = "numero_serie", nullable = false, unique = true, length = 50)
    @Schema(description = "Código único que identifica o exemplar", example = "DVD-0001")
    private String numeroSerie;

    @Column(nullable = false, length = 20)
    @Schema(
            description = "Tipo de mídia do exemplar",
            example = "DVD",
            allowableValues = {"DVD", "BLU_RAY", "VHS"}
    )
    private String tipo;

    @Column(name = "data_aquisicao", nullable = false)
    @Schema(description = "Data em que o item foi adquirido", example = "2026-10-14")
    private LocalDate dataAquisicao;

    @Column(nullable = false)
    @Schema(description = "Indica se o item está disponível para locação", example = "true")
    private boolean disponivel = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "titulo_id", nullable = false)
    @Schema(description = "Título ao qual este exemplar pertence")
    private Titulo titulo;

    public Item() {
    }

    public Item(
            String numeroSerie,
            String tipo,
            LocalDate dataAquisicao,
            boolean disponivel,
            Titulo titulo
    ) {
        this.numeroSerie = numeroSerie;
        this.tipo = tipo;
        this.dataAquisicao = dataAquisicao;
        this.disponivel = disponivel;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Item outroItem)) {
            return false;
        }
        return id != null && Objects.equals(id, outroItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
