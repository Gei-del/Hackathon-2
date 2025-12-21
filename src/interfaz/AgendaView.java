package interfaz;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.Agenda;
import model.Contacto;

import java.util.List;

public class AgendaView extends BorderPane {

    private final Agenda agenda;

    private final ObservableList<Contacto> tableData = FXCollections.observableArrayList();
    private final TableView<Contacto> table = new TableView<>();

    private final TextField tfNombre = new TextField();
    private final TextField tfApellido = new TextField();
    private final TextField tfTelefono = new TextField();

    private final Label lblEstado = new Label("Listo.");
    private final Label lblEspacios = new Label();

    // ✅ CONSTRUCTOR CORRECTO
    public AgendaView(Agenda agenda) {
        this.agenda = agenda;

        setPadding(new Insets(12));
        setTop(crearFormulario());
        setCenter(crearTabla());
        setBottom(crearBarraEstado());

        refrescarTabla();
        refrescarIndicadores();
    }

    private Pane crearFormulario() {
        tfNombre.setPromptText("Nombre");
        tfApellido.setPromptText("Apellido");
        tfTelefono.setPromptText("Teléfono (7-10 dígitos)");

        Button btnAgregar = new Button("Añadir");
        Button btnModificar = new Button("Modificar teléfono");
        Button btnEliminar = new Button("Eliminar");
        Button btnBuscar = new Button("Buscar");
        Button btnRecargar = new Button("Listar/Recargar");

        btnAgregar.setOnAction(e -> onAgregar());
        btnModificar.setOnAction(e -> onModificar());
        btnEliminar.setOnAction(e -> onEliminar());
        btnBuscar.setOnAction(e -> onBuscar());
        btnRecargar.setOnAction(e -> {
            refrescarTabla();
            setEstado("Lista actualizada.");
        });

        HBox filaInputs = new HBox(10, tfNombre, tfApellido, tfTelefono);
        HBox filaBotones = new HBox(10, btnAgregar, btnModificar, btnEliminar, btnBuscar, btnRecargar);

        VBox box = new VBox(10,
                new Label("Gestión de Agenda Telefónica (JavaFX)"),
                filaInputs,
                filaBotones
        );
        box.setPadding(new Insets(0, 0, 12, 0));
        return box;
    }

    private TableView<Contacto> crearTabla() {
        TableColumn<Contacto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombre()));

        TableColumn<Contacto, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getApellido()));

        TableColumn<Contacto, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(cell.getValue().getTelefono()));

        colNombre.setPrefWidth(280);
        colApellido.setPrefWidth(280);
        colTelefono.setPrefWidth(260);

        table.getColumns().addAll(colNombre, colApellido, colTelefono);
        table.setItems(tableData);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel != null) {
                tfNombre.setText(sel.getNombre());
                tfApellido.setText(sel.getApellido());
                tfTelefono.setText(sel.getTelefono());
            }
        });

        return table;
    }

    private Pane crearBarraEstado() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox box = new HBox(10, lblEstado, spacer, lblEspacios);
        box.setPadding(new Insets(12, 0, 0, 0));
        return box;
    }

    private void onAgregar() {
        try {
            Contacto c = new Contacto(
                    tfNombre.getText(),
                    tfApellido.getText(),
                    tfTelefono.getText()
            );
            String msg = agenda.aniadirContacto(c);
            setEstado(msg);
            refrescarTabla();
            refrescarIndicadores();
            if (msg.toLowerCase().contains("añad")) limpiarInputs();
        } catch (Exception ex) {
            setEstado("Error: " + ex.getMessage());
        }
    }

    private void onModificar() {
        try {
            String msg = agenda.modificarTelefono(
                    tfNombre.getText(),
                    tfApellido.getText(),
                    tfTelefono.getText()
            );
            setEstado(msg);
            refrescarTabla();
        } catch (Exception ex) {
            setEstado("Error: " + ex.getMessage());
        }
    }

    private void onEliminar() {
        try {
            Contacto c = new Contacto(
                    tfNombre.getText(),
                    tfApellido.getText(),
                    "0000000"
            );
            String msg = agenda.eliminarContacto(c);
            setEstado(msg);
            refrescarTabla();
            refrescarIndicadores();
            if (msg.toLowerCase().contains("elimin")) limpiarInputs();
        } catch (Exception ex) {
            setEstado("Error: " + ex.getMessage());
        }
    }

    private void onBuscar() {
        String nombre = tfNombre.getText();
        String apellido = tfApellido.getText();
        String resultado = agenda.buscaContacto(nombre, apellido);
        setEstado(resultado);

        if (!resultado.toLowerCase().contains("no encontrado")) {
            tableData.stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombre)
                            && c.getApellido().equalsIgnoreCase(apellido))
                    .findFirst()
                    .ifPresent(c -> table.getSelectionModel().select(c));
        }
    }

    private void refrescarTabla() {
        List<Contacto> ordenados = agenda.listarContactosOrdenados();
        tableData.setAll(ordenados);
    }

    private void refrescarIndicadores() {
        lblEspacios.setText(
                "Espacios libres: " + agenda.espacioLibres() +
                        " / " + agenda.getTamanioMaximo()
        );
        if (agenda.agendaLlena()) {
            setEstado("La agenda está llena. No hay espacio disponible para nuevos contactos.");
        }
    }

    private void limpiarInputs() {
        tfNombre.clear();
        tfApellido.clear();
        tfTelefono.clear();
    }

    private void setEstado(String msg) {
        lblEstado.setText(msg);
    }
}
