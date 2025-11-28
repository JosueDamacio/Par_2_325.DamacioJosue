package cine.persistencia;

import cine.modelo.CineModelo;

import java.io.*;

public class PersistenciaDatos {

    private static final String RUTA = "cine.ser";

    public static CineModelo cargar() {
        File f = new File(RUTA);
        if (!f.exists()) {
            return new CineModelo();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof CineModelo) {
                return (CineModelo) o;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return new CineModelo();
    }

    public static boolean guardar(CineModelo modelo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RUTA))) {
            oos.writeObject(modelo);
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }
}