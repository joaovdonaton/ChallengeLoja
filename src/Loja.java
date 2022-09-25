import Objetos.HistoricoDoUsuario;
import Objetos.Produto;
import Objetos.Usuario;
import Objetos.UsuarioHistorico;
import Sistema.Login;
import Sistema.Mercado;
import Sistema.Paginador;

import java.util.*;

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
            login.carregarUsuarios();

            if(!login.validarCPF() && !user.equalsIgnoreCase("admin")){
                System.out.println("\n [!] CPF inválido!");
                continue;
            }

            if(!login.usuarioExiste()){
                System.out.println("\n [!] Usuário não cadastrado.");
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
                System.out.println("Login realizado com sucesso! Logado como: " + Usuario.formatarCPF(usuario.getCpf()));
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
            else if (opcao == 3) System.out.println("Mercadinho Virtual v1.0 - Desenvolvido por João Vitor Macambira Donaton");
            else if (opcao == 4) { // relatório
                if(!usuario.isAdmin()){
                    System.out.println("\n [!] Você não é um administrador! ");
                    continue;
                }

                System.out.println("\nRelatório de Clientes (Ordenado por cliente que gerou mais receita): \n");

                Mercado m = new Mercado();
                List<UsuarioHistorico> usuariosHistorico = m.carregarHistorico();

                /* O método carregar histórico retorna todas as compras individualmente, então se um cliente tiver feito
                duas compras, ele aparecerá duas vezes no histórico. Logo precisamos ver quais são os CPFS de cada cliente
                individual e buscar todas as suas compras no histórico.*/

                Set<String> cpfsDiferentes = new HashSet<>();
                for(Usuario u: usuariosHistorico){
                    cpfsDiferentes.add(u.getCpf());
                }

                List<HistoricoDoUsuario> historico = new ArrayList<>();
                float totalVendido = 0;
                for(String cpf: cpfsDiferentes){
                    int numCompras = 0;
                    float totalComprado = 0;
                    for(Usuario u: usuariosHistorico){
                        if(cpf.equals(u.getCpf())){
                            for(Map.Entry<Produto, Integer> e: u.getCarrinho().entrySet()){
                                numCompras += e.getValue();
                                totalComprado += e.getKey().getPreco()*e.getValue();
                            }
                        }
                    }

                    totalVendido += totalComprado;
                    historico.add(new HistoricoDoUsuario(cpf, numCompras, totalComprado));

                }

                //ordenar por maior receita
                Collections.sort(historico);
                Collections.reverse(historico);

                new Paginador<>(historico, (HistoricoDoUsuario::toString), 3).promptPaginacao();

                System.out.printf("\nReceita Total: R$ %.2f\n", totalVendido);
            }
            else if (opcao == 5) System.exit(0);
            else System.out.println("\n [!] Opção inválida!");
        }
     }

    static void compras(){
        imprimirHeader("COMPRAS");

        Mercado mercado = new Mercado();
        while(true) {
            mercado.carregarProdutos();

            System.out.println("\n[1] Buscar produto");
            System.out.println("[2] Listar todos os produtos");
            System.out.println("[3] Adicionar o produto ao carrinho");
            System.out.println("[4] Exibir carrinho");
            System.out.println("[5] Finalizar compras");
            System.out.println("[6] Gerenciar Estoque (Administrador)");
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

                new Paginador<>(produtos, (produto) -> "[" + produto.getNome() + "] " + produto.getDescricao() +
                        ". Preço: R$ " + produto.getPrecoFormatado(), 5).promptPaginacao();

            }
            else if (opcao == 2) {//listar produtos
               new Paginador<>(mercado.getProdutos(), (produto) -> "[" + produto.getNome() + "] " + produto.getDescricao() +
                        ". Preço: R$ " + produto.getPrecoFormatado(), 5).promptPaginacao();
            }
            else if (opcao == 3) {//adicionar produto ao carrinho
                System.out.print("Nome do produto: ");
                Produto produto = mercado.getProduto(scanner.nextLine());

                if(produto == null){
                    System.out.println("Objetos.Produto não encontrado.");
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
                    continue;
                }

                usuario.adicionarAoCarrinho(produto, quantidade);
                System.out.println('\n' + produto.getNome() + " x " + quantidade + " adicionado ao carrinho com sucesso! ");
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
                if(usuario.getCarrinho().size() == 0){
                    System.out.println("\n [!] O carrinho está vazio!");
                    continue;
                }

                mercado.comprar(usuario);
                mercado.salvarCompraNoHistorico(new UsuarioHistorico(usuario));

                System.out.println("Compra realizada com sucesso!");
                System.out.println("Total: R$ " + String.format("%.2f", usuario.totalCarrinho()));

                usuario.limparCarrinho();
            }
            else if(opcao == 6){ // gerenciar estoque
                if(!usuario.isAdmin()){
                    System.out.println("\n [!] Você não é um administrador! ");
                    continue;
                }

                gerenciarEstoque(mercado);
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

    static void gerenciarEstoque(Mercado mercado){
        while(true){
            imprimirHeader("GERENCIADOR DE ESTOQUE");

            System.out.println("\n[1] Cadastrar produto");
            System.out.println("[2] Visualizar estoque");
            System.out.println("[3] Adicionar ao estoque");
            System.out.println("[4] Remover produto");
            System.out.println("[5] Voltar às compras");
            System.out.print("\nEscolha uma opção: ");
            int opcao = promptOpcao();

            if(opcao == 1){
                System.out.println("Nome: ");
                String nome = scanner.nextLine();
                System.out.println("Preço (use vírgula para casas decimais):");
                float preco = Float.parseFloat(scanner.nextLine().replace(',', '.'));
                System.out.println("Descrição: ");
                String descricao = scanner.nextLine();
                System.out.println("Quantidade no estoque:");
                int qnt = Integer.parseInt(scanner.nextLine());

                mercado.cadastrarProduto(new Produto(nome, descricao, preco, qnt));
            }
            else if(opcao == 2){
                new Paginador<>(
                        mercado.getProdutos(), (produto) -> "[" + produto.getNome() + "] Quantidade Disponível: " + produto.getQnt_estoque(),
                        5).promptPaginacao();
            }
            else if(opcao == 3){
                System.out.println("Nome: ");
                String nome = scanner.nextLine();

                Produto p = mercado.getProduto(nome);
                if(p == null){
                    System.out.println("\n [!] Objetos.Produto não encontrado!");
                    continue;
                }

                System.out.println("Quantidade a ser adicionada: ");
                int quantidade = Integer.parseInt(scanner.nextLine());

                mercado.adicionarQuantidadeAoEstoque(p, quantidade);
            }
            else if(opcao == 4){
                System.out.println("Nome: ");
                String nome = scanner.nextLine();

                Produto p = mercado.getProduto(nome);
                if(p == null){
                    System.out.println("\n [!] Objetos.Produto não encontrado!");
                    continue;
                }

                mercado.removerProdutoDoEstoque(p);
                System.out.println("\nObjetos.Produto Removido com sucesso!");
            }
            else if(opcao == 5) break;
            else{
                System.out.println("\n [!] Opção inválida!");
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
