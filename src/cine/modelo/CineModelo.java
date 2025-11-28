package cine.modelo;

import cine.controlador.Ejecutador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CineModelo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<SalaModelo> salas = new ArrayList<>();
    private final List<EntradaModelo> entradas = new ArrayList<>();
    private final List<ClienteModelo> clientes = new ArrayList<>();

    public CineModelo() {
        if (buscarClientePorEmail(Ejecutador.ROOT_EMAIL) == null) {
            clientes.add(new ClienteModelo(Ejecutador.ROOT_NAME, Ejecutador.ROOT_EMAIL, Ejecutador.ROOT_PASSWORD, true));
        }
        if (salas.isEmpty()) {
            SalaModelo s1 = new SalaModelo("estandar");
            s1.setTituloPelicula("Cars 5");
            s1.set3D(false);
            s1.setPrecioPelicula(3000.0);
            salas.add(s1);

            SalaModelo s2 = new SalaModelo("estandar");
            s2.setTituloPelicula("Rock 4");
            s2.set3D(false);
            s2.setPrecioPelicula(4500.0);
            salas.add(s2);

            SalaModelo s3 = new SalaModelo("grande");
            s3.setTituloPelicula("Homo Argentum");
            s3.set3D(true);
            s3.setPrecioPelicula(5300.0);
            salas.add(s3);
        }
    }

    public List<SalaModelo> getSalas() {
        return salas;
    }

    public List<EntradaModelo> getEntradas() {
        return entradas;
    }

    public List<ClienteModelo> getClientes() {
        return clientes;
    }

    public ClienteModelo buscarClientePorEmail(String email) {
        if (email == null) {
            return null;
        }
        String e = email.trim().toLowerCase();
        for (ClienteModelo c : clientes) {
            if (c.getEmail() != null && c.getEmail().trim().toLowerCase().equals(e)) {
                return c;
            }
        }
        return null;
    }

    public ClienteModelo buscarClientePorNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        String n = nombre.trim().toLowerCase();
        for (ClienteModelo c : clientes) {
            if (c.getNombreUsuario() != null && c.getNombreUsuario().trim().toLowerCase().equals(n)) {
                return c;
            }
        }
        return null;
    }

    public ClienteModelo registrarCliente(String nombre, String email, String password, boolean esAdmin) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        if (buscarClientePorEmail(email) != null) {
            return null;
        }
        if (nombre != null && !nombre.trim().isEmpty() && buscarClientePorNombre(nombre) != null) {
            return null;
        }
        ClienteModelo c = new ClienteModelo(nombre == null ? "" : nombre.trim(), email.trim(), password == null ? "" : password, esAdmin);
        clientes.add(c);
        return c;
    }

    public ClienteModelo autenticar(String email, String password) {
        if (email == null) {
            return null;
        }
        ClienteModelo c = buscarClientePorEmail(email.trim());
        if (c != null && c.verifyPassword(password)) {
            return c;
        }
        return null;
    }

    public SalaModelo crearSala(String tipo, String tituloPelicula, boolean es3D, double precio) {
        if (tituloPelicula == null || tituloPelicula.trim().isEmpty()) {
            return null;
        }
        String titulo = tituloPelicula.trim();
        for (SalaModelo s : salas) {
            if (s.getTituloPelicula() != null && s.getTituloPelicula().trim().equalsIgnoreCase(titulo) && s.is3D() == es3D) {
                return null;
            }
        }
        SalaModelo sala = new SalaModelo(tipo);
        sala.setTituloPelicula(titulo);
        sala.set3D(es3D);
        sala.setPrecioPelicula(precio);
        salas.add(sala);
        return sala;
    }

    public void agregarEntrada(EntradaModelo e) {
        entradas.add(e);
    }
}