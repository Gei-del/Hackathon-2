package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*Lógica de negocio de la Agenda.*/
public class Agenda {

    private final int tamanioMaximo;
    private final List<Contacto> contactos;

    public Agenda() {
        this(10);
    }

    public Agenda(int tamanioMaximo) {
        if (tamanioMaximo <= 0) {
            throw new IllegalArgumentException("El tamaño máximo debe ser mayor que cero.");
        }
        this.tamanioMaximo = tamanioMaximo;
        this.contactos = new ArrayList<>();
    }

    // --- Métodos del enunciado ---

    public String aniadirContacto(Contacto c) {
        if (agendaLlena()) {
            return "La agenda está llena. No hay espacio disponible.";
        }
        if (existeContacto(c)) {
            return "No se puede añadir: el contacto ya existe (duplicado).";
        }
        contactos.add(c);
        return "Contacto añadido correctamente.";
    }

    public boolean existeContacto(Contacto c) {
        return contactos.contains(c);
    }

    public List<Contacto> listarContactosOrdenados() {
        List<Contacto> copia = new ArrayList<>(contactos);
        copia.sort(Comparator.comparing(Contacto::getNombre, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Contacto::getApellido, String.CASE_INSENSITIVE_ORDER));
        return copia;
    }


    public String buscaContacto(String nombre, String apellido) {
        int idx = indexOfContacto(nombre, apellido);
        if (idx == -1) {
            return "Contacto no encontrado.";
        }
        return "Teléfono: " + contactos.get(idx).getTelefono();
    }

    public String eliminarContacto(Contacto c) {
        boolean eliminado = contactos.remove(c);
        return eliminado ? "Contacto eliminado correctamente." : "No se pudo eliminar: el contacto no existe.";
    }

    public String modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        int idx = indexOfContacto(nombre, apellido);
        if (idx == -1) {
            return "No se puede modificar: el contacto no existe.";
        }
        contactos.get(idx).setTelefono(nuevoTelefono);
        return "Teléfono actualizado correctamente.";
    }

    public boolean agendaLlena() {
        return contactos.size() >= tamanioMaximo;
    }

    public int espacioLibres() {
        return tamanioMaximo - contactos.size();
    }


    private int indexOfContacto(String nombre, String apellido) {
        // Creamos “dummy” con teléfono válido para poder usar equals (solo compara nombre+apellido).
        Contacto dummy = new Contacto(nombre, apellido, "0000000");
        return contactos.indexOf(dummy);
    }

    // Útil para JavaFX (tabla)
    public List<Contacto> getContactosSnapshot() {
        return new ArrayList<>(contactos);
    }

    public int getTamanioMaximo() {
        return tamanioMaximo;
    }
}
