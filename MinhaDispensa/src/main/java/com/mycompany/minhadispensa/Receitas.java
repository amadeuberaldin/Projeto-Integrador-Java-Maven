/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.minhadispensa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Hibernate;

/**
 *
 * @author Vaio
 */
public class Receitas extends javax.swing.JFrame {

    /**
     * Creates new form Receitas
     */
    public Receitas() {
        initComponents();
        updateRecipeTable();
        configurarListener();
    }

    private void updateRecipeTable() {
        DefaultTableModel model = (DefaultTableModel) jtblVisualizarNomesDasReceitas.getModel();
        model.setRowCount(0);  // Clear existing data
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Receita> recipes = em.createQuery("SELECT r FROM Receita r", Receita.class).getResultList();
            for (Receita receita : recipes) {
                model.addRow(new Object[]{receita.getNome()});
            }
        } finally {
            em.close();
        }
    }

    private void clearRecipeForm() {
        jtfNomeReceita.setText("");
        jtfIngrediente1.setText("");
        jtfQuantidade1.setText("");
        jtfIngrediente2.setText("");
        jtfQuantidade2.setText("");
        jtfIngrediente3.setText("");
        jtfQuantidade3.setText("");
        jtfIngrediente4.setText("");
        jtfQuantidade4.setText("");
        jtfIngrediente5.setText("");
        jtfQuantidade5.setText("");
        jtaCadarstrarModoDePreparo.setText("");
    }

    public Receita findRecipeByName(EntityManager em, String recipeName) {
        try {
            return em.createQuery("SELECT r FROM Receita r WHERE r.nome = :nome", Receita.class)
                    .setParameter("nome", recipeName)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private void displayRecipeDetails(Receita receita) {
        if (receita != null) {
            Hibernate.initialize(receita.getIngredientes());  // Assegura que os ingredientes são carregados

            jlblNomeReceitaMostrar.setText(receita.getNome());
            jtaMostrarModoDePreparo.setText(receita.getModoPreparo());

            JLabel[] ingredientLabels = {jlblIngrediente1Mostrar, jlblIngrediente2Mostrar, jlblIngrediente3Mostrar, jlblIngrediente4Mostrar, jlblIngrtediente5Mostrar};
            JLabel[] quantityLabels = {jlblQuantidadeIngrediente1Mostrar, jlblQuantidadeIngrediente2Mostrar, jlblQuantidadeIngrediente3Mostrar, jlblQuantidadeIngrediente4Mostrar, jlblQuantidadeIngrediente5Mostrar};

            int i = 0;
            for (Ingrediente ing : receita.getIngredientes()) {
                if (i < ingredientLabels.length) {
                    ingredientLabels[i].setText(ing.getProduto().getNome());
                    quantityLabels[i].setText(String.format("%.2f", ing.getQuantidade()));
                    i++;
                }
            }

            // Clear remaining labels if any
            for (; i < ingredientLabels.length; i++) {
                ingredientLabels[i].setText("");
                quantityLabels[i].setText("");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Receita não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
            clearDisplayFields();
        }
    }

    public void deleteRecipeByName(String recipeName) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Receita receita = em.createQuery("SELECT r FROM Receita r WHERE r.nome = :nome", Receita.class)
                    .setParameter("nome", recipeName.trim()) // Usando trim() para remover espaços extras
                    .getSingleResult(); // Pode lançar NoResultException se não encontrado
            em.remove(receita);
            em.getTransaction().commit();
            JOptionPane.showMessageDialog(null, "receita e seus ingredientes deletados com sucesso", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NoResultException ex) {
            JOptionPane.showMessageDialog(null, "receita não encontrada:" + recipeName, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(null, "Error deletando receita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close();
        }
    }

    private void clearDisplayFields() {
        jlblNomeReceitaMostrar.setText("");
        jtaMostrarModoDePreparo.setText("");
        JLabel[] ingredientLabels = {jlblIngrediente1Mostrar, jlblIngrediente2Mostrar, jlblIngrediente3Mostrar, jlblIngrediente4Mostrar, jlblIngrtediente5Mostrar};
        JLabel[] quantityLabels = {jlblQuantidadeIngrediente1Mostrar, jlblQuantidadeIngrediente2Mostrar, jlblQuantidadeIngrediente3Mostrar, jlblQuantidadeIngrediente4Mostrar, jlblQuantidadeIngrediente5Mostrar};

        for (JLabel label : ingredientLabels) {
            label.setText("");
        }
        for (JLabel label : quantityLabels) {
            label.setText("");
        }
    }

    private void configurarListener() {
        jtblVisualizarNomesDasReceitas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && jtblVisualizarNomesDasReceitas.getSelectedRow() != -1) {
                    EntityManager em = JPAUtil.getEntityManager();
                    try {
                        String recipeName = jtblVisualizarNomesDasReceitas.getValueAt(jtblVisualizarNomesDasReceitas.getSelectedRow(), 0).toString();
                        Receita receita = findRecipeByName(em, recipeName);
                        if (receita != null) {
                            displayRecipeDetails(receita);
                        } else {
                            JOptionPane.showMessageDialog(null, "Receita não encontrada: " + recipeName, "Erro", JOptionPane.ERROR_MESSAGE);
                            clearDisplayFields();
                        }
                    } finally {
                        em.close();
                    }
                }
            }
        });
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
        jlblNomeReceita = new javax.swing.JLabel();
        jlblIngrediente1 = new javax.swing.JLabel();
        jlblingrediente2 = new javax.swing.JLabel();
        jlblIngrediente3 = new javax.swing.JLabel();
        jlblIngrediente4 = new javax.swing.JLabel();
        jlblIngrtediente5 = new javax.swing.JLabel();
        jtfNomeReceita = new javax.swing.JTextField();
        jtfIngrediente1 = new javax.swing.JTextField();
        jtfIngrediente2 = new javax.swing.JTextField();
        jtfIngrediente3 = new javax.swing.JTextField();
        jtfIngrediente4 = new javax.swing.JTextField();
        jtfIngrediente5 = new javax.swing.JTextField();
        jlblQuantidade1 = new javax.swing.JLabel();
        jlblQuantidade2 = new javax.swing.JLabel();
        jlblQuantidade3 = new javax.swing.JLabel();
        jlblQuantidade4 = new javax.swing.JLabel();
        jlblQuantidade5 = new javax.swing.JLabel();
        jtfQuantidade1 = new javax.swing.JTextField();
        jtfQuantidade2 = new javax.swing.JTextField();
        jtfQuantidade3 = new javax.swing.JTextField();
        jtfQuantidade4 = new javax.swing.JTextField();
        jtfQuantidade5 = new javax.swing.JTextField();
        jlblModoPreparo = new javax.swing.JLabel();
        jspEntradaModoPreparo = new javax.swing.JScrollPane();
        jtaCadarstrarModoDePreparo = new javax.swing.JTextArea();
        jbtConcluir = new javax.swing.JButton();
        jbtVoltar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblVisualizarNomesDasReceitas = new javax.swing.JTable();
        jlblIngrediente1Mostrar = new javax.swing.JLabel();
        jlblIngrediente2Mostrar = new javax.swing.JLabel();
        jlblNomeReceitaMostrar = new javax.swing.JLabel();
        jlblIngrediente3Mostrar = new javax.swing.JLabel();
        jlblIngrediente4Mostrar = new javax.swing.JLabel();
        jlblIngrtediente5Mostrar = new javax.swing.JLabel();
        jlblQuantidadeIngrediente1Mostrar = new javax.swing.JLabel();
        jlblQuantidadeIngrediente2Mostrar = new javax.swing.JLabel();
        jlblQuantidadeIngrediente3Mostrar = new javax.swing.JLabel();
        jlblQuantidadeIngrediente4Mostrar = new javax.swing.JLabel();
        jlblQuantidadeIngrediente5Mostrar = new javax.swing.JLabel();
        jlblModoPreparoMostrar = new javax.swing.JLabel();
        jspMostrarModoPreparo = new javax.swing.JScrollPane();
        jtaMostrarModoDePreparo = new javax.swing.JTextArea();
        jbtExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jlblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jlblTitulo.setText("Crie sua Receita");

        jlblNomeReceita.setText("Nome da Receita:");

        jlblIngrediente1.setText("Ingrediente 1:");

        jlblingrediente2.setText("Ingrediente 2:");

        jlblIngrediente3.setText("Ingrediente 3:");

        jlblIngrediente4.setText("Ingrediente 4:");

        jlblIngrtediente5.setText("Ingrediente 5:");

        jlblQuantidade1.setText("Quantidade:");

        jlblQuantidade2.setText("Quantidade:");

        jlblQuantidade3.setText("Quantidade:");

        jlblQuantidade4.setText("Quantidade:");

        jlblQuantidade5.setText("Quantidade:");

        jlblModoPreparo.setText("Modo de preparo:");

        jtaCadarstrarModoDePreparo.setColumns(20);
        jtaCadarstrarModoDePreparo.setRows(5);
        jspEntradaModoPreparo.setViewportView(jtaCadarstrarModoDePreparo);

        jbtConcluir.setText("Concluir");
        jbtConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtConcluirActionPerformed(evt);
            }
        });

        jbtVoltar.setText("Voltar");
        jbtVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtVoltarActionPerformed(evt);
            }
        });

        jtblVisualizarNomesDasReceitas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Receitas"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtblVisualizarNomesDasReceitas.setColumnSelectionAllowed(true);
        jScrollPane2.setViewportView(jtblVisualizarNomesDasReceitas);
        jtblVisualizarNomesDasReceitas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jlblModoPreparoMostrar.setText("Modo de preparo:");

        jtaMostrarModoDePreparo.setColumns(20);
        jtaMostrarModoDePreparo.setRows(5);
        jspMostrarModoPreparo.setViewportView(jtaMostrarModoDePreparo);

        jbtExcluir.setText("Excluir");
        jbtExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtExcluirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jbtConcluir)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jlblTitulo)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jlblIngrtediente5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblIngrediente4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblIngrediente3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblingrediente2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblIngrediente1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblNomeReceita, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jtfIngrediente1)
                                        .addComponent(jtfIngrediente2)
                                        .addComponent(jtfIngrediente3)
                                        .addComponent(jtfIngrediente4)
                                        .addComponent(jtfIngrediente5, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                        .addComponent(jtfNomeReceita))))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jlblQuantidade1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblQuantidade2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblQuantidade3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblQuantidade4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblQuantidade5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jtfQuantidade1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtfQuantidade2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtfQuantidade3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtfQuantidade4, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jtfQuantidade5, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jlblModoPreparo)
                            .addGap(18, 18, 18)
                            .addComponent(jspEntradaModoPreparo, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(47, 47, 47)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jlblIngrediente2Mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jlblQuantidadeIngrediente2Mostrar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jlblIngrediente1Mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jlblQuantidadeIngrediente1Mostrar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jlblIngrediente3Mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jlblQuantidadeIngrediente3Mostrar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jlblIngrediente4Mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jlblQuantidadeIngrediente4Mostrar))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jlblIngrtediente5Mostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jlblQuantidadeIngrediente5Mostrar))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addComponent(jlblModoPreparoMostrar))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(27, 27, 27)
                            .addComponent(jspMostrarModoPreparo, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtExcluir)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jbtVoltar)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jlblNomeReceitaMostrar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlblTitulo)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblNomeReceita)
                            .addComponent(jtfNomeReceita, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblIngrediente1)
                            .addComponent(jtfIngrediente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblQuantidade1)
                            .addComponent(jtfQuantidade1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblingrediente2)
                            .addComponent(jtfIngrediente2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblQuantidade2)
                            .addComponent(jtfQuantidade2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblIngrediente3)
                            .addComponent(jtfIngrediente3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblQuantidade3)
                            .addComponent(jtfQuantidade3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblIngrediente4)
                            .addComponent(jtfIngrediente4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblQuantidade4)
                            .addComponent(jtfQuantidade4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblIngrtediente5)
                            .addComponent(jtfIngrediente5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblQuantidade5)
                            .addComponent(jtfQuantidade5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jlblModoPreparo)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jspEntradaModoPreparo, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
                        .addGap(46, 46, 46))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jlblNomeReceitaMostrar)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblQuantidadeIngrediente1Mostrar)
                                    .addComponent(jlblIngrediente1Mostrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblQuantidadeIngrediente2Mostrar)
                                    .addComponent(jlblIngrediente2Mostrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblQuantidadeIngrediente3Mostrar)
                                    .addComponent(jlblIngrediente3Mostrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblQuantidadeIngrediente4Mostrar)
                                    .addComponent(jlblIngrediente4Mostrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblIngrtediente5Mostrar)
                                    .addComponent(jlblQuantidadeIngrediente5Mostrar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblModoPreparoMostrar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jspMostrarModoPreparo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jbtVoltar)
                                    .addComponent(jbtConcluir)
                                    .addComponent(jbtExcluir))))
                        .addGap(11, 11, 11))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtVoltarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbtVoltarActionPerformed

    private void jbtConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtConcluirActionPerformed
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Creating the recipe instance
            Receita receita = new Receita();
            receita.setNome(jtfNomeReceita.getText());
            receita.setModoPreparo(jtaCadarstrarModoDePreparo.getText());

            em.persist(receita); // Persist the recipe first to generate its ID

            // Handle ingredients, assuming you have fields and quantity fields matched by index
            JTextField[] ingredientFields = {jtfIngrediente1, jtfIngrediente2, jtfIngrediente3, jtfIngrediente4, jtfIngrediente5};
            JTextField[] quantityFields = {jtfQuantidade1, jtfQuantidade2, jtfQuantidade3, jtfQuantidade4, jtfQuantidade5};

            for (int i = 0; i < ingredientFields.length; i++) {
                if (!ingredientFields[i].getText().isEmpty()) {
                    Produto produto = em.createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class)
                            .setParameter("nome", ingredientFields[i].getText())
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(null); // Find product by name

                    if (produto != null) {
                        Ingrediente ingrediente = new Ingrediente(receita, produto, Float.parseFloat(quantityFields[i].getText()));
                        em.persist(ingrediente);
                    }
                }
            }

            em.getTransaction().commit();
            updateRecipeTable(); // Update the recipe list on UI
            clearRecipeForm(); // Clear the form fields
            clearDisplayFields(); // Clear the display fields

        } catch (Exception ex) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(this, "Error salvando Receita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            em.close();
        }
    }//GEN-LAST:event_jbtConcluirActionPerformed

    private void jbtExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExcluirActionPerformed
        // Confirma se o usuário realmente quer excluir a receita
        int response = JOptionPane.showConfirmDialog(this, "Você tem certeza que quer deletar esta receita?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            // Pega o nome da receita do label onde é exibido
            String recipeName = jlblNomeReceitaMostrar.getText();

            // Chama o método para excluir a receita pelo nome
            deleteRecipeByName(recipeName);

            // Atualiza a tabela para refletir a mudança
            updateRecipeTable();

            // Limpa os campos de detalhes após a exclusão
            clearDisplayFields();
        }
    }//GEN-LAST:event_jbtExcluirActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Receitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Receitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Receitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Receitas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Receitas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbtConcluir;
    private javax.swing.JButton jbtExcluir;
    private javax.swing.JButton jbtVoltar;
    private javax.swing.JLabel jlblIngrediente1;
    private javax.swing.JLabel jlblIngrediente1Mostrar;
    private javax.swing.JLabel jlblIngrediente2Mostrar;
    private javax.swing.JLabel jlblIngrediente3;
    private javax.swing.JLabel jlblIngrediente3Mostrar;
    private javax.swing.JLabel jlblIngrediente4;
    private javax.swing.JLabel jlblIngrediente4Mostrar;
    private javax.swing.JLabel jlblIngrtediente5;
    private javax.swing.JLabel jlblIngrtediente5Mostrar;
    private javax.swing.JLabel jlblModoPreparo;
    private javax.swing.JLabel jlblModoPreparoMostrar;
    private javax.swing.JLabel jlblNomeReceita;
    private javax.swing.JLabel jlblNomeReceitaMostrar;
    private javax.swing.JLabel jlblQuantidade1;
    private javax.swing.JLabel jlblQuantidade2;
    private javax.swing.JLabel jlblQuantidade3;
    private javax.swing.JLabel jlblQuantidade4;
    private javax.swing.JLabel jlblQuantidade5;
    private javax.swing.JLabel jlblQuantidadeIngrediente1Mostrar;
    private javax.swing.JLabel jlblQuantidadeIngrediente2Mostrar;
    private javax.swing.JLabel jlblQuantidadeIngrediente3Mostrar;
    private javax.swing.JLabel jlblQuantidadeIngrediente4Mostrar;
    private javax.swing.JLabel jlblQuantidadeIngrediente5Mostrar;
    private javax.swing.JLabel jlblTitulo;
    private javax.swing.JLabel jlblingrediente2;
    private javax.swing.JScrollPane jspEntradaModoPreparo;
    private javax.swing.JScrollPane jspMostrarModoPreparo;
    private javax.swing.JTextArea jtaCadarstrarModoDePreparo;
    private javax.swing.JTextArea jtaMostrarModoDePreparo;
    private javax.swing.JTable jtblVisualizarNomesDasReceitas;
    private javax.swing.JTextField jtfIngrediente1;
    private javax.swing.JTextField jtfIngrediente2;
    private javax.swing.JTextField jtfIngrediente3;
    private javax.swing.JTextField jtfIngrediente4;
    private javax.swing.JTextField jtfIngrediente5;
    private javax.swing.JTextField jtfNomeReceita;
    private javax.swing.JTextField jtfQuantidade1;
    private javax.swing.JTextField jtfQuantidade2;
    private javax.swing.JTextField jtfQuantidade3;
    private javax.swing.JTextField jtfQuantidade4;
    private javax.swing.JTextField jtfQuantidade5;
    // End of variables declaration//GEN-END:variables
}
