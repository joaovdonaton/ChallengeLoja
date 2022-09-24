package Sistema;

import Objetos.HistoricoDoUsuario;
import Objetos.Produto;
import Objetos.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
*
* BUG: e.g 100 unidades de doritos, o usuário pode adicionar ao carrinho 99, depois 99, depois 99... ultrapassando o limite, já que o estoque só é atualizado dps da compra
*
*
*
* */


public class Mercado {
    private static final String PATH_PRODUTOS = "./produtos.txt";
    private static final String PATH_HISTORICO = "./historico.txt";

    private static final DataBase<Produto> DBProdutos = new DataBase<>(PATH_PRODUTOS, (linha -> {
        String[] dados = linha.split("\\|");
        return new Produto(dados[0], dados[1], Float.parseFloat(dados[2]), Integer.parseInt(dados[3]));
    }));

    private static final DataBase<Usuario> DBHistorico = new DataBase<>(PATH_HISTORICO, (linha -> {
        String[] dados = linha.split("\\|");
        Usuario u = new Usuario(dados[0], false, null);
        for(int i = 2; i < Integer.parseInt(dados[1])+2; i++) {
            String[] item = dados[i].split("@");
            u.adicionarAoCarrinho(new Produto(item[0], null, Float.parseFloat(item[2]), 0), Integer.parseInt(item[1]));
        }

        return u;
    }));

    /**
     * Cadastra o objeto produto no produtos.txt, FORMATO: NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     * @param produto objeto da class Product que contem as informações para cadastrar o produto
     */
    public void cadastrarProduto(Produto produto){
        DBProdutos.add(produto);
        DBProdutos.salvarDados();
    }

    /**
     * @param p deve ser o mesmo produto que está em this.produtos, logo p deve vir de getProduto()
     */
    public void adicionarQuantidadeAoEstoque(Produto p, int quantidade){
        var produtos = DBProdutos.getDados();

        //PODE DAR PAU AQUI
        for(Produto produto: produtos){ // atualizar estoque
            if(produto.equals(p)){
                produto.adicionarEstoque(quantidade);
            }
        }

        DBProdutos.salvarDados();
    }

    public void removerProdutoDoEstoque(Produto p){
        var produtos = DBProdutos.getDados();
        produtos.remove(p);

        DBProdutos.salvarDados();
    }

    public void carregarProdutos(){
        DBProdutos.carregarDados();
    }

    /**
     * @param busca palavra chave para busca nos nomes
     * @return List de Produtos
     */
    public List<Produto> buscarProdutos(String busca){
        List<Produto> encontrados = new ArrayList<>();
        for(Produto produto: DBProdutos.getDados()){
            for(String palavra: produto.getNome().split(" ")){
                if(palavra.equalsIgnoreCase(busca)){
                    encontrados.add(produto);
                }
            }
        }

        return encontrados;
    }

    public List<Produto> getProdutos(){
        return DBProdutos.getDados();
    }

    /**
     * @param nome nome exato do produto
     * @return produto, null se não encontrar
     */
    public Produto getProduto(String nome){
        Produto p = null;
        for(Produto produto: DBProdutos.getDados()){
            if(produto.getNome().equalsIgnoreCase(nome)){
                p = produto;
                break;
            }
        }

        return p;
    }

    /**
     * Compra o carrinho do usuario, removendo as quantidades compradas da base de dados.
     */
    public void comprar(Usuario usuario){
        for(Map.Entry<Produto, Integer> produto: usuario.getCarrinho().entrySet()){ // remover os itens do estoque
            for(Produto p: DBProdutos.getDados()){
                if(produto.getKey().getNome().equals(p.getNome())){
                    p.removerEstoque(produto.getValue());
                }
            }
        }

        DBProdutos.salvarDados();
    }

    /**
     * Salva os itens da compra no PATH_HISTORICO, FORMATO: CPF|QNT_ITENS|ITEM_1|ITEM_2|ITEM_3
     * FORMATO DE ITEM_N: NOME@QUANTIDADE@PRECOPAGO
     * @param usuario
     */
    public void salvarCompraNoHistorico(Usuario usuario){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_HISTORICO, true))){
            bw.append(usuario.getCpf() + "|" + usuario.getCarrinho().size());
            for(Map.Entry<Produto, Integer> e: usuario.getCarrinho().entrySet()){
                bw.append("|" + e.getKey().getNome() + "@" + e.getValue() + "@" + e.getKey().getPrecoFormatado().replace(",", "."));
            }
            bw.append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return uma list em que cada Objetos.Usuario representa uma compra passada. Importante
     * notar que a descrição e a quantidade do estoque são null e zero respectivamente, visto que não são
     * úteis para o histórico.
     */
    public List<Usuario> carregarHistorico(){
        DBHistorico.carregarDados();
        return DBHistorico.getDados();
    }
}
