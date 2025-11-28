package cine.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SalaModelo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final AtomicInteger COUNTER = new AtomicInteger(1);

    private final String nombre; // "Sala N"
    private String tituloPelicula = "";
    private boolean es3D = false;
    private double precioPelicula = 0.0;
    private int filas;
    private int columnas;
    private final List<ButacaModelo> butacas = new ArrayList<>();

    public SalaModelo(String tipo) {
        this.nombre = "Sala " + COUNTER.getAndIncrement();
        setTipo(tipo);
    }

    public void setTipo(String tipo) {
        if ("grande".equalsIgnoreCase(tipo)) {
            this.filas = 16;
            this.columnas = 14;
        } else {
            this.filas = 13;
            this.columnas = 12;
        }
        inicializarButacas();
    }

    private void inicializarButacas() {
        butacas.clear();
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                butacas.add(new ButacaModelo(f, c));
            }
        }
    }

    
    public String getNombre() { return nombre; }

    public String getTituloPelicula() { return tituloPelicula; }
    public void setTituloPelicula(String tituloPelicula) { this.tituloPelicula = tituloPelicula == null ? "" : tituloPelicula; }

    public boolean is3D() { return es3D; }
    public void set3D(boolean es3D) { this.es3D = es3D; }

    public double getPrecioPelicula() { return precioPelicula; }
    public void setPrecioPelicula(double precioPelicula) { this.precioPelicula = precioPelicula; }

    public List<ButacaModelo> getButacas() { return butacas; }

    public ButacaModelo obtenerButaca(int fila, int columna) {
        for (ButacaModelo b : butacas) {
            if (b.getFila() == fila && b.getColumna() == columna) return b;
        }
        return null;
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    public String getListadoParaSeleccion() {
        String tipo = is3D() ? "3D" : "2D";
        long libres = butacas.stream().filter(b -> !b.isOcupada()).count();
        String titulo = (tituloPelicula == null || tituloPelicula.isBlank()) ? "Sin función" : tituloPelicula;
        return String.format("%s - Película: %s - %s - Libres: %d", nombre, titulo, tipo, libres);
    }

    @Override
    public String toString() {
        return getListadoParaSeleccion();
    }
}