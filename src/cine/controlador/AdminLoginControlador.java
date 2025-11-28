package cine.controlador;

import cine.modelo.CineModelo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AdminLoginControlador {

    private final CineModelo modelo;
    private final Stage stage;

    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView bgImage;

    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Label lblError;
    @FXML
    private Button btnIngresar;
    @FXML
    private Button btnVolver;

    public AdminLoginControlador(CineModelo modelo, Stage stage) {
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
        if (btnIngresar != null) {
            btnIngresar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onIngresar();
                }
            });
        }
        if (btnVolver != null) {
            btnVolver.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Ejecutador.showMenu(stage, modelo);
                }
            });
        }
    }

    private void onIngresar() {
        if (lblError != null) {
            lblError.setText("");
        }
        String user = "";
        if (txtUser != null && txtUser.getText() != null) {
            user = txtUser.getText().trim();
        }
        String pass = "";
        if (txtPass != null && txtPass.getText() != null) {
            pass = txtPass.getText();
        }
        if (user.isEmpty() || pass.isEmpty()) {
            if (lblError != null) {
                lblError.setText("Todos los campos son obligatorios");
            }
            return;
        }
        if (!user.equals(Ejecutador.ROOT_NAME) || !pass.equals(Ejecutador.ROOT_PASSWORD)) {
            if (lblError != null) {
                lblError.setText("Nombre o Contraseña incorrecto");
            }
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/Admin.fxml"));
            AdminControlador ctrl = new AdminControlador(modelo, stage);
            loader.setController(ctrl);
            Parent root = loader.load();
            Scene s = new Scene(root, 900, 600);
            try {
                s.getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm());
            } catch (Throwable ignored) {
            }
            stage.setScene(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (lblError != null) {
                lblError.setText("Error al intentar abrir la vista ADMIN");
            }
        }
    }
}