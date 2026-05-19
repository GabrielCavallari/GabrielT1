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

    public ProdutoForm() {
        setTitle("Cadastro de Produtos");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        montarTela();
        carregarCategorias();
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
        JButton btnLimpar = new JButton("Limpar");
        JButton btnAtualizarCategorias = new JButton("Atualizar Categorias");

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnAtualizarCategorias);

        tabela = new JTable();

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCampos, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limparCampos());
        btnAtualizarCategorias.addActionListener(e -> carregarCategorias());
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
            modelo.addColumn("Categoria");

            for (Produto produto : produtos) {
                modelo.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getQuantidade(),
                    produto.getCategoriaNome()
                });
            }

            tabela.setModel(modelo);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");

        if (cbCategoria.getItemCount() > 0) {
            cbCategoria.setSelectedIndex(0);
        }
    }
}