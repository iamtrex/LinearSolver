package com.rweqx.model;

import com.rweqx.view.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Type for Matrix.
 */
public class Matrix {

    private static final double EPSILON = 1e-8;

    List<List<Double>> matrix; // The Matrix.
    Map<Integer, String> variableNames;
    List<Double> solutionsFor; // Solutions

    private int varIndex = 0;

    public Matrix(){
        matrix = new ArrayList<>();

        variableNames = new HashMap<>();

        solutionsFor = new ArrayList<>();
    }

    //Deep Copy of Matrix.
    public Matrix(List<List<Double>> matrix, Map<Integer, String> variableNames, List<Double> solutionsFor, int varIndex) {
        this.matrix = new ArrayList<>();
        this.variableNames = new HashMap<>();
        this.solutionsFor = new ArrayList<>();

        this.variableNames.putAll(variableNames);

        for(double d : solutionsFor){
            this.solutionsFor.add(d);
        }
        for(int i=0; i<matrix.size(); i++){
            List<Double> temp = new ArrayList<>();
            for(double j : matrix.get(0)){
                temp.add(j);
            }
            this.matrix.add(temp);
        }


    }

    public Matrix makeCopy(){
        return new Matrix(matrix, variableNames, solutionsFor, varIndex);
    }

    //Checks if there is a problem with the formed matrix. Should always return true;
    public boolean properForm(){
        if(matrix.size() != solutionsFor.size()){
            return false;
        }else if(matrix.get(0).size() != matrix.get(matrix.size()-1).size()
                || matrix.get(0).size() != variableNames.size()){
            return false;
        }

        return true;
    }




    public void addVariable(String s){
        Logger.log("Creating new variable at index " + varIndex);

        variableNames.put(varIndex, s);
        for(List<Double> temp : matrix){
            temp.add(varIndex, 0.0); //Adding a new column to the matrix.
        }

        varIndex ++;
    }

    public void addNewLine(){
        //add new line
        Logger.log("Adding new line");
        List<Double> newLine = new ArrayList<>();

        if(matrix.size() != 0){
            int len = matrix.get(0).size();
            for(int i=0; i<len; i++) {
                newLine.add(i, 0.0);
            }
        }
        matrix.add(newLine);

    }

    //Returns index from variable name;
    private int getIndex(String varName){
        if(variableNames.containsValue(varName)){
            Logger.log("Contains Variable " + varName);
            for(Map.Entry<Integer, String> entry : variableNames.entrySet()){
                if(entry.getValue().equals(varName)){
                    return entry.getKey();
                }
            }
            throw new IllegalStateException("This is impossible.");
        }else{
            Logger.log("Does not contain variable " + varName + " creating new column.");
            //Add variable.
            int i = variableNames.size();
            addVariable(varName);
            return i;
        }
    }


    public void setValue(int row, int col, double value){
        matrix.get(row).set(col, value);

    }

    public void addToValue(int row, int col, double value){
        matrix.get(row).set(col, matrix.get(row).get(col) + value);
    }

    public void addSolution(double d){
        solutionsFor.add(d);
    }

    public Double getSolution(int row){
        return solutionsFor.get(row);
    }
    public Double getValue(int row, int col){
        return matrix.get(row).get(col);
    }


    public void buildLine(Segment[] splitSegs) {
        int row = matrix.size();
        addNewLine();

        for(Segment seg : splitSegs){
            addToValue(row, getIndex(seg.getVar()), seg.getDouble());
        }
    }

    public List<List<Double>> getAMatrix(){
        return matrix;
    }

    public Map<Integer, String> getVariables() {
        return variableNames;
    }

    public List<Double> getSolFor(){
        return solutionsFor;
    }

    public List<Double> getRow(int row) {
        List<Double> l = new ArrayList<>(matrix.get(row));
        l.add(solutionsFor.get(row));
        return l;
    }
    public void setRow(List<Double> l, int row){
        for(int i=0; i<l.size()-1; i++){
            setValue(row, i, l.get(i));
        }

        solutionsFor.set(row, l.get(l.size()-1));
    }


    public String getStringVersionOfMatrix(){
        String s = "";
        for(int i=0; i<matrix.size(); i++){
            for(int j=0; j<matrix.get(i).size(); j++){
                double val = matrix.get(i).get(j);

                String tail = " + ";
                if(j == matrix.get(i).size()-1){ //Last variable already... don't add extra + sign.
                    tail = "";
                }
                //if(Math.abs(val) > EPSILON){
                    s+= val + variableNames.get(j) + tail; //Add variable for the respective column J
                //}
            }
            //Finished row.
            s += " = " + solutionsFor.get(i) + "\n";
        }
        return s;
    }
}

