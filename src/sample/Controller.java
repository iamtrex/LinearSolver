package sample;

import com.sun.javafx.geom.AreaOp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Controller {
    Set variables;

    public String solveSystem(String text) {
        variables = new HashSet();

        String[] equations = text.split("\n"); // Split by line

        for(String s : equations){
            System.out.println(s);
            s = s.trim();
            evalLine(s);
        }
        System.out.println("Number of Lines " + equations.length);

        return "";

    }

    private void evalLine(String s){
        String[] split = s.split("=");
        if(split.length != 2){
            System.out.println("Fucked Statement :( ");
            throw new IllegalArgumentException();
        }

        //Change to form of:
        // a x1 + b x2 + c x3 + ... +  n xn = z;
        List<String> segements = breakIntoSegments(split[0]);
        List<String> reverseSegments = breakIntoSegments(split[1]);

    }


    //(5+4) x0 + 3x1 + (4*5/6)*x2 = 6 + x3 * (3-2)
    private List<String> breakIntoSegments(String statement){
        String[] split = statement.split("-");
        String reunite = "";
        for(String s : split){
            reunite = "+" + s;
        }

        String[] resplit = statement.split("+"); //Idk why + doesn't work.
        int stateOpen = -1;
        List<String> statements = new ArrayList<String>();


        for(int i=0; i<resplit.length; i++){
            if(resplit[i].contains("(") || resplit[i].contains(")")) {
                if (resplit[i].contains("(")) {
                    stateOpen = i;
                }
                if (resplit[i].contains(")")) {
                    if (stateOpen == i) {
                        stateOpen = -1;
                    } else if (stateOpen == -1) {
                        //Error
                    } else {
                        //Combine into one...
                        statements.add(combineLines(stateOpen, i, resplit));
                    }
                }
            }else{
                statements.add(resplit[i]);
            }
        }

        return statements;

    }

    private String combineLines(int stateOpen, int i, String[] resplit) {
        String s = "";
        for(; stateOpen < i; stateOpen++){
            s = s + resplit[stateOpen];
        }
        return s;
    }

    private BigDecimal evalMath(String s){
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        try {
            Object o = engine.eval(s);
            if(o instanceof Integer){
                return new BigDecimal((Integer) o);
            }else if(o instanceof Double){
                return new BigDecimal((Double) o);
            }else if(o instanceof Float){
                return new BigDecimal((Float) o);
            }else if(o instanceof BigDecimal){
                return (BigDecimal) o;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
