public class Produto {
    private String nome;
    private String descricao;
    private float preco;
    private int qnt_estoque;

    Produto(String nome, String descricao, float preco, int qnt_estoque){
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.qnt_estoque = qnt_estoque;
    }

    /**
     * @return retorna o produto como String no formato NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     */
    public String toString(){
        return this.getNome() + "|" + this.getDescricao() + "|" + getPrecoFormatado().replace(',', '.')
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

    public String getPrecoFormatado(){
        return String.format("%.2f", this.preco);
    }

    public int getQnt_estoque() {
        return qnt_estoque;
    }

    public void removerEstoque(int quantidade){
        if(qnt_estoque - quantidade >= 0) this.qnt_estoque -= quantidade;
        else{
            throw new IllegalArgumentException("Não é possível remover uma quantidade maior que o estoque");
        }
    }
}
