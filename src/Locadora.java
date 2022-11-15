import br.ufrn.Garagem;
import br.ufrn.veiculo.Carro;
import br.ufrn.veiculo.Moto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.sql.*;
public class Locadora {
    public static Connection connection;
    public static void main(String[] args) throws SQLException {
        conectar();
        Statement statement = connection.createStatement();

        while (true){
            Scanner sc = new Scanner(System.in);
            printMenuPrincipal();
            try {
                int opMenuPrincipal = sc.nextInt();
                if(opMenuPrincipal == 1){
                    while(true){
                        printMenuVendedor();
                        try{
                            int opMenuSecundario = sc.nextInt();
                            if(opMenuSecundario == 3){
                                consultaCliente();
                            }
                            if(opMenuSecundario == 0){
                                break;
                            }

                        }catch (InputMismatchException e){
                            System.out.println("Opção inválida!");
                        }
                    }
                } else if (opMenuPrincipal == 2) {
                    while (true){
                        printMenuGerente();
                        try{
                            int opMenuSecundario = sc.nextInt();
                            if(opMenuSecundario == 1){
                                cadastrarAluguel();
                            }
                            if(opMenuSecundario == 3){
                                consultaCliente();
                            }else if(opMenuSecundario == 4){
                                cadastrarVeiculo();
                            }else if(opMenuSecundario == 5){
                                venderVeiculo();
                            }else if(opMenuSecundario == 0){
                                break;
                            }
                        }catch (InputMismatchException e){
                            System.out.println("Opção inválida");
                        }
                    }
                }else{
                    System.exit(0);
                }
            }catch (InputMismatchException e){
                System.out.println("Opção inválida!");
            }
        }
    }

    public static void conectar(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:banco.db");
            System.out.println("Conexão realizada !!!!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Date strToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        }catch (ParseException e){
            return new Date(Long.MIN_VALUE);
        }
    }

    public static void cadastrarAluguel() throws SQLException{
        Scanner sc = new Scanner(System.in);
        Garagem garagem = new Garagem();
        while(true){
            garagem.getVeiculos(connection);
            System.out.println("\n\nDigite a placa do veículo que será alugado:");
            String placa = sc.nextLine();
            String placaTratada = placa.replace("-","");

        }

    }

    public static void consultaCliente() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite parte do nome ou CPF");
        String termo = sc.nextLine();

        PreparedStatement stmt = connection.prepareStatement("select * from clientes where nome like '%"+termo+"%' or cpf like '%"+termo+"%';");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            System.out.println("|------------------------------|");
            String nome = resultSet.getString("nome");
            String cpf = resultSet.getString("cpf");
            String categoriaCNH = resultSet.getString("categoriaCNH");
            String nascimento = resultSet.getString("nascimento");
            String email = resultSet.getString("email");

