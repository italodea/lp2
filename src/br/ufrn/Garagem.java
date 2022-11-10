package br.ufrn;

import java.util.ArrayList;

public class Garagem {
	private Integer vagas;
	private ArrayList<Veiculo> veiculos;
	/**
	 * @param vagas
	 */
	public Garagem(Integer vagas) {
		super();
		this.vagas = vagas;
	}
	public Integer getVagas() {
		return vagas;
	}
	public void setVagas(Integer vagas) {
		this.vagas = vagas;
	}
	public ArrayList<Veiculo> getVeiculos() {
		return veiculos;
	}
	public void setVeiculos(ArrayList<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
	
	
	

}
