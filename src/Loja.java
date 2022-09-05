import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Loja {
    static Usuario usuario;
    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        imprimirHeader("LOGIN");
        autenticarUsuario();
        imprimirHeader("MENU PRINCIPAL");
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
                System.out.print("Deseja criar um novo usuário? [S/N]: ");
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
            System.out.println("\n[1] Fazer compras");
            System.out.println("[2] Trocar usuário");
            System.out.println("[3] Sobre");
            System.out.println("[4] Relatório (Administrador)");
            System.out.println("[5] Sair");
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 1) compras();
            else if (opcao == 2) autenticarUsuario();
            else if (opcao == 3) System.out.println("Loja virtual v1.0");
            else if (opcao == 4) {
            }
            else if (opcao == 5) System.exit(0);
            else System.out.println("Opção inválida!");
        }
    }

    static void compras(){
        imprimirHeader("COMPRAS");

        Mercado mercado = new Mercado();
        mercado.carregarProdutos();

        while(true) {
            System.out.println("\n[1] Buscar produto");
            System.out.println("[2] Listar todos os produtos");
            System.out.println("[3] Adicionar o produto ao carrinho");
            System.out.println("[4] Exibir carrinho");
            System.out.println("[5] Finalizar compras");
            System.out.println("[6] Cadastrar Produto (Administrador)");
            System.out.println("[7] Voltar ao menu principal");
            System.out.print("\nEscolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            if (opcao == 1) {//buscar produto
                System.out.print("Nome do produto que deseja buscar: ");
                List<Produto> produtos = mercado.buscarProdutos(scanner.nextLine());
                if(produtos.size() == 0){
                    System.out.println("Nenhum produto encontrado.");
                    continue;
                }

                System.out.printf("\n%d produtos encontrados! \n\n", produtos.size());

                for(Produto produto: produtos){
                    System.out.println("["+produto.getNome()+"] " + produto.getDescricao() +
                            ". Preço: R$ " + produto.getPreco());
                }
            }
            else if (opcao == 2) {//listar produtos
                for(Produto produto: mercado.getProdutos()){
                    System.out.println("["+produto.getNome()+"] " + produto.getDescricao() +
                            ". Preço: R$ " + produto.getPreco());
                }
            }
            else if (opcao == 3) {//adicionar produto ao carrinho
                System.out.print("Nome do produto: ");
                Produto produto = mercado.getProduto(scanner.nextLine());

                if(produto == null){
                    System.out.println("Produto não encontrado.");
                    continue;
                }

                System.out.print("Quantidade: ");

                usuario.adicionarAoCarrinho(produto, Integer.parseInt(scanner.nextLine()));
            }
            else if (opcao == 4) {// exibir carrinho
                System.out.println("Itens no Carrinho:\n");
                for(Map.Entry<Produto, Integer> produto: usuario.getCarrinho().entrySet()){
                    System.out.println(produto.getValue() + " x " + produto.getKey().getNome());
                }
            }
            else if (opcao == 5) {// finalizar compras
            }
            else if(opcao == 6){ // cadastrar prod
            }
            else if(opcao == 7){
                break;
            }
            else {
                System.out.println("Opção inválida!");
            }
        }
    }

    static void imprimirHeader(String texto){
        System.out.println();
        for (int i = 0; i < 35; i++) System.out.print("-");
        System.out.print(texto);
        for (int i = 0; i < 35; i++) System.out.print("-");
        System.out.println("\n");
    }
}
