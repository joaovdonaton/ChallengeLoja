public class Produto {
    String nome;
    String descricao;
    float preco;
    int qnt_estoque;

    Produto(String nome, String descricao, float preco, int qnt_estoque){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qnt_estoque = qnt_estoque;
    }
}
