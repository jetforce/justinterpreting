/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.Map;

/**
 *
 * @author amcan
 */
public class GrammarModel {

    private final Map<String, TerminalModel> symbols;
    private final Map<String, TerminalModel> tokens;
    private final Map<String, NonTerminalModel> variables;

    private final NonTerminalModel startingSymbol;
    
    public GrammarModel(Map<String, TerminalModel> symbols,
            Map<String, TerminalModel> tokens,
            Map<String, NonTerminalModel> variables,
            NonTerminalModel startingSymbol) {
        
        this.symbols = symbols;
        this.tokens = tokens;
        this.variables = variables;

        this.startingSymbol = startingSymbol;
        
    }

    /**
     * @return the symbols
     */
    public Map<String, TerminalModel> getSymbols() {
        return symbols;
    }

    /**
     * @return the tokens
     */
    public Map<String, TerminalModel> getTokens() {
        return tokens;
    }

    /**
     * @return the variables
     */
    public Map<String, NonTerminalModel> getVariables() {
        return variables;
    }

    /**
     * @return the startingSymbol
     */
    public NonTerminalModel getStartingSymbol() {
        return startingSymbol;
    }
    
}
