package dao;

import beans.Produto;
import conexao.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, preco, quantidade, categoria_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getCategoriaId());

            stmt.executeUpdate();
        }
    }

    public void editar(Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, preco = ?, quantidade = ?, categoria_id = ? WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getQuantidade());
            stmt.setInt(4, produto.getCategoriaId());
            stmt.setInt(5, produto.getId());

            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Produto> getAll() throws SQLException {
        List<Produto> produtos = new ArrayList<>();

        String sql = "SELECT p.id, p.nome, p.preco, p.quantidade, p.categoria_id, "
                   + "c.nome AS categoria_nome "
                   + "FROM produto p "
                   + "INNER JOIN categoria c ON c.id = p.categoria_id "
                   + "ORDER BY p.id";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco"));
                produto.setQuantidade(rs.getInt("quantidade"));
                produto.setCategoriaId(rs.getInt("categoria_id"));
                produto.setCategoriaNome(rs.getString("categoria_nome"));

                produtos.add(produto);
            }
        }

        return produtos;
    }

    public Produto getById(int id) throws SQLException {
        String sql = "SELECT p.id, p.nome, p.preco, p.quantidade, p.categoria_id, "
                   + "c.nome AS categoria_nome "
                   + "FROM produto p "
                   + "INNER JOIN categoria c ON c.id = p.categoria_id "
                   + "WHERE p.id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setPreco(rs.getDouble("preco"));
                    produto.setQuantidade(rs.getInt("quantidade"));
                    produto.setCategoriaId(rs.getInt("categoria_id"));
                    produto.setCategoriaNome(rs.getString("categoria_nome"));
                    return produto;
                }
            }
        }

        return null;
    }
}