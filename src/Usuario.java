import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String cpf;
    private Map<Produto, Integer> carrinho; //Produto, Quantidade
    private boolean admin;

    public Usuario(String cpf, boolean admin){
        this.cpf = cpf;
        carrinho = new HashMap<>();
    }

    boolean isAdmin(){
        return admin;
    }

    public String getCpf() {
        return cpf;
    }

    public void adicionarAoCarrinho(Produto produto, int quantidade){
        carrinho.put(produto, quantidade);
    }

    public Map<Produto, Integer> getCarrinho(){
        return carrinho;
    }

    public double totalCarrinho(){
        double totalPreco = 0;
        for(Map.Entry<Produto, Integer> produto: getCarrinho().entrySet()){
            totalPreco += produto.getKey().getPreco()*produto.getValue();
        }

        return totalPreco;
    }

    public void limparCarrinho(){
        this.carrinho.clear();
    }
}
