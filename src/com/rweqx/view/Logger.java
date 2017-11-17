package com.rweqx.view;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;

public class Logger {

    private Stage stage;
    private static TextArea text;

    public Logger(){

        stage = new Stage();

        Group root = new Group();
        Scene scene = new Scene(root, 400, 200);


        text = new TextArea("");
        text.setEditable(false);
        text.setWrapText(true);
        text.setPrefSize(400, 200);
        root.getChildren().add(text);

        stage.setScene(scene);
        stage.show();
//
    }
    public static void log(String s) {

        System.out.println(s);

        Platform.runLater(()-> text.setText(text.getText()  + s + "\n"));

    }
    //Logs shit. 
}
