import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Mercado {
    private List<Produto> produtos = new ArrayList<>();
    private static final String PATH_PRODUTOS = "./produtos.txt";

    /**
     * Cadastra o objeto produto no produtos.txt, FORMATO: NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     * @param produto objeto da class Product que contem as informações para cadastrar o produto
     */
    void cadastrarProduto(Produto produto){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PRODUTOS, true))) {
            bw.append((produto.toString() + "\n"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void carregarProdutos(){
        try(BufferedReader br = new BufferedReader(new FileReader(PATH_PRODUTOS))){
            String linha;
            while((linha = br.readLine()) != null){
                String[] dados = linha.split("\\|");
                produtos.add(new Produto(dados[0], dados[1], Float.parseFloat(dados[2]), Integer.parseInt(dados[3])));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param busca palavra chave para busca nos nomes
     * @return List de Produtos
     */
    List<Produto> buscarProdutos(String busca){
        List<Produto> encontrados = new ArrayList<>();
        for(Produto produto: produtos){
            for(String palavra: produto.getNome().split(" ")){
                if(palavra.equalsIgnoreCase(busca)){
                    encontrados.add(produto);
                }
            }
        }

        return encontrados;
    }

    List<Produto> getProdutos(){
        return produtos;
    }

    /**
     * @param nome nome exato do produto
     * @return produto, null se não encontrar
     */
    Produto getProduto(String nome){
        Produto p = null;
        for(Produto produto: produtos){
            if(produto.getNome().equalsIgnoreCase(nome)){
                p = produto;
                break;
            }
        }

        return p;
    }
}
