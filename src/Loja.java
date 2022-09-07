import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Loja {
    static Usuario usuario;
    static final Scanner scanner = new Scanner(System.in);
    static final int LIMITE_PAGINA = 6;

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
            login.carregarUsuarios();

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
            int opcao = promptOpcao();
            if (opcao == 1) compras();
            else if (opcao == 2) autenticarUsuario();
            else if (opcao == 3) System.out.println("Loja virtual v1.0");
            else if (opcao == 4) {
            }
            else if (opcao == 5) System.exit(0);
            else System.out.println("\n [!] Opção inválida!");
        }
     }

    static void compras(){
        imprimirHeader("COMPRAS");

        Mercado mercado = new Mercado();
        int paginaAtual = 0;
        while(true) {
            mercado.carregarProdutos();

            System.out.println("\n[1] Buscar produto");
            System.out.println("[2] Listar todos os produtos");
            System.out.println("[3] Adicionar o produto ao carrinho");
            System.out.println("[4] Exibir carrinho");
            System.out.println("[5] Finalizar compras");
            System.out.println("[6] Cadastrar Produto (Administrador)");
            System.out.println("[7] Voltar ao menu principal");
            System.out.print("\nEscolha uma opção: ");
            int opcao = promptOpcao();

            if (opcao == 1) {//buscar produto
                System.out.print("Nome do produto que deseja buscar: ");
                List<Produto> produtos = mercado.buscarProdutos(scanner.nextLine());
                if(produtos.size() == 0){
                    System.out.println("Nenhum produto encontrado.");
                    continue;
                }

                System.out.printf("\n%d produtos encontrados! \n\n", produtos.size());

                promptPaginacao(produtos);

            }
            else if (opcao == 2) {//listar produtos
                promptPaginacao(mercado.getProdutos());
            }
            else if (opcao == 3) {//adicionar produto ao carrinho
                System.out.print("Nome do produto: ");
                Produto produto = mercado.getProduto(scanner.nextLine());

                if(produto == null){
                    System.out.println("Produto não encontrado.");
                    continue;
                }

                else if(produto.getQnt_estoque() == 0){
                    System.out.println("Não temos mais " + produto.getNome());
                    continue;
                }

                int quantidade = 0;
                while(quantidade == 0) {
                    System.out.print("Quantidade: ");
                    quantidade = promptOpcao();
                    if(quantidade == 0) System.out.println(" \n [!] Quantidade inválida! \n");
                }

                if(produto.getQnt_estoque()-quantidade < 0){
                    System.out.println("Desculpe, mas só temos " + produto.getQnt_estoque() + " unidades de " +
                            produto.getNome());
                }

                usuario.adicionarAoCarrinho(produto, quantidade);
                System.out.println('\n' + produto.getNome() + " adicionado ao carrinho com sucesso! ");
            }
            else if (opcao == 4) {// exibir carrinho
                System.out.println("Itens no Carrinho:\n");

                for(Map.Entry<Produto, Integer> produto: usuario.getCarrinho().entrySet()){
                    System.out.println(produto.getValue() + " x " + produto.getKey().getNome() + "(R$ " +
                            produto.getKey().getPrecoFormatado() + ")");
                }

                System.out.println("Preço Total: R$ " + String.format("%.2f", usuario.totalCarrinho()));
            }
            else if (opcao == 5) {// finalizar compras
                mercado.comprar(usuario);

                System.out.println("Compra realizada com sucesso!");
                System.out.println("Total: R$ " + String.format("%.2f", usuario.totalCarrinho()));

                usuario.limparCarrinho();
            }
            else if(opcao == 6){ // cadastrar prod
            }
            else if(opcao == 7){
                break;
            }
            else {
                System.out.println("\n [!] Opção inválida!");
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

    static List<Produto> paginarProdutos(List<Produto> itens, int paginaAtual){
        List<Produto> itensPagina = new ArrayList<>();
        for(int i = LIMITE_PAGINA*(paginaAtual); i < LIMITE_PAGINA*(paginaAtual)+LIMITE_PAGINA; i++){
            if(i >= itens.size()) break;
            itensPagina.add(itens.get(i));
        }

        return itensPagina;
    }

    static void  promptPaginacao(List<Produto> produtos){
        int paginaAtual = 0;

        while(true) {
            System.out.println("\nPágina " + (paginaAtual + 1) + '\n');

            for (Produto produto : paginarProdutos(produtos, paginaAtual)) {
                System.out.println("[" + produto.getNome() + "] " + produto.getDescricao() +
                        ". Preço: R$ " + produto.getPrecoFormatado());
            }

            System.out.print("\nDigite proxima, anterior ou sair: ");
            String escolha = scanner.nextLine();

            //verificar se o usuário não está tentando acessar página inexistentes
            if((escolha.equalsIgnoreCase("Proxima") &&
                    paginaAtual+1 > Math.floor(produtos.size()/(double)LIMITE_PAGINA) ||
                    (paginaAtual-1 < 0 && escolha.equalsIgnoreCase("Anterior")))){
                System.out.println("\n [!] Essa pagína não existe!");
                continue;
            }

            if (escolha.equalsIgnoreCase("Proxima")) paginaAtual += 1;
            else if (escolha.equalsIgnoreCase("Anterior")) paginaAtual += -1;
            else if (escolha.equalsIgnoreCase("Sair")) break;
            else {
                System.out.println("\n [!] Opção Inválida!");
            }
        }
    }

    /**
     * Pede a opção ao usuário, verifica se o input é um int
     * @return retorna a opção caso seja um int, e zero caso seja inválida
     */
    static int promptOpcao(){
        try {
            return Integer.parseInt(scanner.nextLine());
        }
        catch (NumberFormatException e){
            return 0;
        }
    }
}
