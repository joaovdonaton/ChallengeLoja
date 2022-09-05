public class Produto {
    private String nome;
    private String descricao;
    private double preco;
    private int qnt_estoque;

    Produto(String nome, String descricao, double preco, int qnt_estoque){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qnt_estoque = qnt_estoque;
    }

    /**
     * @return retorna o produto como String no formato NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     */
    public String toString(){
        return this.getNome() + "|" + this.getDescricao() + "|" + this.getPreco()
                + "|" + this.getQnt_estoque();
    }

    public String getNome() {
        return nome;
    }


    public String getDescricao() {
        return descricao;
    }


    public double getPreco() {
        return preco;
    }

    public int getQnt_estoque() {
        return qnt_estoque;
    }
}
