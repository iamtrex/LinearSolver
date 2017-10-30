package com.rweqx.model;

import java.util.ArrayList;
import java.util.List;

public class ElementaryRowOperations {

    public void swapRows(Matrix m, int rowA, int rowB){
        List<Double> tempRowA = m.getRow(rowA);
        List<Double> tempRowB = m.getRow(rowB);
        m.setRow(tempRowA, rowB);
        m.setRow(tempRowB, rowA);

    }

    public void scalarMult(Matrix m, int row, double scal){
        List<Double> temp = m.getRow(row);
        for(int i=0; i<temp.size(); i++){
            temp.set(i, temp.get(i) * scal);
        }

        m.setRow(temp, row);
    }

    public void addMult(Matrix m, int addToRow, int addFromRow, double scal){
        List<Double> addFrom = m.getRow(addFromRow);
        List<Double> addTo = m.getRow(addToRow);

        for(int i=0; i<addTo.size(); i++){
            addTo.set(i, addTo.get(i) + addFrom.get(i) * scal);
        }
        m.setRow(addTo, addToRow);
    }


}
