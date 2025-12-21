package model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Modelo de Contacto.
 * Regla de igualdad: nombre + apellido (case-insensitive).
 */
public class Contacto {

    private final String nombre;
    private final String apellido;
    private String telefono;

    // Teléfono: 7 a 10 dígitos (ajusta si tu profe pide otro formato)
    private static final Pattern TELEFONO_PATTERN = Pattern.compile("\\d{7,10}");

    public Contacto(String nombre, String apellido, String telefono) {
        String nombreOk = normalizarTexto(nombre);
        String apellidoOk = normalizarTexto(apellido);

        if (nombreOk.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellidoOk.isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        validarTelefono(telefono);

        this.nombre = nombreOk;
        this.apellido = apellidoOk;
        this.telefono = telefono.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String nuevoTelefono) {
        validarTelefono(nuevoTelefono);
        this.telefono = nuevoTelefono.trim();
    }

    private static void validarTelefono(String telefono) {
        if (telefono == null || !TELEFONO_PATTERN.matcher(telefono.trim()).matches()) {
            throw new IllegalArgumentException("Formato de teléfono inválido (usa 7-10 dígitos).");
        }
    }

    private static String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    /**
     * Igualdad: mismo nombre y apellido (sin distinguir mayúsculas/minúsculas).
     * El teléfono NO se usa para comparar duplicados.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;
        Contacto other = (Contacto) o;

        return nombre.equalsIgnoreCase(other.nombre)
                && apellido.equalsIgnoreCase(other.apellido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase(), apellido.toLowerCase());
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }
}
