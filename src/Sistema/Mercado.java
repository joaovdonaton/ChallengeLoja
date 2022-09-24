package Sistema;

import Objetos.Produto;
import Objetos.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mercado {
    private List<Produto> produtos = new ArrayList<>();
    private static final String PATH_PRODUTOS = "./produtos.txt";
    private static final String PATH_HISTORICO = "./historico.txt";

    /**
     * Cadastra o objeto produto no produtos.txt, FORMATO: NOME|DESCRIÇÃO|PREÇO|ESTOQUE
     * @param produto objeto da class Product que contem as informações para cadastrar o produto
     */
    public void cadastrarProduto(Produto produto){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PRODUTOS, true))) {
            bw.append((produto.toString() + "\n"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        carregarProdutos();
    }

    /**
     * @param p deve ser o mesmo produto que está em this.produtos, logo p deve vir de getProduto()
     */
    public void adicionarQuantidadeAoEstoque(Produto p, int quantidade){
        for(Produto produto: produtos){ // atualizar estoque
            if(produto.equals(p)){
                produto.adicionarEstoque(quantidade);
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PRODUTOS, false))){
            for(Produto produto: produtos) {
                bw.append(produto.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removerProdutoDoEstoque(Produto p){
        this.produtos.remove(p);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PRODUTOS, false))){
            for(Produto produto: produtos) {
                bw.append(produto.toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void carregarProdutos(){
        produtos.clear();
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
    public List<Produto> buscarProdutos(String busca){
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

    public List<Produto> getProdutos(){
        return produtos;
    }

    /**
     * @param nome nome exato do produto
     * @return produto, null se não encontrar
     */
    public Produto getProduto(String nome){
        Produto p = null;
        for(Produto produto: produtos){
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
            for(Produto p: produtos){
                if(produto.getKey().getNome().equals(p.getNome())){
                    p.removerEstoque(produto.getValue());
                }
            }
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_PRODUTOS, false))){
            for(Produto produto: produtos) {
                bw.append(produto.toString() + "\n");
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        List<Usuario> usuarios = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(PATH_HISTORICO))){
            String linha = "";
            while((linha = br.readLine()) != null){
                String[] dados = linha.split("\\|");
                Usuario u = new Usuario(dados[0], false, null);
                for(int i = 2; i < Integer.parseInt(dados[1])+2; i++) {
                    String[] item = dados[i].split("@");
                    u.adicionarAoCarrinho(new Produto(item[0], null, Float.parseFloat(item[2]), 0), Integer.parseInt(item[1]));
                }
                usuarios.add(u);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }
}
