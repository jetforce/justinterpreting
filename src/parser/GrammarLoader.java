package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GrammarLoader {
    
    private static final GrammarLoader grammarLoader = new GrammarLoader();
    
    private final String TOKEN_DIRECTIVE = "%token";
    private final String VAR_DIRECTIVE = "%var";
    private final String START_DIRECTIVE = "%start";

    public Map<String, TerminalModel> symbols;
    public Map<String, TerminalModel> tokens;
    public Map<String, NonTerminalModel> variables;

    public NonTerminalModel startingSymbol;

    private GrammarLoader() {

        symbols = new HashMap<>();
        tokens = new HashMap<>();
        variables = new HashMap<>();

        startingSymbol = null;

    }
    
    public static GrammarLoader getInstance() {
        return GrammarLoader.grammarLoader;
    }
    
    public GrammarModel generateGrammar(ArrayList<String> contents) {

        listTokens(contents.remove(0));
        listVariables(contents.remove(0));
        getStartingSymbol(contents.remove(0));
        generateProductions(contents);
        
        return new GrammarModel(symbols, tokens, variables, startingSymbol);
        
    }

    private void listTokens(String tokenString) {

        if (tokenString.split("\\s+")[0].equals(TOKEN_DIRECTIVE)) {
            String[] tokenArray = tokenString.split("\\s+");
            for (int i = 1; i < tokenArray.length; i++) {
                TerminalModel terminal = new TerminalModel(tokenArray[i]);
                symbols.put(tokenArray[i], terminal);
                tokens.put(tokenArray[i], terminal);
            }
        } else {
            System.out.println("GRAMMAR FILE ERROR: " + TOKEN_DIRECTIVE + " undefined");
        }

    }

    private void listVariables(String varString) {

        if (varString.split("\\s+")[0].equals(VAR_DIRECTIVE)) {
            String[] varArray = varString.split("\\s+");
            for (int i = 1; i < varArray.length; i++) {
                NonTerminalModel variable = new NonTerminalModel(varArray[i]);
                symbols.put(varArray[i], variable);
                variables.put(varArray[i], variable);
            }
        } else {
            System.out.println("GRAMMAR FILE ERROR: " + VAR_DIRECTIVE + " undefined");
        }

    }

    private void generateProductions(ArrayList<String> contents) {

        for (String content : contents) {

            String nonTerminal = content.split(":\\s*")[0];

            String[] productionArray = content.split(":\\s*")[1].split("\\s*\\|\\s*");

            for (String production : productionArray) {

                ArrayList<TerminalModel> pSymbols = new ArrayList<>();

                for (String symbol : production.split("\\s+")) {
                    if (symbols.get(symbol) == null) {
                        System.out.println("GRAMMAR FILE ERROR: Symbol " + symbol + " undefined");
                    } else {
                        pSymbols.add(symbols.get(symbol));
                    }
                }
                ((NonTerminalModel) symbols.get(nonTerminal)).addProduction(new ProductionModel(pSymbols));
            }

        }

    }

    private void getStartingSymbol(String startString) {

        if (startString.split("\\s+")[0].equals(START_DIRECTIVE)) {
            startingSymbol = variables.get(startString.split("\\s+")[1]);
        } else {
            System.out.println("GRAMMAR FILE ERROR: " + START_DIRECTIVE + " undefined");
        }

    }

}
