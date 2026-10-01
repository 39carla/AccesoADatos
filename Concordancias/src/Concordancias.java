import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Concordancias {
    private static boolean seQuiereAnexarAUnFicheroExistente(File fichero) {
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

    private static void imprimirFichero(Scanner lectorFichero) {
        String linea = lectorFichero.nextLine();
        linea = linea.replaceAll("\\s","|");
        System.out.println("|" + linea + "|");
    }

    private static boolean darMensajeDeErrorYTerminar(String mensajeError) throws InterruptedException {
        System.out.println(mensajeError);
        Thread.sleep(1000);
        return false;
    }

    private static int conseguirNumeroDeUsuario(int numeroUsuario, boolean quiereAnexarEnElArchivo, Scanner lectorFichero) {
        if (numeroUsuario == 0 && quiereAnexarEnElArchivo) {
            String siguienteUsuario = "";
            while (lectorFichero.hasNextLine()) {
                siguienteUsuario = lectorFichero.next();
                lectorFichero.nextLine();
            }

            int exponenteDeDiez = 0;
            for (int cont = siguienteUsuario.length() - 1; cont >= 1; cont--) {
                numeroUsuario = numeroUsuario + (siguienteUsuario.charAt(cont) - 48) * Math.powExact(10,exponenteDeDiez);
                exponenteDeDiez++;
            }

            numeroUsuario++;
        } else if (numeroUsuario == 0) {
            numeroUsuario = 100;
        } else {
            numeroUsuario++;
        }
        return numeroUsuario;
    }

    private static void annadirUsuarioAlFichero(int numeroUsuarioActual, boolean quiereSobrescribir, Scanner lectorFichero, FileWriter fileWriter) throws IOException {
        numeroUsuarioActual = conseguirNumeroDeUsuario(numeroUsuarioActual, quiereSobrescribir, lectorFichero);
        StringBuilder lineaUsuarioNuevo = new StringBuilder("U" + numeroUsuarioActual + " ");

        char quierePonerMasHobbies = 's';
        while (quierePonerMasHobbies == 's') {
            String hobby = IO.readln("¿Qué hobby tiene el usuario?\n").toUpperCase();
            lineaUsuarioNuevo.append(hobby).append(" ");
            quierePonerMasHobbies = IO.readln("Escribe \"s\" para añadir más hobbies o cualquier otra letra para salir\n").charAt(0);
        }

        lineaUsuarioNuevo.deleteCharAt(lineaUsuarioNuevo.length() - 1);
        lineaUsuarioNuevo.append("\n");

        fileWriter.write(lineaUsuarioNuevo.toString());
        fileWriter.flush();

        System.out.println("Se ha añadido el usuario U" + numeroUsuarioActual);
    }

    private static void convertirFicheroAArrayBidimensional(Scanner lectorFichero, ArrayList<String[]> usuarios) {
        while (lectorFichero.hasNextLine()) {
            usuarios.add(lectorFichero.nextLine().split(" "));
        }
    }

    private static void conseguirUsuariosConConcordancia(ArrayList<String[]> usuarios, ArrayList<String> usuariosConConcordancia) {
        for (int usuario1 = 0; usuario1 < usuarios.size(); usuario1++) {
            String idUsuario1 = usuarios.get(usuario1)[0];
            for (int usuario2 = usuario1; usuario2 < usuarios.size(); usuario2++) {
                String idUsuario2 = usuarios.get(usuario2)[0];
                StringBuilder annadirUsuariosConcordancia = new StringBuilder();

                int concordancias = 0;
                for (int hobbyUsuario1 = 1; hobbyUsuario1 < usuarios.get(usuario1).length; hobbyUsuario1++) {
                    String hobbyActualUsuario1 = usuarios.get(usuario1)[hobbyUsuario1];

                    for (int hobbyUsuario2 = 1; hobbyUsuario2 < usuarios.get(usuario2).length; hobbyUsuario2++) {
                        String hobbyActualUsuario2 = usuarios.get(usuario2)[hobbyUsuario2];

                        if (hobbyActualUsuario1.equals(hobbyActualUsuario2)
                                                &&
                                     idUsuario1.equals(idUsuario2)
                        ) {
                            concordancias++;
                            if (concordancias == 1) {
                                annadirUsuariosConcordancia.append(usuarios.get(usuario1)[0]);
                                annadirUsuariosConcordancia.append(" ");
                                annadirUsuariosConcordancia.append(usuarios.get(usuario2)[0]);
                            }

                            annadirUsuariosConcordancia.append(" ");
                            annadirUsuariosConcordancia.append(usuarios.get(usuario1)[hobbyUsuario1]);

                            System.out.println(annadirUsuariosConcordancia);
                        }

                    }
                    if (!usuariosConConcordancia.isEmpty())
                        usuariosConConcordancia.add(annadirUsuariosConcordancia.toString());
                }
            }
        }
    }

    private static void crearFicheroConcordancia(Scanner lectorFichero) throws IOException {
        ArrayList<String[]> usuarios = new ArrayList<>();
        ArrayList<String> usuariosConConcordancia = new ArrayList<>();
        convertirFicheroAArrayBidimensional(lectorFichero, usuarios);

        conseguirUsuariosConConcordancia(usuarios, usuariosConConcordancia);


        if (!usuariosConConcordancia.isEmpty()) {
            FileWriter ficheroConcordancias = new FileWriter("datos/concordancias.txt");

            for (int cont = 0; cont < usuariosConConcordancia.size(); cont++) {
                ficheroConcordancias.write(usuariosConConcordancia.get(cont));
                ficheroConcordancias.write("\n");
            }
            ficheroConcordancias.flush();
        }
    }


    static public void main(String[] args) throws InterruptedException {
        String nombreFichero = IO.readln("Dame el nombre del fichero: ");
        File fichero = new File("datos/" + nombreFichero);
        boolean quiereAnexarEnElArchivo = false;

        quiereAnexarEnElArchivo = seQuiereAnexarAUnFicheroExistente(fichero);

        try (FileWriter fileWriter = new FileWriter(fichero, quiereAnexarEnElArchivo)) {
            if (quiereAnexarEnElArchivo) fileWriter.write("\n");
            System.out.println("El fichero se ha creado/accedido de forma correcta");

            int opcion = 0;
            boolean seSigue = true;

            do {
                imprimirMenu();

                try {
                    int numeroUsuarioActual = 0;
                    opcion = Integer.parseInt(IO.readln("¿Qué quieres hacer?\n"));

                    Scanner lectorFichero = new Scanner (new BufferedReader(new FileReader(fichero)));

                    if        (opcion == 1) {
                        annadirUsuarioAlFichero(numeroUsuarioActual, quiereAnexarEnElArchivo, lectorFichero, fileWriter);
                    } else if (opcion == 2) {
                        while (lectorFichero.hasNextLine()) {
                            imprimirFichero(lectorFichero);
                        }
                    } else if (opcion == 3) {
                        crearFicheroConcordancia(lectorFichero);
                    } else if (opcion == 4) {
                        seSigue = false;
                    }
                } catch (NumberFormatException e) {
                    seSigue = darMensajeDeErrorYTerminar("No se ha puesto un número válido, volviendo a intentar...");
                } catch (FileNotFoundException e) {
                    seSigue = darMensajeDeErrorYTerminar("No se ha podido encontrar el fichero, terminando el programa...");
                } catch (IOException e) {
                    seSigue = darMensajeDeErrorYTerminar("No se ha podido leer el fichero, terminando el programa...");
                } catch (Exception e) {
                    seSigue = darMensajeDeErrorYTerminar("Ha surgido un error inesperado, terminando el programa...");
                }
            } while (seSigue);

        } catch (IOException e) {
            System.out.println("No se ha podido crear/acceder al fichero.");
        }
    }
}
