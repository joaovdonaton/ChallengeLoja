package Interfaces;

/**
 * Interface para ser especificar o formato dos dados de uma certa classe na base de dados.
 */
public interface FormatoDB <T> {
    /**
     * Interpretar os dados de uma única entrada na base de dados e retornar um objeto com os dados extrair
     */
    T lerLinha(String linha);
}
