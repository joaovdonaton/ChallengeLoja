package Objetos;

import Interfaces.FormatoDB;

import java.util.Map;

/**
 * Igual a classe Usuário, porém com um formato diferente para salvar na DB.
 */
public class UsuarioHistorico extends Usuario{
    public UsuarioHistorico(String cpf, boolean admin, String senha) {
        super(cpf, admin, senha);
    }

    public UsuarioHistorico(Usuario u) {
        super(u.getCpf(), u.isAdmin(), u.getSenha());
        this.setCarrinho(u.getCarrinho());
    }

    public static final FormatoDB FORMATO_DB = (linha -> {
        String[] dados = linha.split("\\|");
        UsuarioHistorico u = new UsuarioHistorico(dados[0], false, null);
        for(int i = 2; i < Integer.parseInt(dados[1])+2; i++) {
            String[] item = dados[i].split("@");
            u.adicionarAoCarrinho(new Produto(item[0], null, Float.parseFloat(item[2]), 0), Integer.parseInt(item[1]));
        }

        return u;
    });

    @Override
    public String criarLinha(){
        StringBuilder sb = new StringBuilder();
        sb.append(getCpf() + "|" + getCarrinho().size());
        for(Map.Entry<Produto, Integer> e: getCarrinho().entrySet()){
            sb.append("|" + e.getKey().getNome() + "@" + e.getValue() + "@" + e.getKey().getPrecoFormatado().replace(",", "."));
        }

        return sb.toString();
    }
}
