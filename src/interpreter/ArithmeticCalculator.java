/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Arces
 */
public class ArithmeticCalculator {

    private ScriptEngineManager sem;
    private ScriptEngine se;
    private static ArithmeticCalculator calInstance = new ArithmeticCalculator();

    private ArithmeticCalculator() {
        this.sem = new ScriptEngineManager();
        this.se = sem.getEngineByName("JavaScript");
    }

    public static ArithmeticCalculator getInstance() {
        return calInstance;
    }

    public int evalInt(String equation) {
//        System.out.println("[int] evaluating " + equation);
        int result = 0;
        Object eval;
        try {
            eval = se.eval(equation);
            if (eval.toString().contains(".")) {
                result = (int) Math.floor((double) eval);
            } else {
                result = (int) eval;
            }
        } catch (ScriptException ex) {
            Logger.getLogger(ArithmeticCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public float evalFloat(String equation) {
//        System.out.println("[float] evaluating " + equation);
        float result = 0;
        Object eval;

        try {
            eval = se.eval(equation);
            if (eval.toString().contains(".")) {
                result = (float) (Math.round((double) eval * 100d) / 100.0d);
            } else {
                result = (int) eval / (float) 1;
            }
        } catch (ScriptException ex) {
            Logger.getLogger(ArithmeticCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public boolean evalCond(String cond) {
        //System.out.println("[condition] evaluating " + cond);
        boolean isTrue = true;

        try {
            isTrue = (boolean) se.eval(cond);
            
        } catch (ScriptException ex) {
            Logger.getLogger(ArithmeticCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isTrue;
    }
}
