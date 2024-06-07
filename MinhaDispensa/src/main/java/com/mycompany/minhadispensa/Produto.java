/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minhadispensa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "produto")
public class Produto {
    
   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false)
    private Float quantidade;

    @Column(name = "porcao_referencia")  // Corrige o mapeamento para a coluna correta
    private Float porcaoReferencia;

    @Column(name = "proteinas")  // Opcional se o nome no banco é idêntico
    private Float proteinas;

    @Column(name = "carboidratos")  // Opcional se o nome no banco é idêntico
    private Float carboidratos;

    @Column(name = "calorias")  // Opcional se o nome no banco é idêntico
    private Float calorias;

    @Column(name = "gorduras_totais")  // Corrige o mapeamento para a coluna correta
    private Float gordurasTotais;


    // Construtor padrão exigido pelo JPA
    public Produto() {
    }

    // Construtor com nome e quantidade
    public Produto(String nome, Float quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    // Getters e setters
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

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public Float getPorcaoReferencia() {
        return porcaoReferencia;
    }

    public void setPorcaoReferencia(Float porcaoReferencia) {
        this.porcaoReferencia = porcaoReferencia;
    }

    public Float getProteinas() {
        return proteinas;
    }

    public void setProteinas(Float proteinas) {
        this.proteinas = proteinas;
    }

    public Float getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(Float carboidratos) {
        this.carboidratos = carboidratos;
    }

    public Float getCalorias() {
        return calorias;
    }

    public void setCalorias(Float calorias) {
        this.calorias = calorias;
    }

    public Float getGordurasTotais() {
        return gordurasTotais;
    }

    public void setGordurasTotais(Float gordurasTotais) {
        this.gordurasTotais = gordurasTotais;
    }
}
