package interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Agenda;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        // Agenda con tamaño por defecto (10)
        Agenda agenda = new Agenda();

        // Vista principal
        AgendaView root = new AgendaView(agenda);

        // Escena JavaFX (NO Scanner)
        Scene scene = new Scene(root, 920, 560);

        // Configuración del Stage
        stage.setTitle("Agenda Telefónica - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
