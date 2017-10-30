package com.rweqx.model;

import com.rweqx.view.Logger;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MathEvaluator {
    public static double evalMath(String expression){
        //Logger.log("Building result " + expression);
        Expression e = new ExpressionBuilder(expression)
                .build();
        double result = e.evaluate();
        //Logger.log("Result " + result);
        return result;
    }
}
