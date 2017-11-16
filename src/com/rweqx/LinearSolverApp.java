package com.rweqx;

import com.rweqx.model.EchelonReducer;
import com.rweqx.model.InputParser;
import com.rweqx.model.Matrix;
import com.rweqx.view.Logger;
import com.rweqx.view.Window;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class LinearSolverApp extends Application{


    public static void main(String[] args) {
        launch(args);


        //test();
    }
    public static void test(){
        String s = "(5+4) x + y + 3/4 z = 5\n" +
                "2z + 3n + 2/3z = 3/4";
        InputParser IP = new InputParser(s);

        Matrix m = IP.getMatrix();
        Logger.log(m.getStringVersionOfMatrix());

        EchelonReducer ER = new EchelonReducer(m);
        ER.convertToEchelonMatrix();
        Logger.log(m.getStringVersionOfMatrix());
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Window window = new Window();
        window.buildWindow(primaryStage);

        Logger logger = new Logger();
    }
}
