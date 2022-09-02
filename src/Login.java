import java.util.HashMap;
import java.util.Map;

public class Login {
    private static final String ADMIN_USUARIO = "admin";
    private static final String ADMIN_SENHA = "admin";
    private String cpf;
    private String senha;
    private Map<String, String> usuarios = new HashMap<>();

    public Login(String cpf, String senha) {
        this.cpf = cpf;
        this.senha = senha;

        //criar base de usuarios
        usuarios.put("12345678911", "123456");
        usuarios.put("32154398711", "@@@@@");
        usuarios.put("12312312300", "10000000");
    }

    boolean usuarioExiste(){
        for(String cpf: usuarios.keySet()){
            if(cpf.equals(this.cpf)) return true;
        }
        return false;
    }

    boolean validarLogin(){
        return usuarios.get(cpf).equals(senha);
    }
}
