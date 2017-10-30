package com.rweqx.model;

import java.util.List;

public class EchelonReducer {

    private final static double EPSILON = 1e-8; //double imprecision factor.

    ElementaryRowOperations ops;
    Matrix matrix;
    boolean echelon = false;
    boolean redEchelon = false;


    public EchelonReducer(Matrix matrix){
        this.matrix = matrix;
        ops = new ElementaryRowOperations();

    }

    public void convertToEchelonMatrix(){
        if(echelon){
            return;
        }

        //Eval pivots.
        int expectedNextRow = 0;
        int expectedNextCol = 0;
        int row;
        int col = 0;

        int nextPivot[]; //3 index int array, row, col, value;

        while((nextPivot = getNextNonZeroRow(expectedNextRow, expectedNextCol))[0] != -1){
            if(expectedNextRow != nextPivot[0]){
                ops.swapRows(matrix, expectedNextRow, nextPivot[0]);
                nextPivot[0] = expectedNextRow; //Have now moved the row;
            }
            //Now reduce current row
            double val = matrix.getValue(nextPivot[0], nextPivot[1]);
            ops.scalarMult(matrix, nextPivot[0], 1.0/val);

            //Reduce Subsequent rows;
            reduceSubRows(nextPivot[0], nextPivot[1], Direction.DOWN);


            expectedNextCol ++;
            expectedNextRow ++;
        }
        //Done conversion to Echelon form... Cannot simplify further.
    }

    private void reduceSubRows(int row, int col, Direction d) {
        if(d == Direction.DOWN) {
            for (int i = row + 1; i < matrix.getAMatrix().size(); i++) { //All further rows.
                double val = matrix.getValue(i, col);
                if (Math.abs(val) > EPSILON) {
                    double factor = val / matrix.getValue(row, col) * -1;
                    ops.addMult(matrix, i, row, factor);
                }
            }
        }else{
            //TODO - Reduced Echelon Form uses this.
        }
    }

    public void convertToReducedEchelonMatrix(){
        if(!echelon) {
            convertToEchelonMatrix();
        }

    }

    public int[] getNextNonZeroRow(int expRow, int expCol) {
        List<List<Double>> AMatrix = matrix.getAMatrix();

        //Has to be in a col after the expected one...
        for(int j=expCol; j<AMatrix.get(0).size(); j++){
            //Has to be in a row after the expected one...
            for (int i = expRow; i < AMatrix.size(); i++) {
                double val = matrix.getValue(i, j);
                if(Math.abs(val) > EPSILON){
                    //Non-Zero
                    return new int[] {i, j};
                }
            }
        }
        //Ran out of cols without a value greater than 0...
        return new int[] {-1, -1};
    }
}
