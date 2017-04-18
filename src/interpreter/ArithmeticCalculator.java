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

    public static ArithmeticCalculator getInstance(){
        return calInstance;
    }
    
    public int evalInt(String equation) {
        int result = 0;

        try {
            result = (int) Math.floor((double) se.eval(equation));
        } catch (ScriptException ex) {
            Logger.getLogger(ArithmeticCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public float evalFloat(String equation) {
        float result = 0;

        try {
            result = (float) (Math.round((double) se.eval(equation) * 100d) / 100.0d);
        } catch (ScriptException ex) {
            Logger.getLogger(ArithmeticCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }
}
