/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexer.Analyzer;
import lexer.Token;
import parser.GrammarLoader;
import parser.GrammarModel;
import parser.TXTIO;

/**
 *
 * @author amcan
 */
public class Main {
    
    public static void main(String args[]) {
        
        try {
            Scanner s = new Scanner(new File("hello.txt"));
            Analyzer a = Analyzer.getInstance();
            a.loadCategories(new Scanner(new File("tokens2.txt")));
            a.loadRegex();
                
            ArrayList<Token> tokens = a.dump(s, "output.txt");
            
            // PARSER
            String GRAMMAR_FILENAME = "cgrammar.txt";
            ArrayList<String> contents = TXTIO.read(GRAMMAR_FILENAME);
            
            GrammarModel grammar = GrammarLoader.getInstance().generateGrammar(contents);
            System.out.println(grammar.toString());
            
        } catch (FileNotFoundException | IllegalStateException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
