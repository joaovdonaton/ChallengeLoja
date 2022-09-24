package Sistema;

import Objetos.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login {
    private static final String PATH_USUARIOS = "./usuarios.txt";
    private String cpf;
    private String senha;
    private static final DataBase<Usuario> DB = new DataBase<>(PATH_USUARIOS, (linha) -> {
        String[] dados = linha.split("\\|");
        return new Usuario(dados[0], dados[2].equals("1"), dados[1]);
    });

    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    /**
     * Carrega os usuário do arquivo no local PATH_USUARIOS, FORMATO: CPF|SENHA|IS_ADMIN
     */
    public void carregarUsuarios(){
        DB.carregarDados();
    }

    /**
     * Deve ser chamado antes de validarLogin()
     */
    public boolean usuarioExiste(){
        for(Usuario u: DB.getDados()){
            if(u.getCpf().equals(this.cpf)) return true;
        }
        return false;
    }

    public Usuario validarLogin(){
        Usuario user = null;
        for(Usuario u : DB.getDados()){
            if(u.getCpf().equals(this.cpf)) user = u;
        }

        if(user == null) return null;

        if(user.getSenha().equals(this.senha)) {
            return new Usuario(cpf, user.isAdmin(), senha);
        }
        return null;
    }

    public void cadastrarUsuario(){
        DB.add(new Usuario(cpf, false, senha));
        DB.salvarDados();
    }

    public boolean validarCPF(){
        if(cpf.length() == 14) cpf = Usuario.desformatarCPF(cpf);
        else if(cpf.length() != 11) return false;

        int soma = 0;
        for(int i = 10, j = 0; i >= 2; i--, j++){
            soma += Integer.parseInt(String.valueOf(cpf.charAt(j)))*i;
        }

        if(!((soma*10)%11 == Integer.parseInt(String.valueOf(cpf.charAt(cpf.length()-2))))) return false;

        soma = 0;
        for(int i = 11, j = 0; i >= 2; i--, j++){
            soma += Integer.parseInt(String.valueOf(cpf.charAt(j)))*i;
        }

        if(!((soma*10)%11 == Integer.parseInt(String.valueOf(cpf.charAt(cpf.length()-1))))) return false;

        return true;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
