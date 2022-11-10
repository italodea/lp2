package br.ufrn;

import java.util.Date;

public class Cliente {

    private String cpf;
    private String nome;
    private Date nascimento;
    private String categoriaCHN;


    public Cliente(String cpf, String nome, Date nascimento, String categoriaCHN) {
        this.cpf = cpf;
        this.nome = nome;
        this.nascimento = nascimento;
        this.categoriaCHN = categoriaCHN;
    }

}