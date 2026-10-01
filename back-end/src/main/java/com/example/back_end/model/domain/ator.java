package com.example.back_end.model.domain;

import java.util.Objects;

public class ator {

    private Long id;
    private String nome;

    public ator() {
    }

    public ator(String nome) {
        this.nome = nome;
    }

    public ator(Long id, String nome) {
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
        if (!(objeto instanceof ator outroAtor)) {
            return false;
        }
        return id != null && Objects.equals(id, outroAtor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
