package cine.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class EntradaModelo implements Serializable {
    private static final long serialVersionUID = 1L;

    //guarda todos los datos de  la entrada
    private final String compradorNombre;
    private final String peliculaTitulo;
    private final List<ButacaModelo> butacasCompradas = new ArrayList<>();
    private final double precioTotal;

    public EntradaModelo(String compradorNombre, String horarioFuncion, List<ButacaModelo> butacasCompradas, double precioTotal) {
        this.compradorNombre = compradorNombre == null ? "" : compradorNombre;
        this.peliculaTitulo = horarioFuncion == null ? "" : horarioFuncion;
        if (butacasCompradas != null) this.butacasCompradas.addAll(butacasCompradas);
        this.precioTotal = precioTotal;
    }

    public String getCompradorNombre() { return compradorNombre; }
    public String getPeliculaTitulo() { return peliculaTitulo; }
    public List<ButacaModelo> getButacasCompradas() { return butacasCompradas; }
    public int getCantidadEntradas() { return butacasCompradas.size(); }
    public double getPrecioTotal() { return precioTotal; }

    // alias utilizados en distintas vistas
    public String getNombreCliente() { return getCompradorNombre(); }
    public String getNombrePelicula() { return getPeliculaTitulo(); }
    public double getValorTotal() { return getPrecioTotal(); }
}