package cine.controlador;

import cine.modelo.CineModelo;
import cine.modelo.EntradaModelo;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

/*esta clase muestra el historial de ventas.*/
public class AdminControlador {

    private final CineModelo modelo;
    private final Stage stage;

    @FXML private StackPane rootPane;
    @FXML private ImageView bgImage;

    @FXML private TableView<EntradaModelo> tabla;
    @FXML private TableColumn<EntradaModelo, String> colNombre;
    @FXML private TableColumn<EntradaModelo, String> colCantidad;
    @FXML private TableColumn<EntradaModelo, String> colPelicula;
    @FXML private TableColumn<EntradaModelo, String> colValor;
    @FXML private Button btnVolver;
    @FXML private Label lblTotal;

    public AdminControlador(CineModelo modelo, Stage stage) {
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

        if (colNombre != null) colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompradorNombre()));
        if (colCantidad != null) colCantidad.setCellValueFactory(c -> new SimpleStringProperty(Integer.toString(c.getValue().getCantidadEntradas())));
        if (colPelicula != null) colPelicula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombrePelicula()));
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        if (colValor != null) colValor.setCellValueFactory(c -> new SimpleStringProperty(nf.format(c.getValue().getPrecioTotal())));

        if (tabla != null) {
            tabla.getItems().clear();
            if (modelo != null && modelo.getEntradas() != null) tabla.getItems().addAll(modelo.getEntradas());
        }

        double total = modelo == null ? 0.0 : modelo.getEntradas().stream().mapToDouble(EntradaModelo::getPrecioTotal).sum();
        if (lblTotal != null) lblTotal.setText("Total ventas: " + nf.format(total));

        if (btnVolver != null) btnVolver.setOnAction(e -> Ejecutador.showMenu(stage, modelo));
    }
}