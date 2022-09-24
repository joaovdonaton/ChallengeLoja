package Sistema;

import Interfaces.ConteudoPaginacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Paginador <T>{
    private List<T> itens;
    private ConteudoPaginacao<T> conteudoPaginacao;
    private final int LIMITE_PAGINA;
    private Scanner scanner = new Scanner(System.in);

    public Paginador(List<T> itens, ConteudoPaginacao<T> conteudoPaginacao, int LIMITE_PAGINA){
        this.itens = itens;
        this.conteudoPaginacao = conteudoPaginacao;
        this.LIMITE_PAGINA = LIMITE_PAGINA;
    }

    private List<T> paginarItens(int paginaAtual){
        List<T> itensPagina = new ArrayList<>();
        for(int i = LIMITE_PAGINA*(paginaAtual); i < LIMITE_PAGINA*(paginaAtual)+LIMITE_PAGINA; i++){
            if(i >= itens.size()) break;
            itensPagina.add(itens.get(i));
        }

        return itensPagina;
    }

    public void promptPaginacao(){
        int paginaAtual = 0;

        while(true) {
            System.out.println("\nPágina " + (paginaAtual + 1) + '\n');

            for (T item : paginarItens(paginaAtual)) {
                System.out.println(conteudoPaginacao.stringPorListagem(item));
            }

            System.out.print("\nDigite proxima, anterior ou sair: ");
            String escolha = scanner.nextLine();

            //verificar se o usuário não está tentando acessar página inexistentes
            if((escolha.equalsIgnoreCase("Proxima") &&
                    paginaAtual+1 > Math.floor(itens.size()/(double)LIMITE_PAGINA) ||
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
}
