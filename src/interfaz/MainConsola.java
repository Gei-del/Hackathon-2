package interfaz;

import model.Agenda;
import model.Contacto;

import java.util.List;
import java.util.Scanner;

public class MainConsola {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== AGENDA (Consola) ===");
        Agenda agenda = crearAgenda(sc);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(sc, "Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1 -> agregar(sc, agenda);
                    case 2 -> listar(agenda);
                    case 3 -> buscar(sc, agenda);
                    case 4 -> eliminar(sc, agenda);
                    case 5 -> modificar(sc, agenda);
                    case 6 -> System.out.println(agenda.agendaLlena()
                            ? "Agenda llena: no hay espacio."
                            : "Agenda NO está llena: aún hay espacio.");
                    case 7 -> System.out.println("Espacios libres: " + agenda.espacioLibres());
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);

        sc.close();
    }

    private static Agenda crearAgenda(Scanner sc) {
        System.out.println("1) Tamaño por defecto (10)");
        System.out.println("2) Definir tamaño máximo");
        int op = leerEntero(sc, "Elija: ");
        if (op == 2) {
            int max = leerEntero(sc, "Tamaño máximo: ");
            return new Agenda(max);
        }
        return new Agenda();
    }

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Añadir contacto");
        System.out.println("2. Listar contactos");
        System.out.println("3. Buscar contacto");
        System.out.println("4. Eliminar contacto");
        System.out.println("5. Modificar teléfono");
        System.out.println("6. Agenda llena");
        System.out.println("7. Espacios libres");
        System.out.println("0. Salir");
    }

    private static void agregar(Scanner sc, Agenda agenda) {
        String nombre = leerTexto(sc, "Nombre: ");
        String apellido = leerTexto(sc, "Apellido: ");
        String telefono = leerTexto(sc, "Teléfono (7-10 dígitos): ");
        Contacto c = new Contacto(nombre, apellido, telefono);
        System.out.println(agenda.aniadirContacto(c));
    }

    private static void listar(Agenda agenda) {
        List<Contacto> lista = agenda.listarContactosOrdenados();
        if (lista.isEmpty()) {
            System.out.println("La agenda está vacía.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static void buscar(Scanner sc, Agenda agenda) {
        String nombre = leerTexto(sc, "Nombre: ");
        String apellido = leerTexto(sc, "Apellido: ");
        System.out.println(agenda.buscaContacto(nombre, apellido));
    }

    private static void eliminar(Scanner sc, Agenda agenda) {
        String nombre = leerTexto(sc, "Nombre: ");
        String apellido = leerTexto(sc, "Apellido: ");
        // Teléfono válido dummy (equals ignora teléfono)
        Contacto c = new Contacto(nombre, apellido, "0000000");
        System.out.println(agenda.eliminarContacto(c));
    }

    private static void modificar(Scanner sc, Agenda agenda) {
        String nombre = leerTexto(sc, "Nombre: ");
        String apellido = leerTexto(sc, "Apellido: ");
        String nuevoTelefono = leerTexto(sc, "Nuevo teléfono: ");
        System.out.println(agenda.modificarTelefono(nombre, apellido, nuevoTelefono));
    }

    private static int leerEntero(Scanner sc, String msg) {
        System.out.print(msg);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Ingrese un número válido: ");
        }
        int val = sc.nextInt();
        sc.nextLine();
        return val;
    }

    private static String leerTexto(Scanner sc, String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }
}