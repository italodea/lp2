package br.ufrn;

import br.ufrn.veiculo.Carro;
import br.ufrn.veiculo.Moto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Garagem {

	public Garagem() {

	}


	public void getVeiculos(Connection connection) {
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM vw_locacoes WHERE status='Livre';");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()){
				System.out.println("|--------------------------------|");
				System.out.println("| Tipo:        "+resultSet.getString("tipo"));
				System.out.println("| Placa:       "+resultSet.getString("placa"));
				System.out.println("| Marca:       "+resultSet.getString("marca"));
				System.out.println("| Modelo:      "+resultSet.getString("modelo"));
				System.out.println("| Cor:         "+resultSet.getString("cor"));
				System.out.println("| Combustivel: "+resultSet.getString("combustivel"));
				System.out.println("| Diária       R$:"+resultSet.getString("diaria"));
				System.out.println("| Status:      "+resultSet.getString("status"));
				if(Objects.equals(resultSet.getString("tipo"), "carro")){
					System.out.println("| Potência:    "+resultSet.getString("potencia"));
				}else if(Objects.equals(resultSet.getString("tipo"),"moto")){
					System.out.println("| Cilindradas: "+resultSet.getString("cilindradas"));
				}
				System.out.println("|--------------------------------|");
				System.out.println("|################################|");
			}
		}catch (SQLException e){
			System.out.println("Erro de SQL");
			System.out.println(e.getMessage());
		}
	}

	public Veiculo getVeiculo(Connection connection, String placa, String placaTratada){
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM vw_locacoes WHERE status='Livre' AND placa in ('"+placa+"','"+placaTratada+"');");
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				if(Objects.equals(resultSet.getString("tipo"), "carro")){
					Carro veiculo = new Carro(
							resultSet.getString("placa"),
							resultSet.getString("marca"),
							resultSet.getString("modelo"),
							resultSet.getString("cor"),
							resultSet.getString("combustivel"),
							resultSet.getFloat("diaria"),
							resultSet.getString("status"),
							resultSet.getInt("locatario"),
							resultSet.getInt("potencia")
					);
					veiculo.setId(resultSet.getInt("id"));
					return veiculo;
				}else if(Objects.equals(resultSet.getString("tipo"),"moto")){
					Moto veiculo = new Moto(
							resultSet.getString("placa"),
							resultSet.getString("marca"),
							resultSet.getString("modelo"),
							resultSet.getString("cor"),
							resultSet.getString("combustivel"),
							resultSet.getFloat("diaria"),
							resultSet.getString("status"),
							resultSet.getInt("locatario"),
							resultSet.getInt("cilindradas")
					);
					veiculo.setId(resultSet.getInt("id"));
					return veiculo;
				}
			}
		}catch (SQLException e){
			System.out.println("Erro de SQL");
			System.out.println(e.getMessage());
		}
		return null;
	}


}
