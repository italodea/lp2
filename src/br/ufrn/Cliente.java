package br.ufrn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;

public class Cliente {

    private String cpf;
    private String nome;
    private String nascimento;
    private String categoriaCHN;
    private String email;


    public Cliente(String cpf, String nome, String nascimento, String categoriaCHN, String email) {
        this.cpf = cpf;
        this.nome = nome;
        this.nascimento = nascimento;
        this.categoriaCHN = categoriaCHN;
        this.email = email;
    }

    public boolean store(Connection connection){
        try{
            Statement statment = connection.createStatement();
            statment.execute("INSERT INTO clientes(cpf, nome, categoriaCNH, email, nascimento) VALUES (" +
                    "'" + this.cpf + "'," +
                    "'" + this.nome + "'," +
                    "'" + this.categoriaCHN + "'," +
                    "'" + this.email + "'," +
                    "'" + this.nascimento + "'" +
                    ")");
            ResultSet saida = statment.getResultSet();
            return true;
        }catch (SQLException e){
            System.out.println("Erro de SQL");
            System.out.println(e.getMessage());
        }
        return false;
    }

}