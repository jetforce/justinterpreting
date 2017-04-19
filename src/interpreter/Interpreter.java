/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Arces
 */
public class Interpreter {

    private ArithmeticCalculator calc;
    private BufferedReader br;
    private Hashtable<String, Variable> symbolTable;

    public Interpreter() {
        this.calc = ArithmeticCalculator.getInstance();
        this.symbolTable = new Hashtable<>();
    }

    public void interpret() {
        Scanner sc = new Scanner(System.in);
        String s, tempVal, type = "", name, val, cond;
        Variable var, tempVar;
        Pattern p = Pattern.compile("[a-z]");
        Matcher m;
        boolean canPerform, insideFor = false, insideSwitch = false, insideWhile = false;
        ArrayList<String> statements = new ArrayList<>();
        ArrayList<String> bracketStack = new ArrayList<>();
        ArrayList<String> forBracket = new ArrayList<>();
        ArrayList<String> whileBracket = new ArrayList<>();
        ArrayList<String> switchBracket = new ArrayList<>();
        ArrayList<String> printVars = new ArrayList<>();
        int j, forIndex = 0, whileIndex = 0;

        try {
            br = new BufferedReader(new FileReader(new File("output.txt")));

            /* s -> statement */
            while ((s = br.readLine()) != null) {
//                System.out.println(line);
                if (!s.contains("WHITESPACE")) {
                    statements.add(s);
                }
            }

            for (int i = 0; i < statements.size(); i++) {
                s = statements.get(i);
                if (insideFor) {
                    if (s.contains("OPENB")) {
                        forBracket.add("{");
                    } else if (s.contains("CLOSEB")) {
                        forBracket.remove(forBracket.size() - 1);
                    }
                }

                if (insideWhile) {
                    //System.out.println("WHILE: " + s + " " + whileBracket);
                    if (s.contains("OPENB")) {
                        whileBracket.add("{");
                    } else if (s.contains("CLOSEB")) {
                        whileBracket.remove(whileBracket.size() - 1);
                    }
                }
                //System.out.println("loop = " + loopBracket);
                //loop
                if (insideFor && forBracket.isEmpty()) {
                    i = forIndex;
                    s = statements.get(i);
                }

                if (insideWhile && whileBracket.isEmpty()) {
                    i = whileIndex;
                    s = statements.get(i);
                }

                // System.out.println(line);
                /* ===========================================================================
                 If a printf is seen WIP
                 ================================================ */
                if (s.contains("PRINTF")) {
                    //                   System.out.println("PRINTF S IS " + s);
                    //skip '('
                    i += 2;
                    s = statements.get(i);

                    val = s.split(",")[1].substring(1, s.split(",")[1].length() - 1);

                    i++;
                    s = statements.get(i);

                    //if there are values
                    if (s.contains("COMMA")) {
                        printVars.clear();
                        while (!s.contains("CLOSEP")) {
                            i++;
                            s = statements.get(i);

                            if (s.contains("VAR")) {
                                printVars.add(s.split(",")[1]);
                            }
                        }

                        System.out.println("VAR TO PRINT = " + printVars.size());
                        for (String v : printVars) {
                            var = symbolTable.get(v);

                            if (var != null) {
                                if(var.getType().equals("int")){
                                    val = val.replaceFirst("%d", (var.getValue() != null ? var.getValue().toString() : "undefined"));
                                } else if(var.getType().equals("float")){
                                    val = val.replaceFirst("%f", (var.getValue() != null ? var.getValue().toString() : "undefined"));
                                }
                            } else {
                                System.out.println("ERROR: " + s.split(",")[1] + " is undefined.");
                            }
                        }
                        
                        
                    }

                    System.out.println("PRINTF VAL: " + s);
                    /* dont delete */
                    System.out.println(val);
                }

                /* ===========================================================================
                 If a scanf is seen WIP
                 ================================================ */
                if (s.contains("SCANF")) {
                    System.out.println("SCANF S IS " + s);
                    //skip '('
                    i += 2;
                    s = statements.get(i);

                    val = s.split(",")[1].substring(1, s.split(",")[1].length() - 1);

                    //skip ',' get variable
                    i++;
                    s = statements.get(i);
                    System.out.println("SCAANF: " + s);
                    //tempVal 
                    if (val.equals("%d")) {

                    }
                    System.out.println(val);
                }

                /* ===========================================================================
                 If a declaration is seen
                 ================================================ */
                if (s.contains("TINT")) {
                    type = "int";
                } else if (s.contains("TFLOAT")) {
                    type = "float";
                }

                /* ===========================================================================
                 If a variable is seen
                 ================================================ */
                if (s.contains("VAR")) {
                    val = "";
                    canPerform = true;
                    name = s.split(",")[1];

                    //check if var already has value
                    var = symbolTable.get(name);
                    if (var == null) {
                        System.out.println("var created!" + name);
                        //if var doesnt have a value, store in symbol table
                        var = new Variable();
                        var.setType(type);
                        var.setName(name);

                        symbolTable.put(name, var);
                    }

                    //iterate to move to next
                    i++;
                    if (i < statements.size()) {
                        s = statements.get(i);

                        //if '=' is detected, an assignment is happening
                        if (s.contains("EQUALS")) {
                            j = i + 1;
                            tempVal = statements.get(j);

                            //append texts to val
                            while (!tempVal.contains(";") && j < statements.size()) {
                                //  System.out.println("line = " + tempVal);
                                val += tempVal.split(",")[1];
                                j++;
                                tempVal = statements.get(j);
                            }

                            System.out.println("val = " + val);
                            val = val.trim();

                            //check if val contains other variables
                            m = p.matcher(val);

                            //replace all variables with their values
                            while (m.find()) {
                                tempVar = symbolTable.get(m.group());
                                if (tempVar != null) {
                                    val = val.replace(m.group(), tempVar.getValue().toString());
                                } else {
                                    System.out.println("ERROR: " + m.group() + " is undefined.");
                                    canPerform = false;
                                }
                            }

                            if (canPerform) {
                                if (var.getType().equals("int")) {
                                    var.setValue((Integer) calc.evalInt(val));
                                } else {
                                    var.setValue((Float) calc.evalFloat(val));
                                }

                                symbolTable.put(name, var);
                                dumpUpdate();
                            }
                        } else if (s.contains("ADD") && statements.get(i + 1).contains("ADD")) {
                            i++;

                            if (var.getValue() != null) {
                                if (var.getType().equals("int")) {
                                    var.setValue((int) var.getValue() + 1);
                                } else {
                                    var.setValue((float) var.getValue() + 1);
                                }
                                symbolTable.put(name, var);
                                dumpUpdate();
                            } else {
                                System.out.println("ERROR: " + name + " is undefined.");
                            }
                        } else if (s.contains("SUBTRACT") && statements.get(i + 1).contains("SUBTRACT")) {
                            i++;

                            if (var.getValue() != null) {
                                if (var.getType().equals("int")) {
                                    var.setValue((int) var.getValue() - 1);
                                } else {
                                    var.setValue((float) var.getValue() - 1);
                                }
                                symbolTable.put(name, var);
                                dumpUpdate();
                            } else {
                                System.out.println("ERROR: " + name + " is undefined.");
                            }
                        }
                    }
                }

                /* ===========================================================================
                 If an if is seen
                 ================================================ */
                if (s.contains("IF")) {
                    val = "";
                    canPerform = true;
                    bracketStack.clear();
                    //get condition inside ()

                    i++;
                    s = statements.get(i);
                    j = i + 1;

                    tempVal = statements.get(j);
                    while (!tempVal.contains("CLOSEP") && j < statements.size()) {
                        System.out.println("temp = " + tempVal);
                        val += tempVal.split(",")[1];
                        j++;
                        tempVal = statements.get(j);
                    }

                    val = val.trim();

                    //check if val contains other variables
                    m = p.matcher(val);

                    //replace all variables with their values
                    while (m.find()) {
                        tempVar = symbolTable.get(m.group());
                        if (tempVar != null) {
                            val = val.replace(m.group(), tempVar.getValue().toString());
                        } else {
                            System.out.println("ERROR: " + m.group() + " is undefined.");
                            canPerform = false;
                        }
                    }
                    if (canPerform) {
                        //move to first bracket of if
                        while (!s.contains("OPENB")) {
                            i++;
                            s = statements.get(i);
                        }
                        bracketStack.add("{");

                        System.out.println(val + "?: " + calc.evalCond(val));
                        //if the condition is false, keep iterating until the else statement
                        if (!calc.evalCond(val)) {
                            while (!bracketStack.isEmpty()) {
//                                System.out.println("bracket count = " + bracketStack.size() + " | s: " + s);
                                i++;
                                s = statements.get(i);

                                if (s.contains("OPENB")) {
                                    bracketStack.add("{");
                                } else if (s.contains("CLOSEB")) {
                                    bracketStack.remove(bracketStack.size() - 1);
                                }
                            }

                            //go to else token
                            i++;
                        }
                    }
                    /* ===========================================================================
                     If an else is seen, skip it
                     ================================================ */
                } else if (s.contains("ELSE")) {
                    bracketStack.clear();
                    //move to first bracket of the else
                    while (!s.contains("OPENB")) {
                        i++;
                        s = statements.get(i);
                    }
                    bracketStack.add("{");

                    while (!bracketStack.isEmpty()) {
//                        System.out.println("bracket count = " + bracketStack.size() + " | s: " + s);
                        i++;
                        s = statements.get(i);

                        if (s.contains("OPENB")) {
                            bracketStack.add("{");
                        } else if (s.contains("CLOSEB")) {
                            bracketStack.remove(bracketStack.size() - 1);
                        }
                    }
                }

                /* ===========================================================================
                 If a for is seen
                 ================================================ */
                if (s.contains("FOR")) {
                    System.out.println("FOR HERE");
                    forIndex = i; //index for condition
                    canPerform = true;
                    forBracket.clear();

                    //get value of counter
                    if (!insideFor) {
                        //skip '('
                        i += 2;
                        s = statements.get(i);
                        //store counter value
                        var = symbolTable.get(s.split(",")[1]);

                        //skip '='
                        i += 2;
                        s = statements.get(i);
                        var.setValue(Integer.parseInt(s.split(",")[1]));

                        insideFor = true;
                        i++;
                        s = statements.get(i);
                    } else {
                        //if already inside loop, skip initialization
                        while (!s.contains(";")) {
                            i++;
                            s = statements.get(i);
                        }
                    }
                    //System.out.println("after = " + s);

                    val = "";
                    j = i + 1;
                    tempVal = statements.get(j);

                    //get condition
                    while (!statements.get(j).contains(";") && j < statements.size()) {
                        val += tempVal.split(",")[1];
                        j++;
                        tempVal = statements.get(j);
                    }

                    //check if val contains other variables
                    m = p.matcher(val);

                    //replace all variables with their values
                    while (m.find()) {
                        tempVar = symbolTable.get(m.group());
                        if (tempVar != null) {
                            val = val.replace(m.group(), tempVar.getValue().toString());
                        } else {
                            System.out.println("ERROR: " + m.group() + " is undefined.");
                            canPerform = false;
                        }
                    }
                    if (canPerform) {
                        //if the condition is false, exit from loop
                        if (!calc.evalCond(val)) {
                            insideFor = false;

                            //go to increment
                            i++;
                        }
                    }

                    //increment counter
                    if (insideFor) {
                        i++;
                        s = statements.get(i);

                        while (!s.contains(";")) {
                            i++;
                            s = statements.get(i);
                        }

                        i++;
                        s = statements.get(i);

                        var = symbolTable.get(s.split(",")[1]);

                        i++;
                        s = statements.get(i);

                        System.out.println("TEST: " + statements.get(i + 1));

                        if (var.getValue() != null) {
                            if (var.getType().equals("int")) {
                                if (s.contains("ADD") && statements.get(i + 1).contains("ADD")) {
                                    var.setValue((int) var.getValue() + 1);
                                } else if (s.contains("SUBTRACT") && statements.get(i + 1).contains("SUBTRACT")) {
                                    var.setValue((int) var.getValue() - 1);
                                }
                            } else {
                                if (s.contains("ADD") && statements.get(i + 1).contains("ADD")) {
                                    var.setValue((float) var.getValue() + 1);
                                } else if (s.contains("SUBTRACT") && statements.get(i + 1).contains("SUBTRACT")) {
                                    var.setValue((float) var.getValue() - 1);
                                }
                            }
                            symbolTable.put(var.getName(), var);
                            dumpUpdate();
                        } else {
                            System.out.println("ERROR: " + var.getName() + " is undefined.");
                        }

                        //go to for bracket
                        while (!s.contains(")")) {
                            i++;
                            s = statements.get(i);
                        }
                        i++;
                        forBracket.add("{");
                    } else {
                        //if not inside loop, skip whole for loop
                        while (!s.contains("{")) {
                            i++;
                            s = statements.get(i);
                        }
                        forBracket.add("{");
                        while (!forBracket.isEmpty()) {
                            i++;
                            s = statements.get(i);
                            if (s.contains("{")) {
                                forBracket.add("{");
                            } else if (s.contains("}")) {
                                forBracket.remove(forBracket.size() - 1);
                            }
                        }
                    }
                }

                /* ===========================================================================
                 If a while is seen
                 ================================================ */
                if (s.contains("WHILE")) {
                    val = "";
                    canPerform = true;
                    whileBracket.clear();

                    if (!insideWhile) {
                        insideWhile = true;
                        whileIndex = i;

                    }

                    //get condition inside ()
                    i++;
                    s = statements.get(i);
                    j = i + 1;

                    tempVal = statements.get(j);
                    while (!tempVal.contains("CLOSEP") && j < statements.size()) {
                        val += tempVal.split(",")[1];
                        j++;
                        tempVal = statements.get(j);
                    }

                    val = val.trim();

                    //check if val contains other variables
                    m = p.matcher(val);

                    //replace all variables with their values
                    while (m.find()) {
                        tempVar = symbolTable.get(m.group());
                        if (tempVar != null) {
                            val = val.replace(m.group(), tempVar.getValue().toString());
                        } else {
                            System.out.println("ERROR: " + m.group() + " is undefined.");
                            canPerform = false;
                        }
                    }
                    if (canPerform) {
                        //move to first bracket of while
                        while (!s.contains("OPENB")) {
                            i++;
                            s = statements.get(i);
                        }

                        whileBracket.add("{");

                        System.out.println("while: " + val + "?: " + calc.evalCond(val));
                        //if the condition is false, keep iterating until the else statement
                        if (!calc.evalCond(val)) {
                            insideWhile = false;
                            while (!whileBracket.isEmpty()) {
//                                System.out.println("bracket count = " + whileBracket.size() + " | s: " + s);
                                i++;
                                s = statements.get(i);

                                if (s.contains("OPENB")) {
                                    whileBracket.add("{");
                                } else if (s.contains("CLOSEB")) {
                                    whileBracket.remove(whileBracket.size() - 1);
                                }
                            }

                            //go to else token
                            i++;
                        }
                    }
                }

                /* ===========================================================================
                 If a switch is seen
                 ================================================ */
                if (s.contains("SWITCH")) {
                    val = "";
                    switchBracket.clear();

                    System.out.println("IM A SWITCCH");
                    //skip '('
                    i += 2;
                    s = statements.get(i);
                    while (!s.contains(")")) {
                        val += s.split(",")[1];
                        i++;
                        s = statements.get(i);
                    }
                    System.out.println("valllllll = " + val);
                    i++;
                    s = statements.get(i);

                    switchBracket.add("{");
                    //case, default or equal brackets
                    //while end of switch is not seen, search for a case

                    while (!insideSwitch && !switchBracket.isEmpty() && !s.contains("DEFAULT")) {
                        i++;
                        s = statements.get(i);
                        if (s.contains("OPENB")) {
                            switchBracket.add("{");
                        } else if (s.contains("CLOSEB")) {
                            switchBracket.remove(switchBracket.size() - 1);
                        }

                        //if s contains case, get value
                        if (s.contains("CASE")) {
                            i++;
                            s = statements.get(i);
                            tempVal = s.split(",")[1];

                            //compare var val with case val
                            cond = val + " === " + tempVal;

                            //check if val contains other variables
                            m = p.matcher(cond);

                            //replace all variables with their values
                            while (m.find()) {
                                tempVar = symbolTable.get(m.group());
                                if (tempVar != null) {
                                    cond = cond.replace(m.group(), tempVar.getValue().toString());
                                } else {
                                    System.out.println("ERROR: " + m.group() + " is undefined.");
                                    canPerform = false;
                                }
                            }
                            if (calc.evalCond(cond)) {
                                System.out.println("HEHEHE I AM TRUE");
                                insideSwitch = true;
                            }
                        }
                    }

                    if (s.contains("DEFAULT")) {
                        System.out.println("I AM DEFAULT");
                        insideSwitch = true;
                    }
                    System.out.println("S IS = " + s);
                }

                /* ===========================================================================
                 If a break is seen while inside switch
                 ================================================ */
                if (insideSwitch && s.contains("BREAK")) {
                    insideSwitch = false;
                    while (!switchBracket.isEmpty()) {
//                                System.out.println("bracket count = " + bracketStack.size() + " | s: " + s);
                        i++;
                        s = statements.get(i);

                        if (s.contains("OPENB")) {
                            switchBracket.add("{");
                        } else if (s.contains("CLOSEB")) {
                            switchBracket.remove(switchBracket.size() - 1);
                        }
                    }
                    System.out.println("END BREAK: S IS " + s);
                }

            }
            Set<String> keys = symbolTable.keySet();
            System.out.println("==================\nVARIABLES IN TABLE\n==================");
            for (String key : keys) {
                System.out.println("[" + symbolTable.get(key).getType() + "] " + key + " = " + symbolTable.get(key).getValue());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void dumpUpdate() {
        PrintWriter out;
        Variable var;
        System.out.println("=== DUMP ===");
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter("Interpreter Dump.txt", true)));
            Set<String> keys = symbolTable.keySet();
            for (String key : keys) {
                System.out.println("[" + symbolTable.get(key).getType() + "] " + key + " = " + symbolTable.get(key).getValue());
                var = symbolTable.get(key);
                out.println("VAR, " + var.getType() + ", " + var.getName() + ", " + (var.getValue() != null ? var.getValue() : "undefined"));
            }
            out.println("===");
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
