/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import interpreter.Interpreter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexer.Analyzer;
import lexer.Category;
import lexer.Token;
import parser.GrammarLoader;
import parser.GrammarModel;
import parser.TXTIO;
import recparser.Parser;

/**
 *
 * @author amcan
 */
public class Main {

    public static void main(String args[]) {

        String INPUT_FILEPATH = "hello.c";
        File INPUT_FILE = new File(INPUT_FILEPATH);

        try {
            Scanner input = new Scanner(INPUT_FILE);

            Analyzer a = Analyzer.getInstance();
            a.loadCategories(new Scanner(new File("tokens6.txt")));
            a.loadRegex();

            System.out.println("GENERATING LEXEMES...");
            ArrayList<Token> tokens = a.dump(input, INPUT_FILE.getName().split(".c")[0] + ".lex");

            // PARSER
            String GRAMMAR_FILENAME = "cgrammar6.txt";
            ArrayList<String> contents = TXTIO.read(GRAMMAR_FILENAME);
            GrammarModel grammar = GrammarLoader.getInstance().generateGrammar(contents);

            System.out.println("PARSING...");
            Parser parser = new Parser(grammar, tokens, grammar.getStartingSymbol().getName());
            if (parser.parse(0, 0, 0)) {
                System.out.println("Well-formed source code.");
                System.out.println("\n=============\n CONSOLE \n =================\n");

                // INTERPRETER
                String LEX_FILENAME = INPUT_FILE.getName().split(".c")[0] + ".lex";
                Interpreter i = new Interpreter(LEX_FILENAME);
                i.interpret();
            } else {
                System.out.println("\n=============\n CONSOLE \n =================\n");
                
                Token error = tokens.get(parser.getMaxMatchTokenIndex());
                System.out.println("Line [" + error.getLineNumber() + "]" + " ERROR AFTER " + error.getStatement());
                //System.out.println(parser.getExpectedSymbol().getName());
                Category cat = (Category)a.getCategories().get(parser.getExpectedSymbol().getName());
                if(cat != null)
                    System.out.println(cat.getString() + " expected");
                else
                    System.out.println("Error in Line [" + (error.getLineNumber() + 1) + "]" );
                
            }

        } catch (FileNotFoundException | IllegalStateException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
