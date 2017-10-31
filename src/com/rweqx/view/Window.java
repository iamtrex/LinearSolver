package com.rweqx.view;

import com.rweqx.model.EchelonReducer;
import com.rweqx.model.InputParser;
import com.rweqx.model.Matrix;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Solver;


public class Window {
    Button calculate;
    TextArea text;
    TextArea tComputingMatrix;
    TextArea tSolved;

    InputParser IP;
    EchelonReducer ER;
    Matrix matrix;

    public Window(){

    }
    public void buildWindow(Stage primaryStage) {
        Group root = new Group();

        VBox box = new VBox(10);

        text = new TextArea();
        text.setWrapText(true);

        tComputingMatrix = new TextArea();
        tComputingMatrix.setWrapText(true);

        tSolved = new TextArea();
        tSolved.setWrapText(true);

        calculate = new Button("Solve the Linear System");
        calculate.setOnAction(this::calculateSystem);
        box.getChildren().add(text);
        box.getChildren().add(calculate);
        box.getChildren().add(tComputingMatrix);
        box.getChildren().add(tSolved);
        box.setAlignment(Pos.CENTER);

        HBox outerBox = new HBox(10);
        outerBox.getChildren().add(box);
        outerBox.setAlignment(Pos.CENTER);

        root.getChildren().add(outerBox);


        Scene scene = new Scene(root, 500, 700);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateSystem(ActionEvent e){
        Platform.runLater( ()->{
            IP = new InputParser(text.getText());
            matrix = IP.getMatrix();
            tComputingMatrix.setText(matrix.getStringVersionOfMatrix());
            ER = new EchelonReducer(matrix);
        //    ER.convertToEchelonMatrix();
            ER.convertToReducedEchelonMatrix();
            tSolved.setText("Echelon Form -\n" + ER.getEchelon().getStringVersionOfMatrix() +
                    "Reduced Echelon Form -\n" + ER.getRedEchelonForm().getStringVersionOfMatrix());

        });
    }
}
