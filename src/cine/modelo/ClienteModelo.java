package cine.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClienteModelo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombreUsuario;
    private String email;
    private String password;
    private boolean admin; //esto no se si es lo mas conveniente pero este valor permite acceso admin
    private final List<EntradaModelo> entradas = new ArrayList<>();

    public ClienteModelo(String nombreUsuario, String email, String password, boolean admin) {
        this.nombreUsuario = nombreUsuario == null ? "" : nombreUsuario;
        this.email = email == null ? "" : email;
        this.password = password == null ? "" : password;
        this.admin = admin;
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return admin; }
    public List<EntradaModelo> getEntradas() { return entradas; }

    public void agregarEntrada(EntradaModelo e) { entradas.add(e); }

    public String getNombre() { return getNombreUsuario(); }

    public boolean verifyPassword(String raw) {
        if (raw == null) raw = "";
        return password.equals(raw);
    }
    public boolean checkPassword(String raw) { return verifyPassword(raw); }

    public void setPassword(String raw) { this.password = raw == null ? "" : raw; }
}