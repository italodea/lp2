import br.ufrn.Aluguel;
import br.ufrn.Cliente;
import br.ufrn.Garagem;
import br.ufrn.Veiculo;
import br.ufrn.veiculo.Carro;
import br.ufrn.veiculo.Moto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Locadora {
    public static Connection connection;

    public static void main(String[] args) throws SQLException {
        conectar();

        while (true) {
            Scanner sc = new Scanner(System.in);
            printMenuPrincipal();
            try {
                int opMenuPrincipal = sc.nextInt();
                if (opMenuPrincipal == 1) {
                    while (true) {
                        printMenuVendedor();
                        try {
                            int opMenuSecundario = sc.nextInt();
                            if(opMenuSecundario == 1){
                                cadastrarAluguel();
                            } else if (opMenuSecundario == 2){
                                finalizarAluguel();
                            } else if (opMenuSecundario == 3) {
                                consultaCliente();
                            } else if (opMenuSecundario == 0) {
                                break;
                            }

                        } catch (InputMismatchException e) {
                            System.out.println("Opção inválida!");
                        }
                    }
                } else if (opMenuPrincipal == 2) {
                    while (true) {
                        printMenuGerente();
                        try {
                            int opMenuSecundario = sc.nextInt();
                            if (opMenuSecundario == 1) {
                                cadastrarAluguel();
                            }else if (opMenuSecundario == 2){
                                finalizarAluguel();
                            } else if (opMenuSecundario == 3) {
                                consultaCliente();
                            } else if (opMenuSecundario == 4) {
                                cadastrarVeiculo();
                            } else if (opMenuSecundario == 5) {
                                venderVeiculo();
                            } else if (opMenuSecundario == 0) {
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Opção inválida");
                        }
                    }
                } else {
                    System.exit(0);
                }
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida!");
            }
        }
    }


    public static void conectar() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:banco.db");
            System.out.println("Conexão realizada !!!!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Date strToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return new Date(Long.MIN_VALUE);
        }
    }

    public static Veiculo escolherVeiculo() {
        Scanner sc = new Scanner(System.in);
        Garagem garagem = new Garagem();
        garagem.getVeiculos(connection);
        System.out.println("\n\nDigite a placa do veículo que será alugado:");
        String placa = sc.nextLine();
        String placaTratada = placa.replace("-", "");
        Veiculo veiculo = garagem.getVeiculo(connection, placa, placaTratada);
        return veiculo;
    }

    public static ResultSet escolherCliente() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("CPF do cliente: ");
        String cpf = sc.nextLine();

        while (true) {
            PreparedStatement statement = connection.prepareStatement("select * from clientes where cpf = '" + cpf + "';");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result;
            } else {
                System.out.println("Cliente não cadastrado.");
                System.out.print("\n\nNome: ");
                String nome = sc.nextLine();

                System.out.print("Nascimento: ");
                String nascimento = sc.nextLine();
                Date nascimentoTratado = strToDate(nascimento);
                System.out.print("Categoria(s) da CNH: ");
                String categoriaCHN = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();

                Cliente cliente = new Cliente(
                        cpf,
                        nome,
                        nascimento,
                        categoriaCHN,
                        email
                );
                cliente.store(connection);
            }
        }
    }

    public static void registrarAluguelCarro(Connection connection, Veiculo veiculo, ResultSet result) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Digite a data de retirada (01/01/1970) :");
                String dataS = sc.nextLine();
                Date dataSaida = strToDate(dataS);
                LocalDate dataSaidaTratada = LocalDate.ofInstant(dataSaida.toInstant(), ZoneId.systemDefault());
                System.out.print("Digite a data de entrega (01/01/1970) :");
                String dataD = sc.nextLine();
                Date dataDevolucao = strToDate(dataD);
                LocalDate dataDevolucaoTratada = LocalDate.ofInstant(dataDevolucao.toInstant(), ZoneId.systemDefault());

                long dias = getDateDiff(dataSaida,dataDevolucao,TimeUnit.DAYS);
                if (dias > 0) {
                    System.out.println("|------------------|");
                    System.out.println("| Diária:  R$" + veiculo.getDiaria());
                    System.out.println("| Dias:    " + dias);
                    System.out.println("|");
                    System.out.println("Total:     R$" + veiculo.getDiaria() * dias);
                    System.out.println("|------------------|");
                    System.out.println("\nConfirmar: (S/N)");
                    String op = sc.nextLine();
                    if (Objects.equals(op, "S") || Objects.equals(op, "s")) {
                        Aluguel aluguel = new Aluguel(1, result.getInt("id"), veiculo.getId(), dataS, dataD,"C");
                        aluguel.store(connection);

                        String sql = "UPDATE carros SET status='Alugado',locatario="+result.getInt("id")+" WHERE placa = '" + veiculo.getPlaca() + "';";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.execute();
                        System.out.println("Carro foi alugado!");
                        break;
                    } else {
                        System.out.println("Abortado!");
                        break;
                    }
                } else {
                    System.out.println("Intervalo inválido!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL");
            System.out.println(e.getMessage());
        }
    }

    public static void registrarAluguelMoto(Connection connection, Veiculo veiculo, ResultSet result) {
        try {
            while (true) {
                Scanner sc = new Scanner(System.in);
                System.out.print("Digite a data de retirada (01/01/1970) :");
                String dataS = sc.nextLine();
                Date dataSaida = strToDate(dataS);
                LocalDate dataSaidaTratada = LocalDate.ofInstant(dataSaida.toInstant(), ZoneId.systemDefault());
                System.out.print("Digite a data de entrega (01/01/1970) :");
                String dataD = sc.nextLine();
                Date dataDevolucao = strToDate(dataD);
                LocalDate dataDevolucaoTratada = LocalDate.ofInstant(dataDevolucao.toInstant(), ZoneId.systemDefault());

                long dias = getDateDiff(dataSaida,dataDevolucao,TimeUnit.DAYS);
                if (dias > 0) {
                    System.out.println("|------------------|");
                    System.out.println("| Diária:  R$" + veiculo.getDiaria());
                    System.out.println("| Dias:    " + dias);
                    System.out.println("|");
                    System.out.println("Total:     R$" + veiculo.getDiaria() * dias);
                    System.out.println("|------------------|");
                    System.out.println("\nConfirmar: (S/N)");
                    String op = sc.nextLine();
                    if (Objects.equals(op, "S") || Objects.equals(op, "s")) {
                        Aluguel aluguel = new Aluguel(1, result.getInt("id"), veiculo.getId(), dataS, dataD,"M");
                        aluguel.store(connection);

                        String sql = "UPDATE motos SET status='Alugado', locatario="+result.getInt("id")+" WHERE placa = '" + veiculo.getPlaca() + "';";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.execute();
                        System.out.println("Moto foi alugada!");
                        break;
                    } else {
                        System.out.println("Abortado!");
                        break;
                    }
                } else {
                    System.out.println("Intervalo inválido!");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL");
            System.out.println(e.getMessage());
        }
    }

    public static void cadastrarAluguel() {
        try {
            Veiculo veiculo = escolherVeiculo();
            while (veiculo == null) {
                System.out.println("Veículo não disponível!");
                veiculo = escolherVeiculo();
            }

            ResultSet result = escolherCliente();

            int pass = 0;
            Date nascimento = strToDate(result.getString("nascimento"));
            Date hoje = new Date();

            LocalDate nascimentoLocal = LocalDate.ofInstant(nascimento.toInstant(), ZoneId.systemDefault());
            LocalDate hojeLocal = LocalDate.ofInstant(hoje.toInstant(), ZoneId.systemDefault());

            if (Period.between(nascimentoLocal, hojeLocal).getYears() > 19) {
                if (veiculo instanceof Carro) {
                    if (result.getString("categoriaCNH").toLowerCase().contains("B".toLowerCase())) {
                        pass = 1;
                    } else {
                        System.out.println("Nescessário ter habilitação na categoria B para dirigir carros");
                    }
                } else if (veiculo instanceof Moto) {
                    if (result.getString("categoriaCNH").toLowerCase().contains("A".toLowerCase())) {
                        pass = 1;
                    } else {
                        System.out.println("Nescessário ter habilitação na categoria A para dirigir motos");
                    }
                }
            } else {
                System.out.println("Cliente precisa ter pelo menos 20 anos de idade para alugar");
            }

            if (pass == 1) {
                if (veiculo instanceof Carro) {
                    registrarAluguelCarro(connection, veiculo, result);
                } else if (veiculo instanceof Moto) {
                    registrarAluguelMoto(connection, veiculo, result);
                }
            } else {
                System.out.println("Inicie o processo novamente!");
            }
        } catch (SQLException e) {
            System.out.println("Erro de SQL");
            System.out.println(e.getMessage());
        }
    }

    public static void finalizarAluguel() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Digite a placa do veículo:");
        String placa = sc.nextLine();
        String placaTratada = placa.replace("-","");

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vw_historico_alugueis WHERE status = 'Alugado' AND placa in ('" + placa + "','" + placaTratada + "') LIMIT 1;");
        ResultSet result = statement.executeQuery();
        if(result.next()){
            String sql = "";
            if(Objects.equals(result.getString("tipo_veiculo"), "C")){
                sql = "UPDATE carros SET status='Livre',locatario=0 WHERE id="+result.getString("veiculo_id")+";";
            }else{
                sql = "UPDATE motos SET status='Livre',locatario=0 WHERE id="+result.getString("veiculo_id")+";";
            }
            statement = connection.prepareStatement(sql);
            statement.execute();

            System.out.println("Veículo devolvido, pressione ENTER para continuar!");
            sc.nextLine();
        }else{
            System.out.println("Nenhum aluguel registrado para este veículo!");
        }
    }
    public static void consultaCliente() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite parte do nome ou CPF");
        String termo = sc.nextLine();

        PreparedStatement stmt = connection.prepareStatement("select * from clientes where nome like '%" + termo + "%' or cpf like '%" + termo + "%';");
        ResultSet resultSet = stmt.executeQuery();
        while (resultSet.next()) {
            System.out.println("|------------------------------|");
            String nome = resultSet.getString("nome");
            String cpf = resultSet.getString("cpf");
            String categoriaCNH = resultSet.getString("categoriaCNH");
            String nascimento = resultSet.getString("nascimento");
            String email = resultSet.getString("email");

            System.out.println("|Nome:      " + nome);
            System.out.println("|CPF:       " + cpf);
            System.out.println("|CNH:       " + categoriaCNH);
            System.out.println("|Nascimento:" + nascimento);
            System.out.println("|Contato:   " + email);
            System.out.println("|------------------------------|");

            System.out.println("||----HISTÓRICO DE ALUGUÉIS-----||");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM vw_historico_alugueis WHERE cpf = '"+cpf+"';");
            ResultSet result = statement.executeQuery();
            System.out.println("|MARCA MODELO PLACA | DE : ATÉ");
            while(result.next()){
                System.out.println("| "+
                        result.getString("marca")+
                        " "+
                        result.getString("modelo")+
                        " "+
                        result.getString("placa")+
                        " | "+
                        result.getString("dataSaida")+
                        " : "+
                        result.getString("dataRetorno")
                );
            }

            System.out.println("|##############################|");
        }


        System.out.println("\n\nPresione Enter para continuar...");
        sc.nextLine();
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static void cadastrarVeiculo() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Informe o tipo:\n 1 - Carro\n 2 - Moto\n");
        try {
            int tipo = sc.nextInt();
            if (tipo == 1) {
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
                System.out.println("Carro cadastrada");
            } else if (tipo == 2) {
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

                System.out.print("Cilindradas: ");
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
                moto.store(connection);
                System.out.println("Moto cadastrada");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida");
        }
    }

    public static void venderVeiculo() throws SQLException {
        while (true) {
            Scanner sc = new Scanner(System.in);

            Garagem g = new Garagem();
            g.getVeiculos(connection);
            System.out.println("Digite a placa: ");
            String placa = sc.nextLine();
            String placaTratada = placa.replace("-", "");

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM vw_locacoes WHERE placa in ('" + placa + "','" + placaTratada + "') LIMIT 1;");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("|--------------------------------|");
                System.out.println("| Placa:       " + resultSet.getString("placa"));
                System.out.println("| Marca:       " + resultSet.getString("marca"));
                System.out.println("| Modelo:      " + resultSet.getString("modelo"));
                System.out.println("| Cor:         " + resultSet.getString("cor"));
                System.out.println("| Combustivel: " + resultSet.getString("combustivel"));
                System.out.println("| Diária        R$:" + resultSet.getString("diaria"));
                System.out.println("| Status:      " + resultSet.getString("status"));
                if (resultSet.getString("status") != null && resultSet.getInt("status") != 0) {
                    System.out.println("| Locador:     " + resultSet.getString("nome") + " - " + resultSet.getString("cpf"));
                }
                if (Objects.equals(resultSet.getString("tipo"), "carro")) {
                    System.out.println("| Potência:    " + resultSet.getString("potencia"));
                } else if (Objects.equals(resultSet.getString("tipo"), "moto")) {
                    System.out.println("| Cilindradas: " + resultSet.getString("cilindradas"));
                }
                System.out.println("|--------------------------------|");
                if (Objects.equals(resultSet.getString("status"), "Livre")) {
                    System.out.println("Confirmar venda? (S/N)");
                    String op = sc.nextLine();
                    try {
                        if (Objects.equals(op, "S") || Objects.equals(op, "s")) {
                            String sql = "";
                            if (Objects.equals(resultSet.getString("tipo"), "carro")) {
                                sql = "UPDATE carros SET status='Vendido' WHERE placa = '" + resultSet.getString("placa") + "';";
                            } else if (Objects.equals(resultSet.getString("tipo"), "moto")) {
                                sql = "UPDATE motos SET status='Vendido' WHERE placa = '" + resultSet.getString("placa") + "';";
                            } else {
                                break;
                            }
                            statement = connection.prepareStatement(sql);
                            statement.execute();
//                            statement.executeUpdate(sql);
                            break;
                        } else {
                            System.out.println("Operação cancelada!");
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else if (Objects.equals(resultSet.getString("status"), "Vendido")) {
                    System.out.println("Este veiculo já foi vendido e não está mais disponível");
                } else {
                    System.out.println("Este veículo não pode ser vendido agora");
                }
            } else {
                System.out.println("Nenhum veículo encontrado");
            }

            System.out.println("Deseja pesquisar novamente? (S/N)");
            String op = sc.nextLine();
            if (Objects.equals(op, "N") || Objects.equals(op, "n")) {
                break;
            }
        }


    }

    public static void printMenuPrincipal() {
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Login de vendedor        |");
        System.out.println("| 2 - Login de gerente         |");
        System.out.println("|                              |");
        System.out.println("| 0 - Sair                     |");
        System.out.println("|------------------------------|");
    }

    public static void printMenuVendedor() {
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Cadastra aluguel         |");
        System.out.println("| 2 - Finalizar aluguel        |");
        System.out.println("| 3 - Consultar cliente        |");
        System.out.println("|                              |");
        System.out.println("| 0 - Sair                     |");
        System.out.println("|------------------------------|");
    }

    public static void printMenuGerente() {
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