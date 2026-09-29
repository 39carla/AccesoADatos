import java.io.*;
import java.util.Scanner;

public class Concordancias {

    private static boolean seQuiereSobrescribirUnFicheroExistente(File fichero) {
        if (fichero.exists()) {
            System.out.println("El fichero que me has dado ya existe, ¿qué quieres hacer?");
            boolean haFuncionado = false;

            while (!haFuncionado) {
                try {
                    int opcion = Integer.parseInt(IO.readln("1. Sobrescribir el archivo\nCualquier otro número. Escribir en el archivo\n"));

                    if (opcion != 1) return true;

                    haFuncionado = true;
                } catch (Exception e) {
                    System.out.println("No has puesto un número válido en el menú");
                }
            }
        }
        return false;
    }

    private static void imprimirMenu() {
        System.out.println("""
                    --------------MENÚ PRINCIPAL-----------
                    |1. Añadir Usuario.                   |
                    |2. Mostrar usuarios introducidos.    |
                    |3. Generar fichero de concordancia.  |
                    |4. Salir                             |
                    ---------------------------------------
                    
                    """);
    }

    private static boolean darMensajeDeErrorYTerminar(String x) throws InterruptedException {
        System.out.println(x);
        Thread.sleep(1000);
        return false;
    }


    static public void main(String[] args) throws InterruptedException {
        String nombreFichero = IO.readln("Dame el nombre del fichero: ");
        File fichero = new File("datos/" + nombreFichero);
        boolean quiereSobrescribir = false;


        quiereSobrescribir = seQuiereSobrescribirUnFicheroExistente(fichero);


        try (FileWriter fileWriter = new FileWriter(fichero, quiereSobrescribir)) {
            System.out.println("El fichero se ha creado/accedido de forma correcta");
        } catch (IOException e) {
            System.out.println("No se ha podido crear/acceder al fichero.");
        }

        int opcion = 0;
        boolean seSigue = true;

        do {
            imprimirMenu();

            try {
                opcion = Integer.parseInt(IO.readln("¿Qué quieres hacer?\n"));

                Scanner lectorFichero = new Scanner (new BufferedReader(new FileReader(fichero)));

                if        (opcion == 1) {
                    String usuario = IO.readln("Dame un usuario.(Tiene que estar formateado como U)");
                    while (lectorFichero.hasNext()) {
                        String siguienteUsuario;

                    }
                } else if (opcion == 2) {

                } else if (opcion == 3) {

                } else if (opcion == 4) {
                    seSigue = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("No se ha puesto un número válido, volviendo a intentar...");
                Thread.sleep(1000);
            } catch (FileNotFoundException e) {
                seSigue = darMensajeDeErrorYTerminar("No se ha podido encontrar el fichero, terminando el programa...");
            } catch (IOException e) {
                seSigue = darMensajeDeErrorYTerminar("No se ha podido leer el fichero, terminando el programa...");
            }
        } while (seSigue);

    }
}
