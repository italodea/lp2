package br.ufrn.veiculo;

import br.ufrn.Veiculo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Carro extends Veiculo {
	private int potencia;

	/**
	 * @param placa
	 * @param marca
	 * @param modelo
	 * @param cor
	 * @param combustivel
	 * @param diaria
	 * @param status
	 * @param locatorio
	 */
	public Carro(String placa, String marca, String modelo, String cor, String combustivel, Float diaria, String status,
			Integer locatorio,int potencia) {
		super(placa, marca, modelo, cor, combustivel, diaria, status, locatorio);
		this.potencia = potencia;
	}



	public int getPotencia() {
		return potencia;
	}

	public void setPotencia(int potencia) {
		this.potencia = potencia;
	}

	public boolean store(Connection connection){
		try{
			Statement statment = connection.createStatement();
			statment.execute("INSERT INTO carros(placa, marca, modelo, cor, combustivel, diaria, status, locatario, potencia) VALUES (" +
					"'" + this.getPlaca() + "'," +
					"'" + this.getMarca() + "'," +
					"'" + this.getModelo() + "'," +
					"'" + this.getCor() + "'," +
					"'" + this.getCombustivel() + "'," +
					"'" + this.getDiaria() + "'," +
					"'" + this.getStatus() + "'," +
					"'" + this.getLocatorio() + "'," +
					"'" + this.getPotencia() + "'" +
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
