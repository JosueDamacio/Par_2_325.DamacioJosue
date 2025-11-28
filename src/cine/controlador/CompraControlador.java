package cine.controlador;

import cine.modelo.ButacaModelo;
import cine.modelo.CineModelo;
import cine.modelo.ClienteModelo;
import cine.modelo.EntradaModelo;
import cine.modelo.SalaModelo;
import cine.persistencia.PersistenciaDatos;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.stream.Collectors;

public class CompraControlador {

    private final CineModelo modelo;
    private final Stage stage;
    private final ClienteModelo cliente;
    private final SalaModelo sala;
    private final List<ButacaModelo> butacas;

    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView bgImage;

    @FXML
    private Label lblCliente;
    @FXML
    private Label lblPelicula;
    @FXML
    private Label lblButacas;
    @FXML
    private Label lblCantidad;
    @FXML
    private Label lblValor;
    @FXML
    private Button btnConfirmar;
    @FXML
    private Button btnVolver;

    public CompraControlador(CineModelo modelo, Stage stage, ClienteModelo cliente, SalaModelo sala, List<ButacaModelo> butacas) {
        this.modelo = modelo;
        this.stage = stage;
        this.cliente = cliente;
        this.sala = sala;
        this.butacas = butacas;
    }

    @FXML
    public void initialize() {
        if (bgImage != null && rootPane != null) {
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(rootPane.widthProperty());
            bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        }
        if (lblCliente != null) {
            if (cliente != null) {
                lblCliente.setText("Cliente: " + safeNombre(cliente));
            } else {
                //por deefecto
                lblCliente.setText("Cliente: Invitado");
            }
        }
        if (lblPelicula != null) {
            if (sala != null) {
                lblPelicula.setText("Película: " + sala.getTituloPelicula());
            } else {
                lblPelicula.setText("Película: ");
            }
        }
        String btext = "";
        if (butacas != null) {
            btext = butacas.stream().map(ButacaModelo::toString).collect(Collectors.joining(", "));
        }
        if (lblButacas != null) {
            lblButacas.setText("Butacas: " + btext);
        }
        if (lblCantidad != null) {
            lblCantidad.setText("Cantidad: " + (butacas == null ? 0 : butacas.size()));
        }
        double precio = 0.0;
        if (sala != null) {
            precio = sala.getPrecioPelicula();
            if (sala.is3D()) {
                precio = precio * 1.5;
            }
        }
        double total = precio * (butacas == null ? 0 : butacas.size());
        if (lblValor != null) {
            lblValor.setText(String.format("Valor total: $ %.2f", total));
        }
        if (btnConfirmar != null) {
            btnConfirmar.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    confirmar(total);
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

    private String safeNombre(ClienteModelo c) {
        String n = c.getNombreUsuario();
        if (n == null) {
            return "";
        }
        if (n.isBlank()) {
            return "";
        }
        return n;
    }

    private void confirmar(double total) {
        if (sala != null && butacas != null) {
            for (ButacaModelo b : butacas) {
                ButacaModelo real = sala.obtenerButaca(b.getFila(), b.getColumna());
                if (real != null) {
                    real.setOcupada(true);
                }
            }
        }
        EntradaModelo entrada = new EntradaModelo(cliente != null ? safeNombre(cliente) : "Invitado", sala != null ? sala.getTituloPelicula() : "", butacas, total);
        modelo.agregarEntrada(entrada);
        if (cliente != null) {
            cliente.agregarEntrada(entrada);
        }
        boolean ok = PersistenciaDatos.guardar(modelo);
        if (!ok) {
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, "Compra realizada, pero no se pudo guardar :(");
            a.showAndWait();
        } else {
            javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "Gracias por su compra!");
            a.showAndWait();
        }
        Ejecutador.showMenu(stage, modelo);
    }
}