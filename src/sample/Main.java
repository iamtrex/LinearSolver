package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    Button calculate;
    TextArea text;
    TextArea tComputingMatrix;
    TextArea tSolved;

    //Controller control;
    Solver solver;
    @Override
    public void start(Stage primaryStage) throws Exception{
        solver = new Solver();

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


    public static void main(String[] args) {
        launch(args);
    }

    private void calculateSystem(ActionEvent e) {
        tSolved.setText(solver.solveLinearSystem(text.getText()));
    }
}
