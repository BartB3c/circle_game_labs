package com.example.circle_game_labs.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class ClientThread extends Thread{
    Socket socket;
    Server server;

    PrintWriter writer;

    public ClientThread(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }
    public void send(String message){
        writer.println(message);
    }
    public void run(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            String message;
            while ((message = bufferedReader.readLine()) != null){
                server.broadcast(message);
            }
            bufferedReader.close();
            writer.close();
            socket.close();
            System.out.println("Closes\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
