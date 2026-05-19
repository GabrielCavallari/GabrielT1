````markdown
# GabrielT1

Projeto desenvolvido para o Trabalho T1 da faculdade.

## Descrição

Este projeto é um sistema desktop desenvolvido em Java para cadastro de categorias e produtos.

A aplicação utiliza interface gráfica com Java Swing, conexão com banco de dados MySQL por meio de JDBC e organização em camadas, separando as classes em pacotes específicos conforme solicitado no enunciado do trabalho.

O sistema possui duas tabelas principais:

- `categoria`
- `produto`

A relação entre as tabelas é de **1 para N**, pois uma categoria pode possuir vários produtos.

---

## Relação 1-N

A tabela `categoria` fornece a relação.

A tabela `produto` recebe a relação por meio da chave estrangeira `categoria_id`.

Estrutura da relação:

```text
categoria.id 1 ---- N produto.categoria_id
```

Exemplo:

```text
Categoria: Eletrônicos
Produtos: Mouse, Teclado, Monitor, Notebook
```

Ou seja, uma única categoria pode estar vinculada a vários produtos.

---

## Tecnologias utilizadas

- Java
- Java Swing
- JFrame
- JTable
- JComboBox
- JDBC
- MySQL
- MySQL Workbench
- NetBeans IDE
- Projeto Java with Ant

---

## Estrutura do projeto

```text
GabrielT1
├── src
│   ├── beans
│   │   ├── Categoria.java
│   │   └── Produto.java
│   ├── conexao
│   │   ├── Conexao.java
│   │   └── TesteConexao.java
│   ├── dao
│   │   ├── CategoriaDAO.java
│   │   └── ProdutoDAO.java
│   └── forms
│       ├── CategoriaForm.java
│       └── ProdutoForm.java
├── database.sql
├── README.md
└── .gitignore
```

---

## Pacotes do projeto

### beans

O pacote `beans` contém as classes que representam as tabelas do banco de dados.

Classes:

- `Categoria.java`
- `Produto.java`

Essas classes possuem os atributos, construtores, métodos getters e setters utilizados pela aplicação.

---

### conexao

O pacote `conexao` contém a classe responsável pela conexão entre o projeto Java e o banco de dados MySQL.

Classes:

- `Conexao.java`
- `TesteConexao.java`

A classe `Conexao.java` utiliza JDBC para abrir a conexão com o banco `faculdade_t1`.

---

### dao

O pacote `dao` contém as classes responsáveis pelas operações no banco de dados.

Classes:

- `CategoriaDAO.java`
- `ProdutoDAO.java`

Cada classe DAO possui os métodos básicos solicitados no enunciado:

- `inserir`
- `editar`
- `excluir`
- `getAll`
- `getById`

Esses métodos fazem as operações de cadastro, atualização, exclusão e consulta dos registros no banco de dados.

---

### forms

O pacote `forms` contém as telas do sistema.

Classes:

- `CategoriaForm.java`
- `ProdutoForm.java`

Essas classes representam os formulários gráficos desenvolvidos com Java Swing.

---

## Funcionalidades do sistema

### Tela de Categorias

A tela `CategoriaForm` é referente à tabela que fornece a relação no banco de dados.

Funcionalidades:

- Cadastrar uma categoria
- Pesquisar uma categoria pelo ID
- Atualizar os dados de uma categoria
- Excluir uma categoria
- Listar todas as categorias em uma `JTable`
- Abrir a tela de produtos

Campos disponíveis:

- ID
- Nome
- Descrição

Botões disponíveis:

- Salvar
- Pesquisar por ID
- Atualizar
- Excluir
- Limpar
- Abrir Produtos

---

### Tela de Produtos

A tela `ProdutoForm` é referente à tabela que recebe a relação no banco de dados.

Funcionalidades:

- Cadastrar produtos
- Selecionar uma categoria por meio de um `JComboBox`
- Listar todos os produtos cadastrados em uma `JTable`
- Atualizar a lista de categorias disponíveis no combo

Campos disponíveis:

- ID
- Nome
- Preço
- Quantidade
- Categoria

Botões disponíveis:

- Salvar
- Limpar
- Atualizar Categorias

---

## Banco de dados

O banco de dados utilizado no projeto se chama:

```sql
faculdade_t1
```

O banco possui duas tabelas:

```sql
categoria
produto
```

A tabela `produto` possui uma chave estrangeira chamada `categoria_id`, que referencia a coluna `id` da tabela `categoria`.

---

## Script do banco de dados

Para criar o banco de dados, execute o arquivo `database.sql` no MySQL Workbench ou no phpMyAdmin.

Conteúdo do script:

```sql
CREATE DATABASE faculdade_t1
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

USE faculdade_t1;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(80) NOT NULL,
    descricao VARCHAR(200)
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    categoria_id INT NOT NULL,

    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

INSERT INTO categoria (nome, descricao) VALUES
('Eletrônicos', 'Produtos eletrônicos em geral'),
('Papelaria', 'Materiais escolares e de escritório');
```

---

## Como executar o projeto

### 1. Iniciar o MySQL

Antes de executar o projeto, inicie o servidor MySQL.

Caso esteja usando WAMP, abra o WAMP e inicie o serviço do MySQL.

Confirme se o MySQL está rodando na porta:

```text
3306
```

---

### 2. Criar o banco de dados

Abra o MySQL Workbench ou o phpMyAdmin.

Execute o arquivo:

```text
database.sql
```

