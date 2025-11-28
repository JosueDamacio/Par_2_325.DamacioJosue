package cine.controlador;

import cine.modelo.CineModelo;
import cine.persistencia.PersistenciaDatos;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Ejecutador extends Application {

    public static final String ROOT_NAME = "root";
    public static final String ROOT_EMAIL = "root@local";
    public static final String ROOT_PASSWORD = "1234";

    private static Stage primaryStage;
    private static CineModelo modelo;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        modelo = PersistenciaDatos.cargar();
        if (modelo == null) {
            modelo = new CineModelo();
        }
        showMenu(stage, modelo);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                PersistenciaDatos.guardar(modelo);
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void showMenu(Stage stage, CineModelo cineModelo) {
        try {
            FXMLLoader loader = new FXMLLoader(Ejecutador.class.getResource("/cine/vista/Menu.fxml"));
            MenuControlador controller = new MenuControlador(cineModelo, stage);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root, 1250, 720);
            try {
                scene.getStylesheets().add(Ejecutador.class.getResource("/estilos/styles.css").toExternalForm());
            } catch (Throwable ignored) {
            }
            stage.setScene(scene);
            stage.setTitle("Cine - Menú");
            stage.centerOnScreen();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static CineModelo getModelo() {
        return modelo;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}