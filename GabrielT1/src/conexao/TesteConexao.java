package conexao;

import java.sql.Connection;
import java.sql.SQLException;

public class TesteConexao {

    public static void main(String[] args) {
        try {
            Connection conn = Conexao.getConnection();
            System.out.println("Conexão realizada com sucesso!");
            conn.close();
        } catch (SQLException erro) {
            System.out.println("Erro ao conectar:");
            System.out.println(erro.getMessage());
        }
    }
}