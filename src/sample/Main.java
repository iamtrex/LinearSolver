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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    Button calculate;
    TextArea text;
    TextArea tSolved;

    //Controller control;
    Solver solver;
    @Override
    public void start(Stage primaryStage) throws Exception{
        solver = new Solver();

        Group root = new Group();

        VBox box = new VBox(1);
        root.getChildren().add(box);

        text = new TextArea();
        text.setWrapText(true);

        tSolved = new TextArea();
        tSolved.setWrapText(true);

        calculate = new Button("Solve the Linear System");
        calculate.setOnAction(this::calculateSystem);
        box.getChildren().add(text);
        box.getChildren().add(calculate);
        box.getChildren().add(tSolved);
        box.setAlignment(Pos.CENTER);



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void calculateSystem(ActionEvent e) {
        tSolved.setText(solver.solveLinearSystem(text.getText()));
    }
}
