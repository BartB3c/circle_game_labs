module com.example.circle_game_labs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.circle_game_labs to javafx.fxml;
    exports com.example.circle_game_labs;
}