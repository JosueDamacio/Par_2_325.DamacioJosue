package cine.controlador;

import cine.modelo.CineModelo;
import cine.modelo.ClienteModelo;
import cine.modelo.SalaModelo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.List;

public class SalaControlador {

    private final CineModelo modelo;
    private final Stage stage;
    private final ClienteModelo cliente;

    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView bgImage;

    @FXML
    private ListView<SalaModelo> listaSalas;
    @FXML
    private Label infoSala;
    @FXML
    private Button btnVolver;
    @FXML
    private Button btnVerButacas;

    public SalaControlador(CineModelo modelo, Stage stage, ClienteModelo cliente) {
        this.modelo = modelo;
        this.stage = stage;
        this.cliente = cliente;
    }

    @FXML
    public void initialize() {
        if (bgImage != null && rootPane != null) {
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(rootPane.widthProperty());
            bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        }

        if (listaSalas != null && modelo != null) {
            listaSalas.getItems().clear();
            List<SalaModelo> salas = modelo.getSalas();
            if (salas != null) {
                listaSalas.getItems().addAll(salas);
            }

            listaSalas.setCellFactory(new Callback<ListView<SalaModelo>, ListCell<SalaModelo>>() {
                @Override
                public ListCell<SalaModelo> call(ListView<SalaModelo> listView) {
                    return new ListCell<SalaModelo>() {
                        @Override
                        protected void updateItem(SalaModelo item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                                setStyle("");
                            } else {
                                setText(item.getListadoParaSeleccion());
                                setFont(Font.font(20));
                                setTextFill(Color.WHITE);
                                setStyle("-fx-background-color: transparent;");
                            }
                        }
                    };
                }
            });

            listaSalas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
                if (newV != null) {
                    infoSala.setText(newV.getListadoParaSeleccion());
                } else {
                    infoSala.setText("Seleccione una sala");
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

        if (btnVerButacas != null) {
            btnVerButacas.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    SalaModelo seleccionado = null;
                    if (listaSalas != null) {
                        seleccionado = listaSalas.getSelectionModel().getSelectedItem();
                    }
                    if (seleccionado == null) {
                        javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING, "Seleccioná una sala primero.");
                        a.showAndWait();
                        return;
                    }
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/Butaca.fxml"));
                        ButacaControlador ctrl = new ButacaControlador(modelo, stage, cliente, seleccionado);
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
                        javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Error al abrir la vista de butacas.");
                        a.showAndWait();
                    }
                }
            });
        }
    }
}