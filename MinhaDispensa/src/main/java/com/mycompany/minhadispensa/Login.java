package com.mycompany.minhadispensa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends javax.swing.JFrame {

    private EntityManager em;

    public Login(EntityManager em) {
        this.em = em;
        initComponents();
    }

    private boolean authenticateUser(String email, String senha, EntityManager em) {
        try {
            Usuario usuario = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return usuario != null && BCrypt.checkpw(senha, usuario.getSenha());
        } catch (NoResultException nre) {
            // Usuário não encontrado
            JOptionPane.showMessageDialog(null, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao autenticar usuário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private Usuario getAuthenticatedUser(String email, String senhaPlana, EntityManager em) {
        try {
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
            query.setParameter("email", email);
            Usuario usuario = query.getSingleResult();

            if (usuario != null && BCrypt.checkpw(senhaPlana, usuario.getSenha())) {
                return usuario;
            }
        } catch (NoResultException ex) {
            // Nenhum usuário encontrado com esse email
            return null;
        } catch (Exception ex) {
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jlblTitulo = new javax.swing.JLabel();
        jlblLogin = new javax.swing.JLabel();
        jtxfLogin = new javax.swing.JTextField();
        jlblSenha = new javax.swing.JLabel();
        jpfSenha = new javax.swing.JPasswordField();
        jbtLogin = new javax.swing.JButton();
        jbtCadastro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jlblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jlblTitulo.setText("Minha Dispensa");

        jlblLogin.setText("Email");

        jlblSenha.setText("Senha");

        jbtLogin.setText("Login");
        jbtLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtLoginActionPerformed(evt);
            }
        });

        jbtCadastro.setText("Cadastro");
        jbtCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtCadastroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(76, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jlblSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jtxfLogin)
                                .addComponent(jpfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jbtCadastro)
                                .addGap(29, 29, 29)
                                .addComponent(jbtLogin))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jlblLogin)))
                        .addGap(60, 60, 60))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jlblTitulo)
                        .addGap(107, 107, 107))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jlblTitulo)
                .addGap(18, 18, 18)
                .addComponent(jlblLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxfLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jlblSenha)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtLogin)
                    .addComponent(jbtCadastro))
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtLoginActionPerformed
        String email = jtxfLogin.getText();
        String senha = new String(jpfSenha.getPassword());

        try {
            if (authenticateUser(email, senha, em)) {
                Usuario usuario = getAuthenticatedUser(email, senha, em);
                if (usuario != null) {
                    this.dispose(); // Fecha a janela de login
                    Inicio inicio = new Inicio(usuario, em); // Passa o objeto Usuario e o EntityManager para a tela de início
                    inicio.setVisible(true);
                    inicio.setLocationRelativeTo(null); // Centraliza a janela
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao recuperar o usuário autenticado", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Credenciais inválidas", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao autenticar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jbtLoginActionPerformed

    private void jbtCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtCadastroActionPerformed
        CadastroUsuario cadastroUsuario = new CadastroUsuario();
        cadastroUsuario.setLocationRelativeTo(null);
        cadastroUsuario.setVisible(true);
    }//GEN-LAST:event_jbtCadastroActionPerformed

    //este trecho foi removido

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtCadastro;
    private javax.swing.JButton jbtLogin;
    private javax.swing.JLabel jlblLogin;
    private javax.swing.JLabel jlblSenha;
    private javax.swing.JLabel jlblTitulo;
    private javax.swing.JPasswordField jpfSenha;
    private javax.swing.JTextField jtxfLogin;
    // End of variables declaration//GEN-END:variables
}
