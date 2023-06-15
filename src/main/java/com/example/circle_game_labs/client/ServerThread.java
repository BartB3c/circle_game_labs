package com.example.circle_game_labs.client;

import com.example.circle_game_labs.Dot;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ServerThread extends Thread {
    Socket socket;
    PrintWriter writer;

    BufferedReader reader;
    Consumer<Dot> dots;

    public void setDots(Consumer<Dot> consumer) {
        this.dots = dots;
    }

    public ServerThread(String address, int port){
            try {
                socket = new Socket(address, port);
                OutputStream output = socket.getOutputStream();
                writer = new PrintWriter(output, true);
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    public void run() {
        try {
         String message;
         while((message = reader.readLine()) != null){
             dots.accept(Dot.FromMessage(message));
         }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(double centerX, double centerY, double radius, Color color) {
        writer.println(centerX + " " + centerY + " " + radius + " " + color);
    }
}
