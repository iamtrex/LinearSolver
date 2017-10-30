package sample;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Solver {
    double DELTA = 1e-8;


    public void print2D(Object o){
        List<Object> list = (List<Object>) o;
        for(Object o2 : list){
            List<Object> inner = (List<Object>) o2;
            for(Object o3 : inner){
                System.out.print(o3 + ",");
            }
            System.out.println();
        }
    }

    public String solveLinearSystem(String userText){
        //Split user input by lines
        variables = new ArrayList<>();
        String[] s = userText.split("\n"); //Split by blank lines.

        List<List<Integer>> matrix = splitEquations(s); //Build a matrix.

        System.out.println("Printing Translated Matrix ");
        print2D(matrix);
        List<List<BigDecimal>> echelonMatrix = makeEchelonForm(matrix); //Create Echelon Form through transformations.

        System.out.println("Printing Echelon Matrix");
        print2D(echelonMatrix);
        //Return the echelonMatrix as a string;
        String ret = "";
        for(int i=0; i<echelonMatrix.size(); i++){
            ret += "\n";
            for(int j=0; j<variables.size(); j++){
                ret += String.valueOf(echelonMatrix.get(i).get(j)) + " " + variables.get(j);
                if(j != variables.size()-1){
                    ret += "+";
                }
            }
            ret += " = " + String.valueOf(echelonMatrix.get(i).get(variables.size()));
        }

        return ret;

    }



    List<String> variables;


    private List<List<BigDecimal>> makeEchelonForm(List<List<Integer>> matrix) {
        List<List<BigDecimal>> deciMatrix = makeBigDecimal(matrix);

        //Last Completed row/column. Starts at -1.
        int rowIndex = -1;
        int colIndex = -1;



        while(true){
            int nextRowCol[] = nextNonZeroCol(deciMatrix, rowIndex, colIndex);
            if(nextRowCol[1] == -1 || (rowIndex+1) == matrix.size()){
                //All columns after current col are now zeros.
                return deciMatrix;
            }else if (nextRowCol[0] != rowIndex+1){
                //the next non-zero is not in the same row, swap the two rows;
                System.out.println("Swapping Rows");
                swapRows(deciMatrix, rowIndex+1, nextRowCol[0]);
            }
            System.out.println("Same Row " + nextRowCol[0] + " " + (rowIndex+1));
            //Given row and column, make all subsequent values in that row 0.
            deciMatrix.set(rowIndex+1, reduceRow(deciMatrix.get(rowIndex+1), nextRowCol[1])); //Reduces current row.

            //Decreases subsequent rows...
            simplifySubsequentRows(deciMatrix, rowIndex+1, nextRowCol[1]);
            rowIndex ++;
            colIndex = nextRowCol[1];
            System.out.println("Fixed row/column: " + rowIndex + " / " + colIndex);
        }
    }

    private void simplifySubsequentRows(List<List<BigDecimal>> deciMatrix, int row, int col) {
        int noRows = deciMatrix.size();
        int noCols = deciMatrix.get(0).size();

        List<BigDecimal> referenceRow = deciMatrix.get(row);
        System.out.println("Cols : " + noCols + " " + referenceRow.size());
        for(int i=row+1; i<noRows; i++){
            if(deciMatrix.get(i).get(col) != BigDecimal.ZERO){
                //Subtract row above to make zero;
                BigDecimal fac = deciMatrix.get(i).get(col);
                for(int j=col; j<noCols; j++){
                    BigDecimal sub = referenceRow.get(j).multiply(fac);
                    BigDecimal temp = deciMatrix.get(i).get(j).subtract(sub);
                    deciMatrix.get(i).set(j, temp);
                }
            }
        }
    }

    private List<BigDecimal> reduceRow(List<BigDecimal> row, int col) {
        BigDecimal value = row.get(col).setScale(0, RoundingMode.HALF_UP);

        if(value != BigDecimal.ONE && value != BigDecimal.ZERO){
            BigDecimal fac = row.get(col);
            System.out.println("Factor " + fac);

            for(int i=col; i<row.size(); i++){
                BigDecimal temp = row.get(i).divide(fac, 4, RoundingMode.HALF_UP);
                row.set(i, temp);
            }
        }
        return row;
    } //Technically don't need this return statement?


    private List<List<BigDecimal>> makeBigDecimal(List<List<Integer>> matrix) {
        List<List<BigDecimal>> deciMatrix = new ArrayList<>();
        for(List<Integer> list : matrix){
            List<BigDecimal> row = new ArrayList<>();
            for(Integer i : list){
                row.add(new BigDecimal(i));
            }
            deciMatrix.add(row);
        }
        return deciMatrix;
    }

    private void swapRows(List<List<BigDecimal>> matrix, int rowIndex, int i) {
        List<BigDecimal> tempA = matrix.get(rowIndex);
        List<BigDecimal> tempB = matrix.get(i);

        matrix.set(i, tempA);
        matrix.set(rowIndex, tempB);
    }

    private int[] nextNonZeroCol(List<List<BigDecimal>> matrix, int lastRow, int lastCol) {
        int col = lastCol+1;

        while(col < matrix.get(0).size()) {
            for (int row = lastRow + 1; row < matrix.size(); row++) {
                if (Math.abs(matrix.get(row).get(col).doubleValue()) >= DELTA){ //Greater than zero.
                    return new int[] {row, col};
                }
            }
            col++;
        }

        return new int[] {-1, -1};
    }

    public List<List<Integer>> splitEquations(String[] data){
        //Analyze each line adn add it to the array:

        List<List<Integer>> matrix = new ArrayList<>();
        List<Integer> bVector = new ArrayList<>();

        for(int i=0; i<data.length; i++){
            List<Integer> row = new ArrayList<>();
            matrix.add(row);
        }
        for(int i=0; i<data.length; i++){
            String line = data[i];

            String split[] = line.split("=");

            analyzeLine(split[0].trim(), matrix, i);
            bVector.add(i, Integer.parseInt(split[1].trim()));

        }

        //Finialize...
        System.out.println("Variable Size " + variables.size());
        for(int i=0; i<matrix.size(); i++){
            matrix.get(i).add(variables.size(), bVector.get(i));
        }

        return matrix;
    }

    private int[][] convertTo2DArray(List<List<Integer>> matrix) {
        int x = matrix.get(0).size();
        int y = matrix.size();

        int[][] array = new int[x][y];
        for(int i=0; i<x; i++){
            for(int j=0; j<y; j++){
                array[i][j] = matrix.get(i).get(j);
            }
        }

        return array;
    }

    private void analyzeLine(String line, List<List<Integer>> matrix, int row){
        String[] parts = line.split("\\+");

        int val = 0;
        boolean negative = false;

        for(String s : parts){
            s = s.trim();
            CharSequence cs = s;
            for(int i=0; i<cs.length(); i++){
                char c = cs.charAt(i);
                if(c == ' ') {
                   //Ignore.
                }else if(isNumber(c)){
                    if(val == 0){
                        val = Integer.parseInt(String.valueOf(c));
                    }else{
                        val = val*10 + Integer.parseInt(String.valueOf(c));
                    }
                }else if(c == '-'){
                    System.out.println("Detected Negative Number");
                    negative = !negative;
                }else{
                    if(val == 0){
                        //System.out.println("You are trying to set a variable to zero, you must be trolling");
                        val = 1;
                    }

                    //Find index of Variable...
                    String v = String.valueOf(c);
                    int index = -1;
                    for (int j = 0; j < variables.size(); j++) {
                        if (variables.get(j).equals(v)) {
                            index = j;
                            break;
                        }
                    }

                    if (index == -1) {
                        addVariable(matrix, v);
                        index = variables.size() - 1;
                        System.out.println("New Index " + index);
                    }

                    System.out.println("Size of Matrix " + matrix.size() + " " +
                            matrix.get(0).size() + " " + matrix.get(row).size());

                    if(negative){
                        val = val * -1;
                    }
                    int old = matrix.get(row).get(index);
                    matrix.get(row).set(index, val + old);
                    val = 0;

                }
            }
        }

    }

    private void addVariable(List<List<Integer>> matrix, String v) {
        System.out.println("Added new variable " + v);
        variables.add(v);

        //Adds a column of zeros.
        for(List<Integer> row : matrix){
            row.add(0);
        }
    }

    private boolean isNumber(char c){
        int i = Character.getNumericValue(c);
        if(i <= 9 && i >= 0){
            return true;
        }
        return false;
    }
}
