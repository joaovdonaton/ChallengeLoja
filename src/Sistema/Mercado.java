package Sistema;

import Objetos.Produto;
import Objetos.UsuarioHistorico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mercado {
    private static final String PATH_PRODUTOS = "./produtos.txt";
    private static final String PATH_HISTORICO = "./historico.txt";
    private static final DataBase<Produto> DBProdutos = new DataBase<>(PATH_PRODUTOS, Produto.FORMATO_DB);
    private static final DataBase<UsuarioHistorico> DBHistorico = new DataBase<>(PATH_HISTORICO, UsuarioHistorico.FORMATO_DB);

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
     * A quantidade de cada produto comprado já deve ter sido atualizada anteriormente, através de getProduto
     */
    public void comprar(){
        DBProdutos.salvarDados();
    }

    /**
     * Salva os itens da compra no PATH_HISTORICO, FORMATO: CPF|QNT_ITENS|ITEM_1|ITEM_2|ITEM_3
     * FORMATO DE ITEM_N: NOME@QUANTIDADE@PRECOPAGO
     */
    public void salvarCompraNoHistorico(UsuarioHistorico usuarioHistorico){
        carregarHistorico();
        DBHistorico.add(usuarioHistorico);
        DBHistorico.salvarDados();
    }

    /**
     * @return uma list em que cada UsuarioHistorico representa uma compra passada. Importante
     * notar que a descrição e a quantidade do estoque são null e zero respectivamente, visto que não são
     * úteis para o histórico.
     */
    public List<UsuarioHistorico> carregarHistorico(){
        DBHistorico.carregarDados();
        return DBHistorico.getDados();
    }
}
