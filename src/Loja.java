import java.util.Scanner;

public class Loja {
    static Usuario usuario;
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("------------LOGIN------------");
        autenticarUsuario();
        System.out.println("------------MENU PRINCIPAL------------");
        menuPrincipal();
    }

    static void autenticarUsuario(){
        while (true){
            System.out.print("Usuário (CPF): ");
            String user = scanner.nextLine();
            String senha = "";

            Login login = new Login(user, senha);
            if(!login.usuarioExiste()){
                System.out.println("Usuário não cadastrado.");
                System.out.println("Deseja criar um novo usuário? [S/N]");
                if(scanner.nextLine().equals("S")){
                    System.out.print("Crie sua senha: ");
                    login.setSenha(scanner.nextLine());
                    login.cadastrarUsuario();
                    System.out.println("Usuário cadastrado com sucesso!");
                }
                continue;
            }

            System.out.print("Senha: ");
            login.setSenha(scanner.nextLine());

            usuario = login.validarLogin();
            if(usuario != null){
                System.out.println("Login realizado com sucesso! Logado como: " + usuario.getCpf());
                break;
            }
            else{
                System.out.println("Senha inválida");
            }
        }
    }

    static void menuPrincipal(){
        while(true) {
            System.out.println("[1] Fazer compras");
            System.out.println("[2] Trocar usuário");
            System.out.println("[3] Sobre");
            System.out.println("[4] Sair");
            System.out.println("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao){
                case 1:
                    break;
                case 2:
                    autenticarUsuario();
                    break;
                case 3:
                    System.out.println("Loja virtual v1.0");
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
