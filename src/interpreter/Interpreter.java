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
        String s, tempVal, type = "", name, val;
        Variable var, tempVar;
        Pattern p = Pattern.compile("[a-z]");
        Matcher m;
        boolean canPerform, insideLoop = false;
        ArrayList<String> statements = new ArrayList<>();
        ArrayList<String> bracketStack = new ArrayList<>();
        ArrayList<String> loopBracket = new ArrayList<>();
        int j, forIndex = 0;

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
                if (insideLoop) {
                    if (s.contains("OPENB")) {
                        loopBracket.add("{");
                    } else if (s.contains("CLOSEB")) {
                        loopBracket.remove(loopBracket.size() - 1);
                    }
                }
                System.out.println("loop = " + loopBracket);
                //loop
                if (insideLoop && loopBracket.isEmpty()) {
                    i = forIndex;
                    s = statements.get(i);
                }

                // System.out.println(line);

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
                    loopBracket.clear();

                    //get value of counter
                    if (!insideLoop) {
                        //skip '('
                        i += 2;
                        s = statements.get(i);
                        //store counter value
                        var = symbolTable.get(s.split(",")[1]);

                        //skip '='
                        i += 2;
                        s = statements.get(i);
                        var.setValue(Integer.parseInt(s.split(",")[1]));

                        insideLoop = true;
                        i++;
                        s = statements.get(i);
                    } else {
                        //if already inside loop, skip initialization
                        while (!s.contains(";")) {
                            i++;
                            s = statements.get(i);
                        }
                    }
                    System.out.println("after = " + s);

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
                            insideLoop = false;

                            //go to increment
                            i++;
                        }
                    }

                    //increment counter
                    if (insideLoop) {
                        i++;
                        s = statements.get(i);
                        var = symbolTable.get(s.split(",")[1]);
                        if (var.getValue() != null) {
                            if (var.getType().equals("int")) {
                                var.setValue((int) var.getValue() + 1);
                            } else {
                                var.setValue((float) var.getValue() + 1);
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
                        loopBracket.add("{");
                    } else {
                        //if not inside loop, skip whole for loop
                        while (!s.contains("{")) {
                            i++;
                            s = statements.get(i);
                        }
                        loopBracket.add("{");
                        while (!loopBracket.isEmpty()) {
                            i++;
                            s = statements.get(i);
                            if (s.contains("{")) {
                                loopBracket.add("{");
                            } else if (s.contains("}")) {
                                loopBracket.remove(loopBracket.size() - 1);
                            }
                        }
                    }
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
                out.println("VAR, " + var.getType() + ", " + var.getName() + ", " + var.getValue());
            }
            out.println("===");
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Interpreter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
