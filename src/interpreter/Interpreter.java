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
        String line, tempVal, type = "", name, val;
        Variable var, tempVar;
        Pattern p = Pattern.compile("[a-z]");
        Matcher m;
        boolean canPerform;
        ArrayList<String> statements = new ArrayList<>();
        int j = 0;

        try {
            br = new BufferedReader(new FileReader(new File("output.txt")));

            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                if (!line.contains("WHITESPACE")) {
                    statements.add(line);
                }
            }

            for (int i = 0; i < statements.size(); i++) {
                line = statements.get(i);
               // System.out.println(line);
                if (line.contains("TINT")) {
                    type = "int";
                } else if (line.contains("TFLOAT")) {
                    type = "float";
                }

                //if variable is detected
                if (line.contains("VAR")) {
                    val = "";
                    canPerform = true;
                    name = line.split(",")[1];

                    //check if var already has value
                    var = symbolTable.get(name);
                    if (var == null) {
                        System.out.println("var created!");
                        //if var doesnt have a value, store in symbol table
                        var = new Variable();
                        var.setType(type);
                        var.setName(name);
                    }

                    //iterate to move to next
                    i++;
                    if (i < statements.size()) {
                        line = statements.get(i);

                        //if '=' is detected, an assignment is happening
                        if (line.contains("EQUALS")) {
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
