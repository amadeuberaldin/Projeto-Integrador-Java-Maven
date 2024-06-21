package com.mycompany.minhadispensa;

import java.io.Serializable;
import java.util.Objects;

public class IngredienteId implements Serializable {

    private Integer receita;
    private Long produto;

    public IngredienteId() {
    }

    public IngredienteId(Integer receita, Long produto) {
        this.receita = receita;
        this.produto = produto;
    }

    public Integer getReceita() {
        return receita;
    }

    public void setReceita(Integer receita) {
        this.receita = receita;
    }

    public Long getProduto() {
        return produto;
    }

    public void setProduto(Long produto) {
        this.produto = produto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(receita, produto);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        IngredienteId that = (IngredienteId) obj;
        return Objects.equals(receita, that.receita)
                && Objects.equals(produto, that.produto);
    }
}
