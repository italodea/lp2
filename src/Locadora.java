import br.ufrn.Cliente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Locadora {
    public static void main(String[] args) {

        ArrayList<Cliente> clientes = new ArrayList<>();


        Cliente cliente = new Cliente("12345678910","Italo",strToDate("17/12/1999"),"A");
        clientes.add(cliente);
        Cliente cliente = new Cliente("12345678911","Italo",strToDate("17/12/1999"),"A");
        clientes.add(cliente);


        while (true){
            Scanner sc = new Scanner(System.in);
            printMenuPrincipal();
            int opMenuPrincipal = sc.nextInt();
            System.out.println(opMenuPrincipal);
            if(opMenuPrincipal == 1){
                System.out.println("login");
            } else if (opMenuPrincipal == 2) {
                System.out.println("login");
            }else{
                break;
            }
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

    public static void printMenuPrincipal(){
        System.out.println("|------------------------------|");
        System.out.println("| 1 - Login de funcionário     |");
        System.out.println("| 2 - Login de cliente         |");
        System.out.println("|                              |");
        System.out.println("| 3 - Sair                     |");
        System.out.println("|------------------------------|");
    }
}
