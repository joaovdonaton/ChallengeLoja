package Interfaces;

/**
 * Interface para classes cujos dados podem ser armazenados através da classe DataBase
 */
public interface Armazenavel {
    /**
     * @return cria uma entrada na base de dados
     */
    String criarLinha();
}
