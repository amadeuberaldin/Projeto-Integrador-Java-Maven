package com.mycompany.minhadispensa;

import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    public void cadastrarUsuario(String nome, String email, String senhaPlana, int idade, char sexo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin(); // Inicia a transação

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(BCrypt.hashpw(senhaPlana, BCrypt.gensalt())); // Criptografa a senha antes de salvar
            usuario.setIdade(idade);
            usuario.setSexo(sexo);

            em.persist(usuario); // Persiste o objeto no banco de dados
            em.getTransaction().commit(); // Commita a transação

            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } catch (Exception ex) {
            em.getTransaction().rollback(); // Reverte a transação em caso de falha
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close(); // Fecha o EntityManager
        }
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        } finally {
            em.close();
        }
    }

    public void adicionarProdutoNaDespensa(Long usuarioId, Long produtoId, Float quantidade) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, usuarioId);
            Produto produto = em.find(Produto.class, produtoId);

            if (usuario == null || produto == null) {
                throw new IllegalArgumentException("Usuário ou Produto não encontrado.");
            }

            DespensaId despensaId = new DespensaId(usuarioId, produtoId);
            Despensa despensa = em.find(Despensa.class, despensaId);
            if (despensa == null) {
                despensa = new Despensa();
                despensa.setId(despensaId);
                despensa.setUsuario(usuario);
                despensa.setProduto(produto);
                despensa.setQuantidade(quantidade);
                em.persist(despensa);
            } else {
                despensa.setQuantidade(despensa.getQuantidade() + quantidade);
                em.merge(despensa);
            }

            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Produto adicionado na despensa com sucesso!");
        } catch (Exception ex) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar produto na despensa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close();
        }
    }
}
