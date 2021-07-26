package Main;

import Main.Model.ThreadClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ObservableFaceArray;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Controller implements  Observer {
    private Socket socket;
    private DataOutputStream bufferDeSalida = null;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnConectar;

    @FXML
    private TextField ipServer;

    @FXML
    private TextField portServer;

    @FXML
    private TextArea log;

    @FXML
    private TextField txtEnviar;

    @FXML
    private Button btnEnviar;

    @FXML
    private Circle circle;

    @FXML
    void btnConectarOnMouseClicked(MouseEvent event) {
        try {
            socket = new Socket(ipServer.getText(), Integer.valueOf(portServer.getText()));
            log.setText( "Creado");
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();

            ThreadClient cliente = new ThreadClient(socket,log);
            cliente.addObserver(this);
            new Thread(cliente).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSalirOnMouseClicked(MouseEvent event) {
        Platform.exit();
        System.exit(1);

    }

    @FXML
    void btnCerrarOnMouseClicked(MouseEvent event) {
        try {
            socket.close();
            System.out.println("Cerrando...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnEnviarOnMouseClicked(MouseEvent event) {
        try {
            bufferDeSalida.writeUTF(txtEnviar.getText());
            bufferDeSalida.writeUTF("1:1");
            bufferDeSalida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        String color = (String) arg;
        switch (color){
            case "1":
                circle.setFill(Color.RED);
                break;
            case "2":
                circle.setFill(Color.GREEN);
                break;
            case "3":
                circle.setFill(Color.BLUE);
                break;
            case "4":
                circle.setFill(Color.YELLOW);
                break;
        }
    }
}
