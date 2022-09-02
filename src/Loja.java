import java.util.Scanner;

public class Loja {
    static Usuario usuario;
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("------------LOJA------------");
        autenticarUsuario();
    }

    static void autenticarUsuario(){
        while (true){
            System.out.print("Usuário (CPF): ");
            String user = scanner.nextLine();
            System.out.print("Senha: ");
            String pass = scanner.nextLine();

            Login login = new Login(user, pass);
            if(!login.usuarioExiste()){
                //cadastrar novo
            }
            else if(!login.validarLogin()) {
                System.out.println("Senha incorreta!");
            }
            else{
                System.out.println("Login realizado com sucesso!");
                break;
            }
        }
    }
}
