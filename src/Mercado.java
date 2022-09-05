import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mercado {
    private List<Produto> produtos = new ArrayList<>();
    private static final String PATH_PRODUTOS = "./produtos.txt";

    /**
     * Cadastra o objeto produto no produtos.txt, FORMATO: NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     * @param produto
     */
    void cadastrarProduto(Produto produto){

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

    List<Produto> getProdutos(){
        return produtos;
    }
}
