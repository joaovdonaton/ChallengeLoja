package Objetos;

public record HistoricoDoUsuario(String cpf, int numCompras, float totalComprado) {
    @Override
    public String toString() {
        return String.format("CPF: [%s] - Comprou %d produtos - Preço Total: R$ %.2f - Preço Médio: R$ %.2f \n",
                Usuario.formatarCPF(cpf), numCompras, totalComprado, totalComprado/numCompras);
    }
}
