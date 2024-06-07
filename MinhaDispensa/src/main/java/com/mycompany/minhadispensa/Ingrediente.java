/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minhadispensa;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;

@Entity
@Table(name = "Ingrediente")
@IdClass(IngredienteId.class) // Classe de chave composta
public class Ingrediente {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receita_id", referencedColumnName = "id")
    private Receita receita;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
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
