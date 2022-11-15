package br.ufrn;

public class Veiculo {
	private int id = 0;
	private String placa;
	private String marca;
	private String modelo;
	private String cor;
	private String combustivel;
	private float diaria;
	private String status;
	private int locatorio;

	
	
	public Veiculo(String placa, String marca, String modelo, String cor, String combustivel, Float diaria,
			String status, Integer locatorio) {
		super();
		this.placa = placa;
		this.marca = marca;
		this.modelo = modelo;
		this.cor = cor;
		this.combustivel = combustivel;
		this.diaria = diaria;
		this.status = status;
		this.locatorio = locatorio;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return this.id;
	}
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public String getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(String combustivel) {
		this.combustivel = combustivel;
	}

	public Float getDiaria() {
		return diaria;
	}

	public void setDiaria(Float diaria) {
		this.diaria = diaria;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getLocatorio() {
		return locatorio;
	}

	public void setLocatorio(Integer locatorio) {
		this.locatorio = locatorio;
	}
	
	
}
