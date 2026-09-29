import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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



    static public void main(String[] args) {
        String nombreFichero = IO.readln("Dame el nombre del fichero: ");
        File fichero = new File("datos/"+nombreFichero);
        boolean quiereSobrescribir = false;


        quiereSobrescribir = seQuiereSobrescribirUnFicheroExistente(fichero);


        try (FileWriter fileWriter = new FileWriter(fichero, quiereSobrescribir)) {
            System.out.println("El fichero se ha creado de forma correcta");
        } catch (IOException e) {
            System.out.println("No se ha podido crear/acceder el fichero.");
        }

        int opcion = 0;

        do {
            imprimirMenu("""
                    --------------MENÚ PRINCIPAL-----------
                    |1. Añadir Usuario.                   |
                    |2. Mostrar usuarios introducidos.    |
                    |3. Generar fichero de concordancia.  |
                    |4. Salir                             |
                    ---------------------------------------
                    
                    """);
        } while (opcion < 5 && opcion > 0);

    }

    private static void imprimirMenu(String x) {
        System.out.println(x);
    }
}
