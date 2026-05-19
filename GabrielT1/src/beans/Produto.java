package beans;

public class Produto {

    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private int categoriaId;
    private String categoriaNome;

    public Produto() {
    }

    public Produto(int id, String nome, double preco, int quantidade, int categoriaId, String categoriaNome) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
    }

    public Produto(String nome, double preco, int quantidade, int categoriaId) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }


    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }


    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }
}