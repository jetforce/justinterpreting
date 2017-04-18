/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recparser;

import lexer.Token;

/**
 *
 * @author jet
 */
public class Alphabet {
    
    private boolean isTerminal;
    private Token token;
    private Variable variable;
    
    
    public Alphabet(Token token){
        isTerminal = true;
        this.token = token;
    }
    
    
    public Alphabet(Variable variable){
        isTerminal = false;
        this.variable = variable;
    }
    
    
    
    
}
