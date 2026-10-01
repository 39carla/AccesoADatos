import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Concordancias {
    //En este método se le pregunta al usuario si quiere anexar información a un fichero ya existente
    private static boolean seQuiereAnexarAUnFicheroExistente(File fichero) {
        if (fichero.exists()) {
            System.out.println("El fichero que me has dado ya existe, ¿qué quieres hacer?");
            //Esta variable existe por si el usuario no pone un número cuando se lo pedimos
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

    //Método que muestra por pantalla el menú
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

    //Método que muestra por pantalla el fichero entero
    private static void imprimirFichero(Scanner lectorFichero) {
        while (lectorFichero.hasNextLine()) {
            String linea = lectorFichero.nextLine();
            linea = linea.replaceAll("\\s","|");
            System.out.println("|" + linea + "|");
        }
    }

    //Método que muestra un mensaje de error y termina el programa
    private static boolean darMensajeDeErrorYTerminar(String mensajeError) throws InterruptedException {
        System.out.println(mensajeError);
        Thread.sleep(1000);
        return false;
    }

    //Método para conseguir el id del usuario que se va a añadir.
    private static int conseguirNumeroDeUsuario(int numeroUsuario, boolean quiereAnexarEnElArchivo, Scanner lectorFichero) {
        //Esto es por si es un fichero que ya existe y es el primer id que añadimos
        if (numeroUsuario == 0 && quiereAnexarEnElArchivo) {
            String siguienteUsuario = "";
            while (lectorFichero.hasNextLine()) {
                siguienteUsuario = lectorFichero.next();
                lectorFichero.nextLine();
            }

            int exponenteDeDiez = 0;
            //Vamos mirando carácter por carácter el número del id y lo multiplicamos por la potencia de 10 correcta.
            for (int cont = siguienteUsuario.length() - 1; cont >= 1; cont--) {
                //Aqui se le resta 48 al numero del usuario para sacar el número de verdad porque sacamos un número char.
                numeroUsuario = numeroUsuario + (siguienteUsuario.charAt(cont) - 48) * Math.powExact(10,exponenteDeDiez);
                exponenteDeDiez++;
            }

            numeroUsuario++;
        } else if (numeroUsuario == 0) {
            //Esta opción es por si es un nuevo documento
            numeroUsuario = 100;
        } else {
            //Esta opción es cuando ya sabemos cuál es el último número del id.
            numeroUsuario++;
        }
        return numeroUsuario;
    }

    //Método para añadir un usuario al fichero que utilizamos, este método llama a conseguirNumerodDeUsuario
    private static void annadirUsuarioAlFichero(int numeroUsuarioActual, boolean quiereSobrescribir, Scanner lectorFichero, FileWriter fileWriter) throws IOException {
        numeroUsuarioActual = conseguirNumeroDeUsuario(numeroUsuarioActual, quiereSobrescribir, lectorFichero);
        StringBuilder lineaUsuarioNuevo = new StringBuilder("U" + numeroUsuarioActual + " ");

        String hobby = IO.readln("¿Qué hobbies tiene el usuario? (Ponlos separados por un espacio)\n").toUpperCase();
        lineaUsuarioNuevo.append(hobby).append("\n");

        fileWriter.write(lineaUsuarioNuevo.toString());
        fileWriter.flush();

        System.out.println("Se ha añadido el usuario U" + numeroUsuarioActual);
    }

    //Método para convertir cada línea del fichero a un array de Strings que se guardan en un ArrayList
    private static void convertirFicheroAArrayBidimensional(Scanner lectorFichero, ArrayList<String[]> usuarios) {
        while (lectorFichero.hasNextLine()) {
            usuarios.add(lectorFichero.nextLine().split(" "));
        }
    }

    //Método que busca las concordancias dentro del ArrayList que se genera a partir del fichero
    private static void conseguirUsuariosConConcordancia(ArrayList<String[]> usuarios, ArrayList<String> usuariosConConcordancia) {
        /*
         * En este método se va a guardar toda la información de las concordancias en un ArrayList de Strings que nos pasa otro método
         * Aquí se hacen cuatros bucles para encontrar cada concordancia
         * En el primero y el segundo son los que van pasando de usuario a usuario mientras que el tercero y el cuarto van mirando los hobbies de cada uno
         * Dentro del segundo bucle creamos dos variables importantes, annadirUsuariosConcordancia que es el StringBuilder en donde se guarda la información que luego se pondrá en el fichero
         * Y también se crea la variable concordancias que es principalmente para mirar si es la primera concordancia y añadir los usuarios al StringBuilder
         * Al final del bucle del segundo usuario es cuando miramos si el StringBuilder tiene algo dentro, si tiene significa que ha encontrado una concordancia y se añade al ArrayList
         */
        //Bucle de búsqueda de usuarios
        for (int usuario1 = 0; usuario1 < usuarios.size() - 1; usuario1++) {
            String idUsuario1 = usuarios.get(usuario1)[0];
            //Bucle de búsqueda del segundo usuario
            for (int usuario2 = usuario1 + 1; usuario2 < usuarios.size(); usuario2++) {
                String idUsuario2 = usuarios.get(usuario2)[0];
                StringBuilder annadirUsuariosConcordancia = new StringBuilder();

                int concordancias = 0;

                //Bucle de búsqueda del hobby del primer usuario
                for (int hobbyUsuario1 = 1; hobbyUsuario1 < usuarios.get(usuario1).length; hobbyUsuario1++) {
                    String hobbyActualUsuario1 = usuarios.get(usuario1)[hobbyUsuario1];


                    //Bucle de búsqueda del hobby del segundo usuario
                    for (int hobbyUsuario2 = 1; hobbyUsuario2 < usuarios.get(usuario2).length; hobbyUsuario2++) {
                        String hobbyActualUsuario2 = usuarios.get(usuario2)[hobbyUsuario2];

                        if (hobbyActualUsuario1.equals(hobbyActualUsuario2)
                                                &&
                                     !idUsuario1.equals(idUsuario2)
                        ) {
                            concordancias++;
                            if (concordancias == 1) {
                                annadirUsuariosConcordancia.append(usuarios.get(usuario1)[0]);
                                annadirUsuariosConcordancia.append(" ");
                                annadirUsuariosConcordancia.append(usuarios.get(usuario2)[0]);
                            }

                            annadirUsuariosConcordancia.append(" ");
                            annadirUsuariosConcordancia.append(usuarios.get(usuario1)[hobbyUsuario1]);
                        }

                    }
                }

                if (!annadirUsuariosConcordancia.isEmpty()) {
                    usuariosConConcordancia.add(annadirUsuariosConcordancia.toString());
                }
            }
        }
    }

    //Método que ordena un ArrayList de String que le pases usando BubbleSort
    private static void ordenarArray(ArrayList<String> arrayDeStrings) {
        for (int cont = 0; cont < arrayDeStrings.size(); cont++) {
            int posicionesARecorrer = arrayDeStrings.size() - cont - 1;
            for (int i = 0; i < posicionesARecorrer; i++) {
                if (arrayDeStrings.get(cont).length() > arrayDeStrings.get(cont +1).length()) {
                    String stringApoyo = arrayDeStrings.get(cont);
                    arrayDeStrings.set(cont, arrayDeStrings.get(cont+1));
                    arrayDeStrings.set(cont + 1,stringApoyo);
                }
            }
        }
    }


    //Método donde se crea el archivo de concordancias
    private static void crearFicheroConcordancia(Scanner lectorFichero) throws IOException {
        ArrayList<String[]> usuarios = new ArrayList<>();
        ArrayList<String> usuariosConConcordancia = new ArrayList<>();
        convertirFicheroAArrayBidimensional(lectorFichero, usuarios);

        conseguirUsuariosConConcordancia(usuarios, usuariosConConcordancia);


        if (!usuariosConConcordancia.isEmpty()) {
            ordenarArray(usuariosConConcordancia);
            FileWriter ficheroConcordancias = new FileWriter("datos/concordancias.txt");

            for (String linea : usuariosConConcordancia) {
                ficheroConcordancias.write(linea);
                ficheroConcordancias.write("\n");
            }
            ficheroConcordancias.flush();

            System.out.printf("Se han añadido %d concordancias al fichero\n",usuariosConConcordancia.size());
        } else {
            System.out.println("No se pudo encontrar ninguna concordancia. No se ha creado el fichero");
        }
    }




    static public void main(String[] args) throws InterruptedException {
        String nombreFichero = IO.readln("Dame el nombre del fichero: ");
        File fichero = new File("datos/" + nombreFichero);
        boolean quiereAnexarEnElArchivo;


        //En esta variable se llama al método para saber si el usuario va anexar información a un fichero ya existente o no. True significa que anexa y false que genera un archivo
        quiereAnexarEnElArchivo = seQuiereAnexarAUnFicheroExistente(fichero);

        try (FileWriter fileWriter = new FileWriter(fichero, quiereAnexarEnElArchivo)) {
            if (quiereAnexarEnElArchivo) fileWriter.write("\n");
            System.out.println("El fichero se ha creado/accedido de forma correcta");

            int opcion;
            boolean seSigue = true;

            //Bucle de control de menú donde se le el teclado y se ejecutan una función dependiendo de lo que elija el usuario
            do {
                imprimirMenu();

                try {
                    int numeroUsuarioActual = 0;
                    opcion = Integer.parseInt(IO.readln("¿Qué quieres hacer?\n"));

                    Scanner lectorFichero = new Scanner (new BufferedReader(new FileReader(fichero)));

                    if        (opcion == 1) {
                        annadirUsuarioAlFichero(numeroUsuarioActual, quiereAnexarEnElArchivo, lectorFichero, fileWriter);
                    } else if (opcion == 2) {
                        imprimirFichero(lectorFichero);
                    } else if (opcion == 3) {
                        crearFicheroConcordancia(lectorFichero);
                    } else if (opcion == 4) {
                        seSigue = false;
                    }
                } catch (NumberFormatException e) {
                    seSigue = darMensajeDeErrorYTerminar("No se ha puesto un número válido, terminando programa...");
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