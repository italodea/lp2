package br.ufrn.veiculo;

import br.ufrn.Veiculo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Moto extends Veiculo {
	private int cilindradas;

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
	public Moto(String placa, String marca, String modelo, String cor, String combustivel, Float diaria, String status,
			Integer locatorio,int cilindradas) {
		super(placa, marca, modelo, cor, combustivel, diaria, status, locatorio);
		this.cilindradas = cilindradas;
		// TODO Auto-generated constructor stub
	}

	public int getCilindradas() {
		return cilindradas;
	}

	public void setCilindradas(int cilindradas) {
		this.cilindradas = cilindradas;
	}

	public boolean store(Connection connection){
		try{
			Statement statment = connection.createStatement();
			statment.execute("INSERT INTO motos(placa, marca, modelo, cor, combustivel, diaria, status, locatario, cilindradas) VALUES (" +
					"'" + this.getPlaca() + "'," +
					"'" + this.getMarca() + "'," +
					"'" + this.getModelo() + "'," +
					"'" + this.getCor() + "'," +
					"'" + this.getCombustivel() + "'," +
					"'" + this.getDiaria() + "'," +
					"'" + this.getStatus() + "'," +
					"'" + this.getLocatorio() + "'," +
					"'" + this.getCilindradas() + "'" +
					")");
			ResultSet saida = statment.getResultSet();
			return true;
		}catch (SQLException e){
			System.out.println("Erro de SQL");
			System.out.println(e.getMessage());
			return false;
		}
		finally {
			return false;
		}
	}
}
