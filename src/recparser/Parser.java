/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recparser;

import java.util.ArrayList;
import java.util.HashMap;
import lexer.Analyzer;
import lexer.Token;

/**
 *
 * @author jet
 */
public class Parser {
    

    public static Parser instance = new Parser();
    public static Parser getInstance(){
        return Parser.instance;
    }
    
    private HashMap grammar;
    
    public Parser(){
        grammar = new HashMap();
    }

    public void loadGrammar(){
        
        Analyzer a = Analyzer.getInstance();
        HashMap cats = a.getCategories();
        
        ArrayList<Production> temp = new ArrayList<>();
        ArrayList<Alphabet> alphs = new ArrayList<>();
                
        alphs.add(new Alphabet((Token)cats.get("T")));
        
        temp.add(new Production(alphs));
        
        
        
        
    }

    
    
}