Esse arquivo criará:

- O banco `faculdade_t1`
- A tabela `categoria`
- A tabela `produto`
- A relação de chave estrangeira entre as tabelas
- Alguns registros iniciais de categoria

---

### 3. Abrir o projeto no NetBeans

Abra o NetBeans IDE.

Clique em:

```text
File → Open Project
```

Selecione a pasta do projeto:

```text
GabrielT1
```

Depois clique em:

```text
Open Project
```

---

### 4. Adicionar o driver MySQL Connector/J

Para que o Java consiga se conectar ao MySQL, é necessário adicionar o driver JDBC do MySQL ao projeto.

No NetBeans:

```text
Botão direito no projeto → Properties → Libraries
```

Depois clique em:

```text
Add JAR/Folder
```

Adicione o arquivo do MySQL Connector/J, por exemplo:

```text
mysql-connector-j-9.7.0.jar
```

O arquivo deve aparecer nas bibliotecas do projeto.

---

### 5. Conferir a configuração da conexão

Abra o arquivo:

```text
src/conexao/Conexao.java
```

Confira se as informações estão corretas:

```java
private static final String URL = "jdbc:mysql://localhost:3306/faculdade_t1"
        + "?useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=UTC";

private static final String USUARIO = "root";
private static final String SENHA = "";
```

Caso o MySQL tenha senha, altere a linha:

```java
private static final String SENHA = "";
```

Exemplo:

```java
private static final String SENHA = "sua_senha";
```

---

### 6. Testar a conexão

Execute a classe:

```text
conexao.TesteConexao
```

Se a conexão estiver correta, será exibida a mensagem:

```text
Conexão realizada com sucesso!
```

Caso ocorra erro, verifique:

- Se o MySQL está iniciado
- Se a porta está correta
- Se o banco `faculdade_t1` foi criado
- Se o usuário e senha estão corretos
- Se o MySQL Connector/J foi adicionado ao projeto

---

### 7. Configurar a classe principal

No NetBeans, clique com botão direito no projeto e acesse:

```text
Properties → Run
```

No campo `Main Class`, coloque:

```text
forms.CategoriaForm
```

Clique em:

```text
OK
```

---

### 8. Executar o projeto

Para executar o projeto, pressione:

```text
F6
```

Ou clique com botão direito no projeto e selecione:

```text
Run
```

A primeira tela aberta será a tela de cadastro de categorias.

---

## Ordem recomendada de uso

1. Abrir o MySQL ou WAMP
2. Criar o banco usando o arquivo `database.sql`
3. Abrir o projeto no NetBeans
4. Adicionar o MySQL Connector/J
5. Conferir a conexão em `Conexao.java`
6. Executar `TesteConexao.java`
7. Executar `forms.CategoriaForm`
8. Cadastrar categorias
9. Abrir a tela de produtos
10. Cadastrar produtos vinculados às categorias

---

## Exemplo de uso

### Cadastro de categoria

Na tela de categorias, preencha:

```text
Nome: Alimentos
Descrição: Produtos alimentícios
```

Depois clique em:

```text
Salvar
```

A categoria será salva no banco e exibida na tabela.

---

### Cadastro de produto

Na tela de produtos, preencha:

```text
Nome: Mouse
Preço: 59.90
Quantidade: 10
Categoria: Eletrônicos
```

Depois clique em:

```text
Salvar
```

O produto será salvo no banco e exibido na tabela de produtos.

---

## Observações importantes

O projeto utiliza banco de dados local. Portanto, o MySQL precisa estar rodando antes da execução do sistema.

Se a aplicação não conectar ao banco, verifique:

- O servidor MySQL está iniciado
- A porta configurada é `3306`
- O banco `faculdade_t1` existe
- O usuário está correto
- A senha está correta
- O driver MySQL Connector/J foi adicionado ao projeto

---

## Possíveis erros

### Erro: No suitable driver found

Esse erro indica que o driver MySQL Connector/J não foi adicionado corretamente ao projeto.

Solução:

```text
Projeto → Properties → Libraries → Add JAR/Folder
```

Adicione o arquivo `.jar` do MySQL Connector/J.

---

### Erro: Unknown database 'faculdade_t1'

Esse erro indica que o banco de dados ainda não foi criado.

Solução:

Execute o arquivo:

```text
database.sql
```

no MySQL Workbench ou phpMyAdmin.

---

### Erro: Access denied for user 'root'

Esse erro indica problema com usuário ou senha do MySQL.

Solução:

Verifique as informações no arquivo:

```text
src/conexao/Conexao.java
```

Altere o usuário ou senha conforme a configuração do seu MySQL.

---

### Erro ao excluir uma categoria

Caso uma categoria possua produtos vinculados, o banco pode impedir a exclusão por causa da chave estrangeira.

Isso acontece porque a tabela `produto` depende da tabela `categoria`.

Nesse caso, primeiro exclua os produtos vinculados ou cadastre uma categoria sem produtos para testar a exclusão.

---

## Entrega

O projeto pode ser entregue por meio de um repositório no GitHub contendo:

- Código-fonte Java
- Arquivo `database.sql`
- Arquivo `README.md`
- Arquivo `.gitignore`
- Configurações do projeto NetBeans

---

## Autor

Gabriel

---

## Trabalho

Trabalho T1 da faculdade.

Tema: Sistema de cadastro de categorias e produtos com relação 1-N utilizando Java, Swing, JDBC e MySQL.
````
