package forms;

import beans.Categoria;
import dao.CategoriaDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class CategoriaForm extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtDescricao;
    private JTable tabela;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public CategoriaForm() {
        setTitle("Cadastro de Categorias");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        montarTela();
        carregarTabela();
    }

    private void montarTela() {
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 5, 5));

        txtId = new JTextField();
        txtNome = new JTextField();
        txtDescricao = new JTextField();

        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(txtDescricao);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnPesquisar = new JButton("Pesquisar por ID");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnProdutos = new JButton("Abrir Produtos");

        JPanel painelBotoes = new JPanel();

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnPesquisar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnProdutos);

        tabela = new JTable();

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCampos, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvar());
        btnPesquisar.addActionListener(e -> pesquisarPorId());
        btnAtualizar.addActionListener(e -> atualizar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());
        btnProdutos.addActionListener(e -> abrirProdutos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposPelaTabela();
            }
        });
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome da categoria.");
                return;
            }

            Categoria categoria = new Categoria(nome, descricao);
            categoriaDAO.inserir(categoria);

            JOptionPane.showMessageDialog(this, "Categoria salva com sucesso.");

            limparCampos();
            carregarTabela();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void pesquisarPorId() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            Categoria categoria = categoriaDAO.getById(id);

            if (categoria == null) {
                JOptionPane.showMessageDialog(this, "Categoria não encontrada.");
                return;
            }

            txtNome.setText(categoria.getNome());
            txtDescricao.setText(categoria.getDescricao());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ID válido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
        }
    }

    private void atualizar() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nome = txtNome.getText().trim();
            String descricao = txtDescricao.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome da categoria.");
                return;
            }

            Categoria categoria = new Categoria(id, nome, descricao);
            categoriaDAO.editar(categoria);

            JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso.");

            limparCampos();
            carregarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ID válido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
        }
    }

    private void excluir() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir esta categoria?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                categoriaDAO.excluir(id);

                JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso.");

                limparCampos();
                carregarTabela();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Informe um ID válido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir. Verifique se existem produtos vinculados a esta categoria.\n" + ex.getMessage()
            );
        }
    }

    private void carregarTabela() {
        try {
            List<Categoria> categorias = categoriaDAO.getAll();

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nome");
            modelo.addColumn("Descrição");

            for (Categoria categoria : categorias) {
                modelo.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNome(),
                    categoria.getDescricao()
                });
            }

            tabela.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tabela: " + ex.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linha = tabela.getSelectedRow();

        if (linha >= 0) {
            txtId.setText(tabela.getValueAt(linha, 0).toString());
            txtNome.setText(tabela.getValueAt(linha, 1).toString());
            txtDescricao.setText(tabela.getValueAt(linha, 2).toString());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtDescricao.setText("");
        tabela.clearSelection();
    }
    
    private void abrirProdutos() {
    try {
        if (txtId.getText().trim().isEmpty()) {
            ProdutoForm produtoForm = new ProdutoForm();
            produtoForm.setVisible(true);
            return;
        }

        int categoriaId = Integer.parseInt(txtId.getText().trim());

        ProdutoForm produtoForm = new ProdutoForm(categoriaId);
        produtoForm.setVisible(true);

        } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "ID da categoria inválido.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CategoriaForm().setVisible(true);
        });
    }
}