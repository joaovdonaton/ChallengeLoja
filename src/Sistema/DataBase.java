package Sistema;

import Interfaces.Armazenavel;
import Interfaces.FormatoDB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase <T extends Armazenavel>{
    private final String PATH_DB;
    private final FormatoDB<T> formatoDB;
    private List<T> dados = new ArrayList<>();

    public DataBase(String PATH_DB, FormatoDB<T> formatoDB){
        this.PATH_DB = PATH_DB;

        if(formatoDB == null){
            throw new IllegalArgumentException("formatoDB não deve ser null.");
        }
        this.formatoDB = formatoDB;
    }

    /**
     * Lê os dados da base de dados. Usa o método da interface FormatoDB para implementar a lógica que lê cada
     * entrada e retorna o objeto de tipo T.
     */
    public void carregarDados() {
        dados.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(PATH_DB))){
            String linha;
            while((linha = br.readLine()) != null){
                dados.add(formatoDB.lerLinha(linha));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Salva os dados do tipo T na base de dados. Cada entrada da base de dados é uma linha. Usa o método criarLinha
     * da interface Armazenavel para criar essa entrada e escrever para a DB.
     */
    public void salvarDados(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(PATH_DB, false))){
            for(T dado: dados){
                bw.write(dado.criarLinha());
                bw.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        carregarDados();
    }

    public void add(T e){
        this.dados.add(e);
    }

    /**
     * PERIGO! getDados retorna o dados modificável, para que seja possível alterar os itens e depois salvá-los na
     * base de dados.
     */
    public List<T> getDados(){
        return dados;
    }
}
