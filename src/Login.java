import java.util.HashMap;
import java.util.Map;

public class Login {
    private static final String ADMIN_USUARIO = "admin";
    private static final String ADMIN_SENHA = "admin";
    private String cpf;
    private String senha;
    private static final Map<String, String> usuarios = new HashMap<>();
    static{
        usuarios.put("12345678911", "123456");
        usuarios.put("32154398711", "@@@@@");
        usuarios.put("12312312300", "10000000");
    }

    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;
    }

    boolean usuarioExiste(){
        for(String cpf: usuarios.keySet()){
            System.out.println(cpf);
            if(cpf.equals(this.cpf)) return true;
        }
        return false;
    }

    Usuario validarLogin(){
        if(usuarios.get(cpf).equals(senha)) {
            Usuario u = new Usuario(cpf);
            return u;
        }
        return null;
    }

    void cadastrarUsuario(){
        usuarios.put(cpf, senha);
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
