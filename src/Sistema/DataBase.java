package Sistema;

import Interfaces.Armazenavel;
import Interfaces.FormatoDB;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataBase <T extends Armazenavel>{
    private final String PATH_DB;
    private final FormatoDB<T> formatoDB;
    private List<T> dados = new ArrayList<>();

    public DataBase(String PATH_DB, FormatoDB<T> formatoDB){
        this.PATH_DB = PATH_DB;
        this.formatoDB = formatoDB;
    }

    void carregarDados() {
        dados.clear();
        try(BufferedReader br = new BufferedReader(new FileReader(PATH_DB))){
            String linha;
            while((linha = br.readLine()) != null){
                dados.add(formatoDB.lerLinha(linha));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void salvarDados(){
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

    void add(T e){
        this.dados.add(e);
    }

    List<T> getDados(){
        return Collections.unmodifiableList(dados);
    }

}
