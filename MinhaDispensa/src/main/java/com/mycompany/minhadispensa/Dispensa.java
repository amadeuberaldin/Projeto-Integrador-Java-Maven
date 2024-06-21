package com.mycompany.minhadispensa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Dispensa extends javax.swing.JFrame {

    private Usuario usuario;
    private EntityManager em;

    public Dispensa(Usuario usuario, EntityManager em) {
        this.usuario = usuario;
        this.em = em;
        initComponents();
        carregarProdutos();
        configurarListeners();
    }

    private void carregarProdutos() {
        List<Despensa> produtos = buscarProdutos(usuario); // Método para buscar produtos do banco de dados
        DefaultTableModel model = (DefaultTableModel) jtprodutos.getModel();
        model.setRowCount(0);
        for (Despensa despensa : produtos) {
            model.addRow(new Object[]{despensa.getProduto().getNome(), despensa.getQuantidade()});
        }
    }

    private List<Despensa> buscarProdutos(Usuario usuario) {
        try {
            return em.createQuery("SELECT d FROM Despensa d WHERE d.usuario = :usuario", Despensa.class)
                    .setParameter("usuario", usuario)
                    .getResultList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar produtos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void adicionarOuAtualizarProduto() {
        String nome = jtfNomeProduto.getText();
        float quantidade = Float.parseFloat(jtfQuantidadeProduto.getText());
        try {
            Produto produto = em.createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class)
                    .setParameter("nome", nome)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
            em.getTransaction().begin();
            if (produto == null) {
                produto = new Produto();
                produto.setNome(nome);
                produto.setQuantidade(quantidade);
                if (jrbProdutoNovo.isSelected()) {
                    produto.setPorcaoReferencia(Float.parseFloat(jtfPorcaoReferencia.getText()));
                    produto.setProteinas(Float.parseFloat(jtfProteinas.getText()));
                    produto.setCarboidratos(Float.parseFloat(jtfCarboidratos.getText()));
                    produto.setCalorias(Float.parseFloat(jtfCalorias.getText()));
                    produto.setGordurasTotais(Float.parseFloat(jtfGordurasTotais.getText()));
                }
                em.persist(produto);
            } else {
                produto.setQuantidade(produto.getQuantidade() + quantidade);
                em.merge(produto);
            }
            DespensaId despensaId = new DespensaId(usuario.getId(), produto.getId());
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
            carregarProdutos();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            JOptionPane.showMessageDialog(this, "Erro ao salvar o produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } finally {
            limparCamposCadastro();
        }
    }

    private void limparCamposCadastro() {
        jtfNomeProduto.setText("");
        jtfQuantidadeProduto.setText("");
        jtfPorcaoReferencia.setText("");
        jtfProteinas.setText("");
        jtfCarboidratos.setText("");
        jtfCalorias.setText("");
        jtfGordurasTotais.setText("");
        buttonGroup1.clearSelection();
    }

    private void atualizarEstadoCampos() {
        boolean ehProdutoNovo = jrbProdutoNovo.isSelected();
        jtfPorcaoReferencia.setEnabled(ehProdutoNovo);
        jtfProteinas.setEnabled(ehProdutoNovo);
        jtfCarboidratos.setEnabled(ehProdutoNovo);
        jtfCalorias.setEnabled(ehProdutoNovo);
        jtfGordurasTotais.setEnabled(ehProdutoNovo);
    }

    private void limparCampos() {
        jtfNomeProduto.setText("");
        jtfQuantidadeProduto.setText("");
        jtfPorcaoReferencia.setText("");
        jtfProteinas.setText("");
        jtfCarboidratos.setText("");
        jtfCalorias.setText("");
        jtfGordurasTotais.setText("");
    }

    private void configurarListeners() {
        jtprodutos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    atualizarCamposComDadosDoProdutoSelecionado();
                    atualizarEstadoCampos();
                }
            }
        });

        jrbProdutoNovo.addActionListener(e -> atualizarEstadoCampos());
        jrbProdutoJaCadastrado.addActionListener(e -> atualizarEstadoCampos());
    }

    private void atualizarCamposComDadosDoProdutoSelecionado() {
        int selectedRow = jtprodutos.getSelectedRow();
        if (selectedRow >= 0) {
            String nomeProduto = (String) jtprodutos.getValueAt(selectedRow, 0);
            try {
                Produto produto = em.createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class)
                        .setParameter("nome", nomeProduto)
                        .getSingleResult();

                if (produto != null) {
                    jlblNomeDoProdutoMostrar.setText(produto.getNome());
                    jlblQuantidadeDoProdutoMostrar.setText(String.valueOf(produto.getQuantidade()));
                    jlblMostraPorcaodeReferencia.setText(formatarFloat(produto.getPorcaoReferencia()));
                    jlblMostraProteinas.setText(formatarFloat(produto.getProteinas()));
                    jlblMostraCarboidratos.setText(formatarFloat(produto.getCarboidratos()));
                    jlblMostraCalorias.setText(formatarFloat(produto.getCalorias()));
                    jlblMostraGordurasTotais.setText(formatarFloat(produto.getGordurasTotais()));
                }
            } catch (NoResultException nre) {
                JOptionPane.showMessageDialog(this, "Produto não encontrado", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String formatarFloat(Float valor) {
        return valor == null ? "" : String.format("%.2f", valor);
    }

    private void excluirProduto() {
        int row = jtprodutos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
            return;
        }
        String nomeProduto = (String) jtprodutos.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Deseja realmente excluir o produto " + nomeProduto + "?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Produto produto = em.createQuery("SELECT p FROM Produto p WHERE p.nome = :nome", Produto.class)
                        .setParameter("nome", nomeProduto)
                        .getSingleResult();
                em.getTransaction().begin();
                em.remove(produto);
                em.getTransaction().commit();
                carregarProdutos();
            } catch (Exception ex) {
                em.getTransaction().rollback();
                JOptionPane.showMessageDialog(this, "Erro ao excluir o produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } finally {
                limparCamposMostrar();
                carregarProdutos();
            }
        }
    }

    private void limparCamposMostrar() {
        jlblNomeDoProdutoMostrar.setText("");
        jlblQuantidadeDoProdutoMostrar.setText("");
        jlblMostraPorcaodeReferencia.setText("");
        jlblMostraProteinas.setText("");
        jlblMostraCarboidratos.setText("");
        jlblMostraCalorias.setText("");
        jlblMostraGordurasTotais.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jlblNomeProduto = new javax.swing.JLabel();
        jlblQuantidadeProduto = new javax.swing.JLabel();
        jtfNomeProduto = new javax.swing.JTextField();
        jtfQuantidadeProduto = new javax.swing.JTextField();
        jbtVoltar = new javax.swing.JButton();
        jlbltitulo = new javax.swing.JLabel();
        jbtConcluir = new javax.swing.JButton();
        jrbProdutoNovo = new javax.swing.JRadioButton();
        jrbProdutoJaCadastrado = new javax.swing.JRadioButton();
        jlblPorcaoReferencia = new javax.swing.JLabel();
        jtfPorcaoReferencia = new javax.swing.JTextField();
        jlblProteinas = new javax.swing.JLabel();
        jtfProteinas = new javax.swing.JTextField();
        jlblCarboidratos = new javax.swing.JLabel();
        jtfCarboidratos = new javax.swing.JTextField();
        jlblCalorias = new javax.swing.JLabel();
        jtfCalorias = new javax.swing.JTextField();
        jlblGordurasTotais = new javax.swing.JLabel();
        jtfGordurasTotais = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtprodutos = new javax.swing.JTable();
        jlblNomeDoProdutoMostrar = new javax.swing.JLabel();
        jlblQuantidadeDoProdutoMostrar = new javax.swing.JLabel();
        jlblPorcaoDeReferenciaMostrar = new javax.swing.JLabel();
        jlblProteinasMostrar = new javax.swing.JLabel();
        jlblCarboidratosMostrar = new javax.swing.JLabel();
        jlblCaloriasMostrar = new javax.swing.JLabel();
        jlblGordurasTotaisMostrar = new javax.swing.JLabel();
        jlblMostraPorcaodeReferencia = new javax.swing.JLabel();
        jlblMostraProteinas = new javax.swing.JLabel();
        jlblMostraCarboidratos = new javax.swing.JLabel();
        jlblMostraCalorias = new javax.swing.JLabel();
        jlblMostraGordurasTotais = new javax.swing.JLabel();
        jbtExcluir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jlblNomeProduto.setText("Nome do Produto:");

        jlblQuantidadeProduto.setText("Quantidade:");

        jbtVoltar.setText("Voltar");
        jbtVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtVoltarActionPerformed(evt);
            }
        });

        jlbltitulo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jlbltitulo.setText("Cadastro de Produtos");

        jbtConcluir.setText("Concluir");
        jbtConcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtConcluirActionPerformed(evt);
            }
        });

        buttonGroup1.add(jrbProdutoNovo);
        jrbProdutoNovo.setText("Produto Novo");

        buttonGroup1.add(jrbProdutoJaCadastrado);
        jrbProdutoJaCadastrado.setText("Adicionar Quantidade");

        jlblPorcaoReferencia.setText("Porção Referencia:");

        jlblProteinas.setText("Proteinas:");

        jlblCarboidratos.setText("Carboidratos:");

        jlblCalorias.setText("Calorias:");

        jlblGordurasTotais.setText("Gorduras Totais:");

        jtprodutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Produtos"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jtprodutos.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(jtprodutos);
        jtprodutos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        jlblNomeDoProdutoMostrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jlblPorcaoDeReferenciaMostrar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblPorcaoDeReferenciaMostrar.setText("Porção de Referencia:");

        jlblProteinasMostrar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblProteinasMostrar.setText("Proteinas:");

        jlblCarboidratosMostrar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblCarboidratosMostrar.setText("Carboidratos:");

        jlblCaloriasMostrar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblCaloriasMostrar.setText("Calorias:");

        jlblGordurasTotaisMostrar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jlblGordurasTotaisMostrar.setText("Gorduras Totais:");

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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jlbltitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jrbProdutoNovo, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jrbProdutoJaCadastrado, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jlblNomeProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jlblQuantidadeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlblPorcaoReferencia)
                                        .addComponent(jlblProteinas, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlblCarboidratos, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jlblCalorias, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jlblGordurasTotais))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jbtConcluir, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                    .addComponent(jtfNomeProduto)
                                    .addComponent(jtfQuantidadeProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                    .addComponent(jtfPorcaoReferencia)
                                    .addComponent(jtfProteinas)
                                    .addComponent(jtfCarboidratos)
                                    .addComponent(jtfCalorias)
                                    .addComponent(jtfGordurasTotais))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlblNomeDoProdutoMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jlblQuantidadeDoProdutoMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jbtVoltar)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jlblPorcaoDeReferenciaMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblProteinasMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblCarboidratosMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblCaloriasMostrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblGordurasTotaisMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jlblMostraPorcaodeReferencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblMostraProteinas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblMostraCarboidratos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblMostraCalorias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jlblMostraGordurasTotais, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtExcluir))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jlbltitulo)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jrbProdutoNovo)
                                    .addComponent(jrbProdutoJaCadastrado))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblNomeProduto)
                                    .addComponent(jtfNomeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblQuantidadeProduto)
                                    .addComponent(jtfQuantidadeProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jtfPorcaoReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jlblPorcaoReferencia))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblProteinas)
                                    .addComponent(jtfProteinas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblCarboidratos)
                                    .addComponent(jtfCarboidratos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblCalorias)
                                    .addComponent(jtfCalorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jlblGordurasTotais)
                                    .addComponent(jtfGordurasTotais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jbtConcluir)
                            .addComponent(jbtVoltar)
                            .addComponent(jbtExcluir)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblNomeDoProdutoMostrar)
                            .addComponent(jlblQuantidadeDoProdutoMostrar))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblPorcaoDeReferenciaMostrar)
                            .addComponent(jlblMostraPorcaodeReferencia))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblProteinasMostrar)
                            .addComponent(jlblMostraProteinas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblCarboidratosMostrar)
                            .addComponent(jlblMostraCarboidratos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblCaloriasMostrar)
                            .addComponent(jlblMostraCalorias))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jlblGordurasTotaisMostrar)
                            .addComponent(jlblMostraGordurasTotais))))
                .addContainerGap(18, Short.MAX_VALUE))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtVoltarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jbtVoltarActionPerformed

    private void jbtConcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtConcluirActionPerformed
        adicionarOuAtualizarProduto();
    }//GEN-LAST:event_jbtConcluirActionPerformed

    private void jbtExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtExcluirActionPerformed
        excluirProduto();
    }//GEN-LAST:event_jbtExcluirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtConcluir;
    private javax.swing.JButton jbtExcluir;
    private javax.swing.JButton jbtVoltar;
    private javax.swing.JLabel jlblCalorias;
    private javax.swing.JLabel jlblCaloriasMostrar;
    private javax.swing.JLabel jlblCarboidratos;
    private javax.swing.JLabel jlblCarboidratosMostrar;
    private javax.swing.JLabel jlblGordurasTotais;
    private javax.swing.JLabel jlblGordurasTotaisMostrar;
    private javax.swing.JLabel jlblMostraCalorias;
    private javax.swing.JLabel jlblMostraCarboidratos;
    private javax.swing.JLabel jlblMostraGordurasTotais;
    private javax.swing.JLabel jlblMostraPorcaodeReferencia;
    private javax.swing.JLabel jlblMostraProteinas;
    private javax.swing.JLabel jlblNomeDoProdutoMostrar;
    private javax.swing.JLabel jlblNomeProduto;
    private javax.swing.JLabel jlblPorcaoDeReferenciaMostrar;
    private javax.swing.JLabel jlblPorcaoReferencia;
    private javax.swing.JLabel jlblProteinas;
    private javax.swing.JLabel jlblProteinasMostrar;
    private javax.swing.JLabel jlblQuantidadeDoProdutoMostrar;
    private javax.swing.JLabel jlblQuantidadeProduto;
    private javax.swing.JLabel jlbltitulo;
    private javax.swing.JRadioButton jrbProdutoJaCadastrado;
    private javax.swing.JRadioButton jrbProdutoNovo;
    private javax.swing.JTextField jtfCalorias;
    private javax.swing.JTextField jtfCarboidratos;
    private javax.swing.JTextField jtfGordurasTotais;
    private javax.swing.JTextField jtfNomeProduto;
    private javax.swing.JTextField jtfPorcaoReferencia;
    private javax.swing.JTextField jtfProteinas;
    private javax.swing.JTextField jtfQuantidadeProduto;
    private javax.swing.JTable jtprodutos;
    // End of variables declaration//GEN-END:variables
}