            System.out.println("|Nome:      "+nome);
            System.out.println("|CPF:       "+cpf);
            System.out.println("|CNH:       "+categoriaCNH);
            System.out.println("|Nascimento:"+nascimento);
            System.out.println("|Contato:   "+email);
            System.out.println("|------------------------------|");
            System.out.println("|##############################|");
        }
        System.out.println("\n\nPresione Enter para continuar...");
        sc.nextLine();
    }

    public static void cadastrarVeiculo() throws SQLException{
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe o tipo:\n 1 - Carro\n 2 - Moto\n");
        try {
            int tipo = sc.nextInt();
            if(tipo == 1){
                System.out.print("Placa: ");
                sc.nextLine();
                String placa = sc.nextLine();

                System.out.print("Marca: ");
                String marca = sc.nextLine();

                System.out.print("Modelo: ");
                String modelo = sc.nextLine();

                System.out.print("Cor: ");
                String cor = sc.nextLine();

                System.out.print("Combustivel: ");
                String combustivel = sc.nextLine();

                System.out.print("Diaria: ");
                Float diaria = sc.nextFloat();

                System.out.print("Potência: ");
                int potencia = sc.nextInt();

                Carro carro = new Carro(
                        placa,
                        marca,
                        modelo,
                        cor,
                        combustivel,
                        diaria,
                        "Livre",
                        0,
                        potencia
                );

                carro.store(connection);
            }else if(tipo == 2){
                System.out.print("Placa: ");
                sc.nextLine();
                String placa = sc.nextLine();

                System.out.print("Marca: ");
                String marca = sc.nextLine();

                System.out.print("Modelo: ");
                String modelo = sc.nextLine();

                System.out.print("Cor: ");
                String cor = sc.nextLine();

                System.out.print("Combustivel: ");
                String combustivel = sc.nextLine();

                System.out.print("Diaria: ");
                Float diaria = sc.nextFloat();

                System.out.print("Potência: ");
                int potencia = sc.nextInt();

                Moto moto = new Moto(
                        placa,
                        marca,
                        modelo,
                        cor,
                        combustivel,
                        diaria,
                        "Livre",
                        0,
                        potencia
                );
                System.out.println("Moto cadastrada");
            }
        }catch (InputMismatchException e){
            System.out.println("Entrada inválida");
        }
    }

    public static void venderVeiculo() throws SQLException {
        while (true){
            Scanner sc = new Scanner(System.in);
            System.out.println("Digite a placa: ");
            String placa        = sc.nextLine();
            String placaTratada = placa.replace("-","");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM vw_locacoes WHERE placa in ('"+placa+"','"+placaTratada+"') LIMIT 1;");
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                System.out.println("|--------------------------------|");
                System.out.println("| Placa:       "+resultSet.getString("placa"));
                System.out.println("| Marca:       "+resultSet.getString("marca"));
                System.out.println("| Modelo:      "+resultSet.getString("modelo"));
                System.out.println("| Cor:         "+resultSet.getString("cor"));
                System.out.println("| Combustivel: "+resultSet.getString("combustivel"));
                System.out.println("| Diária        R$:"+resultSet.getString("diaria"));
                System.out.println("| Status:      "+resultSet.getString("status"));
                if(resultSet.getString("status") != null && resultSet.getInt("status") != 0) {
                    System.out.println("| Locador:     " + resultSet.getString("nome") + " - " + resultSet.getString("cpf"));
                }
                if(Objects.equals(resultSet.getString("tipo"), "carro")){
                    System.out.println("| Potência:    "+resultSet.getString("potencia"));
                }else if(Objects.equals(resultSet.getString("tipo"),"moto")){
                    System.out.println("| Cilindradas: "+resultSet.getString("cilindradas"));
                }
                System.out.println("|--------------------------------|");
                if(Objects.equals(resultSet.getString("status"), "Livre")){
                    System.out.println("Confirmar venda? (S/N)");
                    String op = sc.nextLine();
                    try {
                        if(Objects.equals(op,"S") || Objects.equals(op,"s")){
                            String sql = "";
                            if(Objects.equals(resultSet.getString("tipo"), "carro")){
                                sql = "UPDATE carros SET status='Vendido' WHERE placa = '"+resultSet.getString("placa")+"';";
                                statement.execute();
                            }else if(Objects.equals(resultSet.getString("tipo"),"moto")){
                                sql = "UPDATE motos SET status='Vendido' WHERE placa = '"+resultSet.getString("placa")+"';";
                            }else{
                                break;
                            }
                            statement = connection.prepareStatement(sql);
                            statement.executeUpdate(sql);
                            break;
                        }else{
                            System.out.println("Operação cancelada!");
                        }
                    }catch (SQLException e){
                        System.out.println(e.getMessage());
                    }
                }else if(Objects.equals(resultSet.getString("status"),"Vendido")){
                    System.out.println("Este veiculo já foi vendido e não está mais disponível");
                }else{
                    System.out.println("Este veículo não pode ser vendido agora");
                }
            }else{
                System.out.println("Nenhum veículo encontrado");
            }

            System.out.println("Deseja pesquisar novamente? (S/N)");
            String op = sc.nextLine();
            if(Objects.equals(op, "N") || Objects.equals(op, "n")){
                break;
            }
        }


    }

    public static void printMenuPrincipal(){
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Login de vendedor        |");
        System.out.println("| 2 - Login de gerente         |");
        System.out.println("|                              |");
        System.out.println("| 0 - Sair                     |");
        System.out.println("|------------------------------|");
    }

    public static void printMenuVendedor(){
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Cadastra aluguel         |");
        System.out.println("| 2 - Finalizar aluguel        |");
        System.out.println("| 3 - Consultar cliente        |");
        System.out.println("|                              |");
        System.out.println("| 0 - Sair                     |");
        System.out.println("|------------------------------|");
    }

    public static void printMenuGerente(){
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Cadastra aluguel         |");
        System.out.println("| 2 - Finalizar aluguel        |");
        System.out.println("| 3 - Consultar cliente        |");
        System.out.println("|                              |");
        System.out.println("| 4 - Cadastrar veículo        |");
        System.out.println("| 5 - Vender veículo           |");
        System.out.println("|                              |");
        System.out.println("| 0 - Sair                     |");
        System.out.println("|------------------------------|");
    }
}

//create view vw_locacoes as select * from (select v.id,v.placa,v.marca,v.modelo,v.cor,v.combustivel,v.diaria,v.status,v.locatario,v.cilindradas,null as potencia,'moto' as tipo,c.cpf,c.nome,c.categoriaCNH,c.email,c.nascimento from motos v LEFT JOIN clientes c ON c.id = v.locatario UNION select v.id,v.placa,v.marca,v.modelo,v.cor,v.combustivel,v.diaria,v.status,v.locatario,null,v.potencia as potencia,'carro' as tipo,c.cpf,c.nome,c.categoriaCNH,c.email,c.nascimento from carros v LEFT JOIN clientes c ON c.id = v.locatario);