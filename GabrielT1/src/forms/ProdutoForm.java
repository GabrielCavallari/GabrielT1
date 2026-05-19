package forms;

import beans.Categoria;
import beans.Produto;
import dao.CategoriaDAO;
import dao.ProdutoDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ProdutoForm extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtQuantidade;
    private JComboBox<Categoria> cbCategoria;
    private JTable tabela;

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private Integer categoriaIdInicial;

    public ProdutoForm() {
        this(null);
    }

    public ProdutoForm(Integer categoriaIdInicial) {
        this.categoriaIdInicial = categoriaIdInicial;

        setTitle("Cadastro de Produtos");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        montarTela();
        carregarCategorias();

        if (categoriaIdInicial != null) {
            selecionarCategoriaPorId(categoriaIdInicial);
        }

        carregarTabela();
    }

    private void montarTela() {
        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 5, 5));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNome = new JTextField();
        txtPreco = new JTextField();
        txtQuantidade = new JTextField();
        cbCategoria = new JComboBox<>();

        painelCampos.add(new JLabel("ID:"));
        painelCampos.add(txtId);

        painelCampos.add(new JLabel("Nome:"));
        painelCampos.add(txtNome);

        painelCampos.add(new JLabel("Preço:"));
        painelCampos.add(txtPreco);

        painelCampos.add(new JLabel("Quantidade:"));
        painelCampos.add(txtQuantidade);

        painelCampos.add(new JLabel("Categoria:"));
        painelCampos.add(cbCategoria);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnAtualizarProduto = new JButton("Atualizar Produto");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAtualizarProduto);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);

        tabela = new JTable();

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCampos, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvar());
        btnAtualizarProduto.addActionListener(e -> atualizarProduto());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limparCampos());

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposPelaTabela();
            }
        });
    }

    private void salvar() {
        try {
            String nome = txtNome.getText().trim();
            String precoTexto = txtPreco.getText().trim().replace(",", ".");
            String quantidadeTexto = txtQuantidade.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
                return;
            }

            if (precoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe o preço do produto.");
                return;
            }

            if (quantidadeTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Informe a quantidade do produto.");
                return;
            }

            if (cbCategoria.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Cadastre uma categoria antes de cadastrar produtos.");
                return;
            }

            double preco = Double.parseDouble(precoTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);

            Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();

            Produto produto = new Produto(
                    nome,
                    preco,
                    quantidade,
                    categoriaSelecionada.getId()
            );

            produtoDAO.inserir(produto);

            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso.");

            limparCampos();
            carregarTabela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar produto: " + ex.getMessage());
        }
    }

    private void atualizarProduto() {
    try {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para atualizar.");
            return;
        }

        int id = Integer.parseInt(txtId.getText().trim());

        String nome = txtNome.getText().trim();
        String precoTexto = txtPreco.getText().trim().replace(",", ".");
        String quantidadeTexto = txtQuantidade.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
            return;
        }

        if (precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o preço do produto.");
            return;
        }

        if (quantidadeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade do produto.");
            return;
        }

        if (cbCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria.");
            return;
        }

        double preco = Double.parseDouble(precoTexto);
        int quantidade = Integer.parseInt(quantidadeTexto);

        Categoria categoriaSelecionada = (Categoria) cbCategoria.getSelectedItem();

        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setQuantidade(quantidade);
        produto.setCategoriaId(categoriaSelecionada.getId());

        produtoDAO.editar(produto);

        JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso.");

        limparCampos();
        carregarTabela();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Preço, quantidade ou ID inválido.");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Erro ao atualizar produto: " + ex.getMessage());
    }
    }
    
    private void excluir() {
        try {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela para excluir.");
                return;
            }

            int id = Integer.parseInt(txtId.getText().trim());

            int resposta = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente excluir este produto?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION
            );

            if (resposta == JOptionPane.YES_OPTION) {
                produtoDAO.excluir(id);

                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso.");

                limparCampos();
                carregarTabela();
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID do produto inválido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + ex.getMessage());
        }
    }

    private void carregarCategorias() {
        try {
            cbCategoria.removeAllItems();

            List<Categoria> categorias = categoriaDAO.getAll();

            for (Categoria categoria : categorias) {
                cbCategoria.addItem(categoria);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        try {
            List<Produto> produtos = produtoDAO.getAll();

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nome");
            modelo.addColumn("Preço");
            modelo.addColumn("Quantidade");
            modelo.addColumn("Categoria ID");
            modelo.addColumn("Categoria");

            for (Produto produto : produtos) {
                modelo.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.getCategoriaId(),
                    produto.getCategoriaNome()
                });
            }

            tabela.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linha = tabela.getSelectedRow();

        if (linha >= 0) {
            txtId.setText(tabela.getValueAt(linha, 0).toString());
            txtNome.setText(tabela.getValueAt(linha, 1).toString());
            txtPreco.setText(tabela.getValueAt(linha, 2).toString());
            txtQuantidade.setText(tabela.getValueAt(linha, 3).toString());

            int categoriaId = Integer.parseInt(tabela.getValueAt(linha, 4).toString());
            selecionarCategoriaPorId(categoriaId);
        }
    }

    private void selecionarCategoriaPorId(int categoriaId) {
        for (int i = 0; i < cbCategoria.getItemCount(); i++) {
            Categoria categoria = cbCategoria.getItemAt(i);

            if (categoria.getId() == categoriaId) {
                cbCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");

        if (categoriaIdInicial != null) {
            selecionarCategoriaPorId(categoriaIdInicial);
        } else if (cbCategoria.getItemCount() > 0) {
            cbCategoria.setSelectedIndex(0);
        }

        tabela.clearSelection();
    }
}