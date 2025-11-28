package cine.controlador;

import cine.modelo.ButacaModelo;
import cine.modelo.CineModelo;
import cine.modelo.ClienteModelo;
import cine.modelo.SalaModelo;
import cine.persistencia.PersistenciaDatos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ButacaControlador {

    private final CineModelo modelo;
    private final Stage stage;
    private final ClienteModelo cliente;
    private final SalaModelo sala;

    @FXML
    private StackPane rootPane;
    @FXML
    private ImageView bgImage;

    @FXML
    private Label titulo;
    @FXML
    private Label pantallaLabel;
    @FXML
    private GridPane grid;
    @FXML
    private Label totalLabel;
    @FXML
    private Button volverBtn;
    @FXML
    private Button confirmarBtn;

    private final List<ButacaModelo> butacasSeleccionadas = new ArrayList<>();

    public ButacaControlador(CineModelo modelo, Stage stage, ClienteModelo cliente, SalaModelo sala) {
        this.modelo = modelo;
        this.stage = stage;
        this.cliente = cliente;
        this.sala = sala;
    }

    @FXML
    public void initialize() {
        if (bgImage != null && rootPane != null) {
            bgImage.setPreserveRatio(false);
            bgImage.fitWidthProperty().bind(rootPane.widthProperty());
            bgImage.fitHeightProperty().bind(rootPane.heightProperty());
        }

        if (titulo != null) {
            if (sala != null) {
                titulo.setText("Seleccionar butacas - " + sala.getTituloPelicula());
            } else {
                titulo.setText("Seleccionar butacas");
            }
            titulo.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        }

        if (pantallaLabel != null) {
            pantallaLabel.setStyle(
                "-fx-background-color: rgba(255,255,255,0.98);" +
                "-fx-text-fill: #222222;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 8 36 8 36;" +
                "-fx-background-radius: 6px;" +
                "-fx-border-color: rgba(0,0,0,0.08);" +
                "-fx-border-radius: 6px;"
            );
        }

        if (totalLabel != null) {
            totalLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        }

        if (grid != null) {
            grid.getChildren().clear();
            grid.setHgap(8);
            grid.setVgap(8);
            grid.setAlignment(Pos.CENTER);
        }

        if (sala != null) {
            List<ButacaModelo> lista = sala.getButacas();
            if (lista != null) {
                for (ButacaModelo b : lista) {
                    int fila = b.getFila();
                    int col = b.getColumna();

                    if (b.isOcupada()) {
                        Label box = new Label();
                        box.setMinSize(18, 18);
                        box.setPrefSize(18, 18);
                        box.setStyle(
                            "-fx-background-color: #ff4d4d;" +
                            "-fx-background-radius: 4px;" +
                            "-fx-border-color: #990000;" +
                            "-fx-border-width: 1px;" +
                            "-fx-border-radius: 4px;"
                        );

                        Label lbl = new Label(b.toString());
                        lbl.setStyle("-fx-text-fill: white; -fx-padding: 0 6 0 6;");

                        HBox cont = new HBox(6, box, lbl);
                        cont.setAlignment(Pos.CENTER);
                        cont.getStyleClass().add("butaca-wrapper");
                        cont.getStyleClass().add("ocupada-wrapper");
                        Tooltip.install(cont, new Tooltip("Ocupada"));

                        GridPane.setHalignment(cont, HPos.CENTER);
                        grid.add(cont, col - 1, fila - 1);
                    } else {
                        CheckBox cb = new CheckBox(b.toString());
                        cb.setTextFill(Color.WHITE);
                        cb.setStyle("-fx-text-fill: white; -fx-opacity: 1;");

                        cb.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                if (cb.isSelected()) {
                                    butacasSeleccionadas.add(b);
                                } else {
                                    Iterator<ButacaModelo> it = butacasSeleccionadas.iterator();
                                    while (it.hasNext()) {
                                        ButacaModelo x = it.next();
                                        if (x.getFila() == b.getFila() && x.getColumna() == b.getColumna()) {
                                            it.remove();
                                        }
                                    }
                                }
                                actualizarTotal();
                            }
                        });

                        HBox cont = new HBox(cb);
                        cont.setAlignment(Pos.CENTER);
                        cont.getStyleClass().add("butaca-wrapper");

                        GridPane.setHalignment(cont, HPos.CENTER);
                        grid.add(cont, col - 1, fila - 1);
                    }
                }
            }
        }

        if (volverBtn != null) {
            volverBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
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
                        Alert a = new Alert(Alert.AlertType.ERROR, "Error al abrir lista de salas");
                        a.showAndWait();
                    }
                }
            });
        }

        if (confirmarBtn != null) {
            confirmarBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (butacasSeleccionadas.isEmpty()) {
                        Alert a = new Alert(Alert.AlertType.WARNING, "Elegi una butaca!");
                        a.showAndWait();
                        return;
                    }
                    try {
                        boolean saved = PersistenciaDatos.guardar(modelo);
                        if (saved) {
                            System.out.println("Guardado exitoso");
                        }
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cine/vista/Compra.fxml"));
                        CompraControlador ctrl = new CompraControlador(modelo, stage, cliente, sala, new ArrayList<>(butacasSeleccionadas));
                        loader.setController(ctrl);
                        Parent root = loader.load();
                        Scene sc = new Scene(root, 1250, 720);
                        try {
                            sc.getStylesheets().add(getClass().getResource("/estilos/styles.css").toExternalForm());
                        } catch (Throwable ignored) {
                        }
                        stage.setScene(sc);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Alert a = new Alert(Alert.AlertType.ERROR, "error al abrir vetana compra");
                        a.showAndWait();
                    }
                }
            });
        }

        actualizarTotal();
    }

    private void actualizarTotal() {
        double precioBase = 0.0;
        if (sala != null) {
            precioBase = sala.getPrecioPelicula();
            if (sala.is3D()) {
                precioBase = precioBase * 1.5;
            }
        }
        double total = precioBase * butacasSeleccionadas.size();
        if (totalLabel != null) {
            totalLabel.setText(String.format("Total: $ %.2f", total));
            totalLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        }
    }
}