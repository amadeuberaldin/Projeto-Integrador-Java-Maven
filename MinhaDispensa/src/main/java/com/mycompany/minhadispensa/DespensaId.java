package com.mycompany.minhadispensa;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class DespensaId implements Serializable {

    private Long usuarioId;
    private Long produtoId;

    public DespensaId() {
    }

    public DespensaId(Long usuarioId, Long produtoId) {
        this.usuarioId = usuarioId;
        this.produtoId = produtoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DespensaId that = (DespensaId) o;
        return Objects.equals(usuarioId, that.usuarioId)
                && Objects.equals(produtoId, that.produtoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId, produtoId);
    }
}
