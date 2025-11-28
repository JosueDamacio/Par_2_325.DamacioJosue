package cine.controlador;

import cine.modelo.CineModelo;
import cine.modelo.ClienteModelo;
import cine.persistencia.PersistenciaDatos;
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

import java.util.regex.Pattern;

public class ClienteLoginControlador {

    private final CineModelo modelo;
    private final Stage stage;

    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView bgImage;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMail;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Label lblCrearError;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnVolver;

    @FXML
    private TextField loginMail;
    @FXML
    private PasswordField loginPass;
    @FXML
    private Label lblLoginError;
    @FXML
    private Button btnIngresar;

    private final Pattern emailPattern = Pattern.compile(".+@.+");

    public ClienteLoginControlador(CineModelo modelo, Stage stage) {
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
        if (btnAceptar != null) {
            btnAceptar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    onCrearCuenta();
                }
            });
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

    //gestiona toda la creacion de cuentas y posibles errores (tomo mucho tienpo)
    private void onCrearCuenta() {
        if (lblCrearError != null) {
            lblCrearError.setStyle("-fx-text-fill: red;");
            lblCrearError.setText("");
        }
        String nombre = "";
        if (txtNombre != null && txtNombre.getText() != null) {
            nombre = txtNombre.getText().trim();
        }
        String mailRaw = "";
        if (txtMail != null && txtMail.getText() != null) {
            mailRaw = txtMail.getText().trim();
        }
        String mail = mailRaw.toLowerCase();
        String pass = "";
        if (txtPass != null && txtPass.getText() != null) {
            pass = txtPass.getText();
        }
        if (nombre.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
            if (lblCrearError != null) {
                lblCrearError.setText("Todos los campos son obligatorios");
            }
            return;
        }
        if (!emailPattern.matcher(mail).matches()) {
            if (lblCrearError != null) {
                lblCrearError.setText("Los mail deben tener @");
            }
            return;
        }
        if (modelo.buscarClientePorEmail(mail) != null) {
            if (lblCrearError != null) {
                lblCrearError.setText("Ya existe un usuario con ese mail");
            }
            return;
        }
        if (modelo.buscarClientePorNombre(nombre) != null) {
            if (lblCrearError != null) {
                lblCrearError.setText("Ya hay un usuario con ese nombre");
            }
            return;
        }
        try {
            ClienteModelo c = modelo.registrarCliente(nombre, mail, pass, false);
            if (c == null) {
                if (lblCrearError != null) {
                    lblCrearError.setText("No se creo la cuenta (verifique datos).");
                }
                System.out.println("[ClienteLoginController] registrarCliente devolvió null para: " + nombre + " / " + mail);
                return;
            }
            boolean saved = PersistenciaDatos.guardar(modelo);
            System.out.println("[ClienteLoginController] Cuenta creada: " + mail + " - guardado = " + saved);
            if (!saved) {
                if (lblCrearError != null) {
                    lblCrearError.setStyle("-fx-text-fill: orange;");
                    lblCrearError.setText("Cuenta creada, problema al guardarla");
                }
            } else {
                if (lblCrearError != null) {
                    lblCrearError.setStyle("-fx-text-fill: green;");
                    lblCrearError.setText("Cuenta creada. Inicie sesión a la derecha");
                }
            }
            if (loginMail != null) {
                loginMail.setText(mail);
            }
            if (txtNombre != null) {
                txtNombre.clear();
            }
            if (txtMail != null) {
                txtMail.clear();
            }
            if (txtPass != null) {
                txtPass.clear();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            if (lblCrearError != null) {
                lblCrearError.setText("Error al crear la cuenta (ver consola)");
            }
        }
    }
    
    //revisa y valida los datos ingresados 
    private void onIngresar() {
        if (lblLoginError != null) {
            lblLoginError.setText("");
        }
        String mail = "";
        if (loginMail != null && loginMail.getText() != null) {
            mail = loginMail.getText().trim();
        }
        String pass = "";
        if (loginPass != null && loginPass.getText() != null) {
            pass = loginPass.getText();
        }
        if (mail.isEmpty() || pass.isEmpty()) {
            if (lblLoginError != null) {
                lblLoginError.setText("mail y clave obligatorios");
            }
            return;
        }
        ClienteModelo c = modelo.autenticar(mail, pass);
        if (c == null) {
            if (lblLoginError != null) {
                lblLoginError.setText("Datos incorrectos o inexistentes");
            }
            return;
        }
        abrirSeleccionSalas(c);
    }

    private void abrirSeleccionSalas(ClienteModelo cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/Sala.fxml"));
            SalaControlador ctrl = new SalaControlador(modelo, stage, cliente);
            loader.setController(ctrl);
            Parent root = loader.load();
            Scene s = new Scene(root, 1250, 720);
            try {
                s.getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm());
            } catch (Throwable ignored) {
            }
            stage.setScene(s);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (lblCrearError != null) {
                lblCrearError.setText("Error al abrir vista de salas");
            }
        }
    }
}