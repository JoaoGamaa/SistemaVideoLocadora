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

import java.util.Objects;

@Entity
@Table(name = "titulos")
@Schema(description = "Obra disponível no catálogo da videolocadora")
public class Titulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador gerado pelo sistema", example = "1")
    private Long id;

    @Column(nullable = false, length = 120)
    @Schema(description = "Nome da obra", example = "Central do Brasil")
    private String nome;

    @Column(name = "ano_lancamento", nullable = false)
    @Schema(description = "Ano em que a obra foi lançada", example = "1998")
    private Integer anoLancamento;

    @Column(nullable = false, length = 60)
    @Schema(description = "Gênero principal da obra", example = "Drama")
    private String genero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "diretor_id", nullable = false)
    @Schema(description = "Diretor cadastrado responsável pela obra")
    private Diretor diretor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "classe_id", nullable = false)
    @Schema(description = "Classe que determina o valor da locação")
    private Classe classe;

    public Titulo() {
    }

    public Titulo(String nome, Integer anoLancamento, String genero, Diretor diretor, Classe classe) {
        this.nome = nome;
        this.anoLancamento = anoLancamento;
        this.genero = genero;
        this.diretor = diretor;
        this.classe = classe;
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

    public Integer getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(Integer anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Diretor getDiretor() {
        return diretor;
    }

    public void setDiretor(Diretor diretor) {
        this.diretor = diretor;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Titulo outroTitulo)) {
            return false;
        }
        return id != null && Objects.equals(id, outroTitulo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
