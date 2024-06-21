package com.mycompany.minhadispensa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MinhaDispensa {

    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        // Inicializa o EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("MinhaDispensaPU");

        SwingUtilities.invokeLater(() -> iniciarInterfaceGrafica());
    }

    private static void iniciarInterfaceGrafica() {
        EntityManager em = emf.createEntityManager();  // Cria o EntityManager
        Login loginFrame = new Login(em);              // Passa o EntityManager para a tela de login
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setTitle("Login - Minha Dispensa");
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);

        // Adiciona um listener para fechar o EntityManagerFactory ao fechar a aplicação
        Runtime.getRuntime().addShutdownHook(new Thread(MinhaDispensa::fecharFactory));
    }

    public static void fecharFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close(); // Garante que a EntityManagerFactory seja fechada ao sair
        }
    }
}
