/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
}
