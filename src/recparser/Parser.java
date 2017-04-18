/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import lexer.Analyzer;
import lexer.Token;
import parser.ParserController;
import parser.TXTIO;

/**
 *
 * @author jet
 */
public class Parser {


    
    public static void main(String args[]) throws FileNotFoundException {

        Scanner s = new Scanner("(int)");
        Analyzer a = Analyzer.getInstance();
        a.loadCategories(new Scanner(new File("tokens3.txt")));
        a.loadRegex();
        
        ArrayList<String> contents = TXTIO.read("cgrammar2.txt");

        ParserController  pc = new ParserController(contents);
        
        
        
        //a.dump(s, "output.txt");

    }

}
