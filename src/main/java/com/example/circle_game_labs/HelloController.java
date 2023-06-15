package com.example.circle_game_labs;

import com.example.circle_game_labs.client.ServerThread;
import com.example.circle_game_labs.server.Server;
import javafx.beans.NamedArg;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Locale;

public class HelloController {

    public GraphicsContext graphicsContext;
    @FXML
    public Slider radiusSlider;
    @FXML
    public ColorPicker colorPicker;
    @FXML
    public TextField portField;
    @FXML
    TextField addressField;
    @FXML
    public Canvas canvas;
    Server server;
    ServerThread serverThread;

    public void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();
    }

    public void onStartServerClicked(ActionEvent actionEvent) {
        String address = addressField.getText().isEmpty() ? "localhost" : addressField.getText();
        int port = portField.getText().isEmpty() ? 5000 : Integer.parseInt(portField.getText());
        server = new Server(port);
        server.run();
        serverThread = new ServerThread(address, port);
        serverThread.setDots(dot -> {
            graphicsContext.setFill(dot.color());
            graphicsContext.fillOval(dot.centerX(), dot.centerY(), dot.radius(), dot.radius());
        });
        serverThread.start();
        System.out.println("Server started on port" + port);
    }

    public void onConnectClicked() {
        String address = addressField.getText().isEmpty() ? "localhost" : addressField.getText();
        int port = portField.getText().isEmpty() ? 5000 : Integer.parseInt(portField.getText());
        serverThread = new ServerThread(address, port);
        serverThread.setDots(dot -> {
            graphicsContext.setFill(dot.color());
            graphicsContext.fillOval(dot.centerX(), dot.centerY(), dot.radius(), dot.radius());
        });
        serverThread.start();
        System.out.println("You have been connected to server on port: " + port);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        double radius = radiusSlider.getValue();
        Color color = colorPicker.getValue();
        server.saveDot(new Dot(x,y,radius,color));
        serverThread.send(x-radius/2, y-radius/2, radius, color);
    }

    public void drawCircle(double x, double y, double radius, Color color) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(color);
        context.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public String colorToString(Color color) {
        return String.format(Locale.ENGLISH, "#02X%02X%02X%", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }
}