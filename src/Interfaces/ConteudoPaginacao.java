package Interfaces;

public interface ConteudoPaginacao <T> {
    /**
     * @param i recebe um item paginável i.
     * @return retorna uma string formatada com a listagem individual daquele item.
     */
    String stringPorListagem(T i);
}
