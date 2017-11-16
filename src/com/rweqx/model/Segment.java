package com.rweqx.model;

import com.rweqx.view.Logger;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.NumberFormat;

public class Segment {

    String expression;
    String variable;

    boolean negative;

    public Segment(String expression, String variable, boolean negative){
        this.negative = negative;
        this.expression = expression;
        this.variable = variable;
    }


    public double getDouble() {
        Logger.log("Expression " + expression);
        double d = MathEvaluator.evalMath(expression);

        Logger.log("Result " + d);
        if(negative){
            return -1 * d;
        }else{
            return d;
        }
    }

    public String getVar() {
        return variable;
    }
}
