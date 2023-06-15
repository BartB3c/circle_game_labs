package com.example.circle_game_labs.server;

import com.example.circle_game_labs.Dot;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Server extends Thread{
    ServerSocket server;
    List<ClientThread> clients = new ArrayList<>();
    private int port;
    private Connection connection;

    public void setPort(int port){
        this.port = port;
    }

    public Server(int port){

        try {
            this.port = port;
            this.server = new ServerSocket(port);
            Class.forName("ord.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\offic\\IdeaProjects\\circle_game_labs\\dots");
            System.out.println("Connected to DB");
        } catch (IOException | ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
       Thread listenThread = new Thread(() -> {
           while (true){
               try {
                   Socket clientSocket = server.accept();
                   System.out.println(clientSocket + " joined!");
                   ClientThread clientThread = new ClientThread(clientSocket, this);
                   clients.add(clientThread);
                   clientThread.start();
                   List<Dot> dots = getSavedDots();
                   for (var dot : dots){
                       clientThread.send(dot.centerX()+" "+dot.centerY()+" "+dot.radius()+" "+dot.color().toString());
                   }
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
           }
       });
    }

    public void broadcast(String message){
        for (ClientThread client : clients){
            client.send(message);
        }
        saveDot(Dot.FromMessage(message));
    }

    public void saveDot(Dot dot){
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO dots (centerX, centerY, radius, color) VALUES (?, ?, ?, ?)");
            statement.setDouble(1, dot.centerX());
            statement.setDouble(2, dot.centerY());
            statement.setDouble(3, dot.radius());
            statement.setString(4, dot.color().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void sendSavedDotsToClients(ClientThread clientThread){
        List<Dot> saveDots = getSavedDots();
    }

    private List<Dot> getSavedDots(){
        List<Dot> saveDots = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM dots");
            while (resultSet.next()){
                double centerX = resultSet.getDouble("centerX");
                double centerY = resultSet.getDouble("centerY");
                double radius = resultSet.getDouble("radius");
                String color = resultSet.getString("color");
                Dot dot = new Dot(centerX, centerY, radius, Color.valueOf(color));
                saveDots.add(dot);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return saveDots;
    }
}
