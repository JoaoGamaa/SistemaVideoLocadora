package com.example.back_end.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "classes")
public class classe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nome;

    @Column(name = "valor_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorDiaria;

    public classe() {
    }

    public classe(String nome, BigDecimal valorDiaria) {
        this.nome = nome;
        this.valorDiaria = valorDiaria;
    }

    public classe(Long id, String nome, BigDecimal valorDiaria) {
        this.id = id;
        this.nome = nome;
        this.valorDiaria = valorDiaria;
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

    public BigDecimal getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(BigDecimal valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof classe outraClasse)) {
            return false;
        }
        return id != null && Objects.equals(id, outraClasse.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
