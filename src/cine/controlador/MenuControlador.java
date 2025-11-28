package cine.controlador;

import cine.modelo.CineModelo;
import cine.persistencia.PersistenciaDatos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MenuControlador {

    private final CineModelo modelo;
    private final Stage stage;

    @FXML private StackPane rootPane;
    @FXML private ImageView bgImage;

    @FXML private Button btnAdmin;
    @FXML private Button btnComprar;
    @FXML private Button btnSalir;

    public MenuControlador(CineModelo modelo, Stage stage) {
        this.modelo = modelo;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
       
        if (bgImage != null && rootPane != null) {
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(rootPane.widthProperty());
            bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        }

        if (btnAdmin != null) btnAdmin.setOnAction(e -> abrirAdminLogin());
        if (btnComprar != null) btnComprar.setOnAction(e -> abrirClienteLogin());
        if (btnSalir != null) btnSalir.setOnAction(e -> {
            PersistenciaDatos.guardar(modelo);
            stage.close();
        });
    }

    private void abrirAdminLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/AdminLogin.fxml"));
            AdminLoginControlador ctrl = new AdminLoginControlador(modelo, stage);
            loader.setController(ctrl);
            Parent root = loader.load();
            Scene s = new Scene(root, 1250, 720);
            try { s.getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm()); } catch (Throwable ignored) {}
            stage.setScene(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void abrirClienteLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/ClienteLogin.fxml"));
            ClienteLoginControlador ctrl = new ClienteLoginControlador(modelo, stage);
            loader.setController(ctrl);
            Parent root = loader.load();
            Scene s = new Scene(root, 1250, 720);
            try { s.getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm()); } catch (Throwable ignored) {}
            stage.setScene(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}