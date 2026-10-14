package com.example.back_end.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "atores")
@Schema(description = "Ator que pode participar dos títulos do acervo")
public class Ator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador gerado pelo sistema", example = "1")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Nome completo do ator", example = "Fernanda Montenegro")
    private String nome;

    public Ator() {
    }

    public Ator(String nome) {
        this.nome = nome;
    }

    public Ator(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Ator outroAtor)) {
            return false;
        }
        return id != null && Objects.equals(id, outroAtor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
