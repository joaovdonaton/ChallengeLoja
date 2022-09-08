import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Login {
    private static final String ADMIN_USUARIO = "admin";
    private static final String ADMIN_SENHA = "admin";
    private static final String PATH_USUARIOS = "./usuarios.txt";
    private String cpf;
    private String senha;
    private static final Map<Usuario, String> usuarios = new HashMap<>(); //usuario, senha

    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    /**
     * Carrega os usuário do arquivo no local PATH_USUARIOS, FORMATO: CPF|SENHA|IS_ADMIN
     */
    void carregarUsuarios(){
        usuarios.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(PATH_USUARIOS))){
            String linha;
            Usuario u;
            while((linha = br.readLine()) != null){
                String[] dados = linha.split("\\|");
                usuarios.put(new Usuario(dados[0], dados[2].equals("1")), dados[1]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean usuarioExiste(){
        for(Usuario u: usuarios.keySet()){
            if(u.getCpf().equals(this.cpf)) return true;
        }
        return false;
    }

    Usuario validarLogin(){ //usuarios.get(cpf).equals(senha)
        String senha = "";
        for(Usuario u : usuarios.keySet()){
            if(u.getCpf().equals(this.cpf)) senha = usuarios.get(u);
        }

        if(senha.equals(this.senha)) {
            return new Usuario(cpf, true);
        }
        return null;
    }

    void cadastrarUsuario(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_USUARIOS, true))){
            bw.append(this.cpf + "|" + this.senha + "|" + "0" + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        carregarUsuarios();
    }

    boolean validarCPF(){
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
