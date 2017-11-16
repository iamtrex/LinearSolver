package com.rweqx.model;


import com.rweqx.view.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

//Builds a matrix from the given input
public class InputParser {
    Matrix matrix;
    public InputParser(String data){
        matrix = new Matrix();
        parse(data); //Parses data into the matrix.
    }

    private void parse(String data){

        String[] lines = data.split("\n"); //Split by lines

        for(String s : lines){
            parseLine(s.trim());
        }
    }
    private void parseLine(String s){
        String[] split = s.split("=");

        if(split.length != 2){
            Logger.log("Line does not contain an equals... skipping");
            return;
        }

        Segment[] splitSegs = splitIntoSegments(split[0].trim());
        matrix.buildLine(splitSegs);
        matrix.addSolution(MathEvaluator.evalMath(split[1].trim()));



    }

    private Segment[] splitIntoSegments(String line){
        char[] chars = line.toCharArray();

        int bracket = -1;
        int div = -1;

        List<Segment> segs = new ArrayList<>();
        String equation = "";
        String variable = "";
        boolean negative = false;

        for(int i=0; i<chars.length; i++){
            char c = chars[i];
            //Logger.log("Eval " + c);
            switch (c){
                case '(':
                    bracket = i;
                    equation += c;
                    break;
                case ')':
                    if(bracket != -1 && div != -1){
                        //Was in both bracket and div, must check order...
                        if(bracket < div){ //Bracket surrounding everything...
                            div = -1;
                        }else{
                            div = -1;
                        }
                    }
                    bracket = -1;
                    equation += c;
                    break;
                case '*':
                    equation += c;
                    if(bracket == -1 && div != -1){
                        div = -1;
                    }
                    break;
                case '/':
                    equation += c;
                    div = i;
                    break;
                case ' ':
                    if(div != -1 && bracket == -1){
                        div = -1;
                    }
                    break;
                case '+':
                    //Same as - .
                case '-':
                    if(equation.equals("") && variable.equals("")){
                        negative = !negative;
                    }else {
                        if (div == -1 && bracket == -1) { //joining of two seperate segments. Add current seg and create new one
                            if (equation.equals("")) {
                                equation = "1";
                            }
                            Logger.log("New Seg " + equation + " and vars " +  variable);
                            addNewSegment(segs, equation, variable, negative);
                            equation = "";
                            variable = "";
                            if (c == '-') {
                                negative = true;
                            } else {
                                negative = false;
                            }
                        } else if (div != -1 && bracket == -1) {
                            //Not in brackets but in divisor...
                            //Break bracket;
                            equation += c;
                            div = -1;
                        } else if (div == -1 && bracket != -1) {
                            //Not in div but in bracket.
                            equation += c;
                        } else if (div != -1 && bracket != -1) {
                            equation += c;
                        }
                    }
                    break;
                default:
                    if(isNumber(c)){
                        Logger.log("Is number" + c);
                        equation += c;
                        Logger.log(equation);
                    }else{
                        if(bracket != -1){
                            Logger.log("Cannot recognize functions in the form of 2(x + y), must be 2x + 2y.");
                        }
                        if(div != -1 && bracket == -1){
                            div = -1;
                        }
                        //Is a variable
                        variable += c;
                    }

            }
        }

        //Last one...
        if(equation.equals("")){
            equation = "1";
        }
        addNewSegment(segs, equation, variable, negative);
        //Add final segment...

        return segs.toArray(new Segment[segs.size()]);
    }

    private void addNewSegment(List<Segment> segs, String equation, String variable, boolean negative) {
        segs.add(new Segment(equation, variable, negative));

    }

    private boolean isNumber(char c){
        int i = Character.getNumericValue(c);
        if(i <= 9 && i >= 0){
            return true;
        }
        return false;
    }

    public Matrix getMatrix(){
        return matrix;
    }


}
