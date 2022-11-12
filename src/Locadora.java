import br.ufrn.Cliente;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
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
                    System.out.println("login");
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
        }finally {
            System.out.println("e");
        }
    }

    public static void consultaCliente() throws SQLException {
        System.out.println("Digite parte do nome ou CPF");
        Scanner sc = new Scanner(System.in);

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
}
