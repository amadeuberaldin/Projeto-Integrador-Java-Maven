/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minhadispensa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import org.mindrot.jbcrypt.BCrypt;

public class LoginScreen {

    private EntityManager em;

    public LoginScreen(EntityManager em) {
        this.em = em;
    }

    public String verificarCredenciais(String email, String senhaPlana) {
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            query.setParameter("email", email);
            Usuario usuario = query.getSingleResult();

            if (BCrypt.checkpw(senhaPlana, usuario.getSenha())) {
                return usuario.getNome();
            }
        } catch (NoResultException ex) {
            System.out.println("Usuário não encontrado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
