package com.example.circle_game_labs;

import javafx.scene.paint.Color;

public record Dot(double centerX, double centerY, double radius, Color color) {
    public static String toMessage(Dot dot){
        return dot.centerX + " " + dot.centerY + " " + dot.radius + "," + dot.color.toString();

    }

    public static Dot FromMessage(String message){
        String parts[] = message.split(",");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double radius = Double.parseDouble(parts[2]);
        Color color = Color.valueOf(parts[0]);
        return new Dot(x,y,radius,color);
    }
}
