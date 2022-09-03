import java.util.List;
import java.util.Map;

public class Usuario {
    private String cpf;
    Map<String, Integer> carrinho;
    private boolean admin;

    public Usuario(String cpf, boolean admin){
        this.cpf = cpf;
    }

    boolean isAdmin(){
        return admin;
    }

    public String getCpf() {
        return cpf;
    }
}
