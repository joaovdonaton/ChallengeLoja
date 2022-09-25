package Objetos;

import Interfaces.Armazenavel;
import Interfaces.FormatoDB;

import java.util.HashMap;
import java.util.Map;

public class Usuario implements Armazenavel {
    private String cpf;
    private Map<Produto, Integer> carrinho; //Objetos.Produto, Quantidade
    private boolean admin;

    public String getSenha() {
        return senha;
    }

    private String senha;

    public static final FormatoDB<Usuario> FORMATO_DB = (linha) -> {
        String[] dados = linha.split("\\|");
        return new Usuario(dados[0], dados[2].equals("1"), dados[1]);
    };

    public Usuario(String cpf, boolean admin, String senha){
        this.cpf = cpf;
        this.admin = admin;
        carrinho = new HashMap<>();
        this.senha = senha;
    }

    public boolean isAdmin(){
        return admin;
    }

    public String getCpf() {
        return cpf;
    }

    public static String formatarCPF(String cpf){
        if(cpf.length() != 11) return cpf; // retorna o cpf normal caso não seja formatável
        String[] chars = cpf.split("");
        return chars[0]+chars[1]+chars[2]+"."+chars[3]+chars[4]+chars[5]+"."+chars[6]+chars[7]+chars[8]+"-"+chars[9]+chars[10];
    }

    /* e.g 111.111.111-00 => 11111111100 (remove pontos e hifens)
    * */
    public static String desformatarCPF(String cpf){
        return cpf.replace(".", "").replace("-", "");
    }

    public void adicionarAoCarrinho(Produto produto, int quantidade){
        carrinho.put(produto, quantidade);
    }

    public Map<Produto, Integer> getCarrinho(){
        return carrinho;
    }

    public double totalCarrinho(){
        return getCarrinho().entrySet().stream()
                .mapToDouble((i) -> i.getValue()*i.getKey().getPreco()).reduce(0, Double::sum);
    }

    public void limparCarrinho(){
        this.carrinho.clear();
    }

    @Override
    public String criarLinha() {
        return this.cpf + "|" + this.senha + "|" + ( isAdmin() ? "1" : "0");
    }
}
