package cine.modelo;

import java.io.Serializable;
import java.util.Objects;

public class ButacaModelo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int fila; // 1-based
    private int columna; // 1-based
    private boolean ocupada;

    public ButacaModelo(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
        this.ocupada = false;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }

    public String getFilaLetra() {
        char c = (char) ('A' + (fila - 1));
        return Character.toString(c);
    }

    @Override
    public String toString() {
        return getFilaLetra() + columna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ButacaModelo)) return false;
        ButacaModelo b = (ButacaModelo) o;
        return fila == b.fila && columna == b.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }
}