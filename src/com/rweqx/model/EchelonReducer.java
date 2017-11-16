package com.rweqx.model;

import java.util.List;

public class EchelonReducer {

    private final static double EPSILON = 1e-8; //double imprecision factor.


    private ElementaryRowOperations ops;
    private Matrix matrix;
    private Matrix original;
    private Matrix echelonForm;
    private Matrix redEchelonForm;

    boolean echelon = false;
    boolean redEchelon = false;

    public Matrix getEchelon(){
        return echelonForm;
    }

    public Matrix getRedEchelonForm(){
        return redEchelonForm;
    }

    public EchelonReducer(Matrix matrix){
        this.matrix = new Matrix(matrix); //Copy. Deletes previous reference to the matrix created by InputParser.
        this.original = new Matrix(matrix); //Copy.
        ops = new ElementaryRowOperations();
    }

    public void convertToEchelonMatrix(){
        if(echelon){
            return;
        }

        //Eval pivots.
        int expectedNextRow = 0;
        int expectedNextCol = 0;


        int nextPivot[]; //3 index int array, row, col;

        while((nextPivot = getNextNonZeroRow(expectedNextRow, expectedNextCol, Direction.DOWN))[0] != -1){
            if(expectedNextRow != nextPivot[0]){
                ops.swapRows(matrix, expectedNextRow, nextPivot[0]);
                nextPivot[0] = expectedNextRow; //Have now moved the row;
            }
            //Now reduce current row
            reduceRow(nextPivot);
            //Reduce Subsequent rows;
            reduceSubRows(nextPivot[0], nextPivot[1], Direction.DOWN);


            expectedNextCol ++;
            expectedNextRow ++;
        }
        //Done conversion to Echelon form... Cannot simplify further.
        echelon = true;
        echelonForm = new Matrix(matrix);
    }

    private void reduceRow(int[] nextPivot){
        double val = matrix.getValue(nextPivot[0], nextPivot[1]);
        if(Math.abs(val - 1.0) > EPSILON){
            ops.scalarMult(matrix, nextPivot[0], 1.0/val);
        }
    }


    private void reduceSubRows(int row, int col, Direction d) {
        if(d == Direction.DOWN) {
            for (int i = row + 1; i < matrix.getAMatrix().size(); i++) { //All further rows.
                /*
                double val = matrix.getValue(i, col);
                if (Math.abs(val) > EPSILON) {
                    double factor = val / matrix.getValue(row, col) * -1;
                    ops.addMult(matrix, i, row, factor);
                }
                */
                reduceAbyB(i, col, row, col);
            }
        }else{
            //TODO - Reduced Echelon Form uses this.
            for(int i = row - 1; i > -1; i--){
                reduceAbyB(i, col, row, col);
            }
        }
    }
    private void reduceAbyB(int aRow, int aCol, int bRow, int bCol){
        double val = matrix.getValue(aRow, aCol);
    //    if(Math.abs(val) > EPSILON) {
            double factor = val / matrix.getValue(bRow, bCol) * -1;
            ops.addMult(matrix, aRow, bRow, factor);
    //    }
    }

    public void convertToReducedEchelonMatrix(){
        if(!echelon) {
            convertToEchelonMatrix();
        }else if(redEchelon){
            return;
        }

        //Eval pivots.
        int expectedNextRow = matrix.getAMatrix().size() - 1;
        int expectedNextCol = matrix.getAMatrix().get(0).size() - 1;

        int nextPivot[]; //2 index int array, row, col;

        while((nextPivot = getNextNonZeroRow(expectedNextRow, expectedNextCol, Direction.UP))[0] != -1){
            if(expectedNextRow != nextPivot[0]){
                ops.swapRows(matrix, expectedNextRow, nextPivot[0]);
                nextPivot[0] = expectedNextRow; //Have now moved the row;
            }
            //Now reduce current row
            reduceRow(nextPivot);
            //Reduce Subsequent rows;
            reduceSubRows(nextPivot[0], nextPivot[1], Direction.UP);


            expectedNextCol --;
            expectedNextRow --;
        }

        redEchelonForm = new Matrix(matrix);
        redEchelon = true;
    }

    private int[] getNextNonZeroRow(int expRow, int expCol, Direction d) {
        if(d == Direction.DOWN) {
            List<List<Double>> AMatrix = matrix.getAMatrix();

            //Has to be in a col after the expected one...
            for (int j = expCol; j < AMatrix.get(0).size(); j++) {
                //Has to be in a row after the expected one...
                for (int i = expRow; i < AMatrix.size(); i++) {
                    double val = matrix.getValue(i, j);
                    if (Math.abs(val) > EPSILON) {
                        //Non-Zero
                        return new int[]{i, j};
                    }
                }
            }
            //Ran out of cols without a value greater than 0...
            return new int[]{-1, -1};
        }else{
            //Has to be in a col before the expected one...
            for (int j = expCol; j > 0; j--) {
                 //Has to be in a row before after the expected one...
                for (int i = expRow; i > 0; i--) {
                    double val = matrix.getValue(i, j);
                    if (Math.abs(val) > EPSILON) {
                        //Non-Zero
                        return new int[]{i, j};
                    }
                }
            }
            return new int[]{-1, -1};
        }
    }
}
