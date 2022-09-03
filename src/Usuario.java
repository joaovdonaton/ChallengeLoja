import java.util.List;
import java.util.Map;

public class Usuario {
    private String cpf;
    Map<String, Integer> carrinho;

    public Usuario(String cpf){
        this.cpf = cpf;
    }

    boolean isAdmin(){
        return cpf.equals("admin");
    }

    public String getCpf() {
        return cpf;
    }
}
