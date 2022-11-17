package br.ufrn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Aluguel {
    private int funcionario;
    private int cliente;
    private int veiculo;
    private String dataSaida;
    private String dataRetorno;
    private String tipo;

    public Aluguel(int funcionario, int cliente, int veiculo, String dataSaida, String dataRetorno,String tipo){
        this.funcionario = funcionario;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.dataSaida = dataSaida;
        this.dataRetorno = dataRetorno;
        this.tipo = tipo;
    }

    public boolean store(Connection connection){
        try{
            Statement statment = connection.createStatement();
            statment.execute("INSERT INTO alugueis(cliente, veiculo, funcionario, dataSaida, dataRetorno,tipo) VALUES (" +
                    "'" + this.getCliente() + "'," +
                    "'" + this.getVeiculo() + "'," +
                    "'" + this.getFuncionario() + "'," +
                    "'" + this.getDataSaida() + "'," +
                    "'" + this.getDataRetorno() + "'," +
                    "'" + this.getTipo() + "'" +
                    ")");
            ResultSet saida = statment.getResultSet();
            return true;
        }catch (SQLException e){
            System.out.println("Erro de SQL");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(int funcionario) {
        this.funcionario = funcionario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public int getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(int veiculo) {
        this.veiculo = veiculo;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(String dataRetorno) {
        this.dataRetorno = dataRetorno;
    }
}


