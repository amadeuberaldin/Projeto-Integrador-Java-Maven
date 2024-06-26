package com.mycompany.minhadispensa;

import javax.persistence.*;

@Entity
@Table(name = "ingrediente")
@IdClass(IngredienteId.class)
public class Ingrediente {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_id")
    private Receita receita;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(nullable = false)
    private Float quantidade;

    // Construtores
    public Ingrediente() {
    }

    public Ingrediente(Receita receita, Produto produto, Float quantidade) {
        this.receita = receita;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    // Getters e setters
    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }
}
