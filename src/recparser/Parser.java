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
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lexer.Analyzer;
import lexer.Token;
import parser.GrammarLoader;
import parser.GrammarModel;
import parser.NonTerminalModel;
import parser.ProductionModel;
import parser.TXTIO;
import parser.TerminalModel;

/**
 *
 * @author jet
 */
public class Parser {

    private GrammarModel grammar;
    private ArrayList<Token> tokens;
    private Map tokenMap;
    private Map variableMap;

    private ArrayList<TerminalModel> leafNodes;

    public Parser(GrammarModel grammar, ArrayList<Token> tokens, String StartingSymbol) {

        this.grammar = grammar;
        this.tokens = tokens;
        this.tokenMap = this.grammar.getTokens();
        this.variableMap = this.grammar.getVariables();
        this.leafNodes = new ArrayList<>();
        leafNodes.add(new TerminalModel(StartingSymbol));

    }

    public boolean isTerminal(String key) {
        return this.tokenMap.containsKey(key);
    }

    public boolean parse(int callNum, int matchTokenIndex, int leafNodeIndex) {
        System.out.println(callNum + " Call! " + matchTokenIndex + " " + leafNodeIndex);
        System.out.print("\t");
        for (TerminalModel tms : leafNodes) {
            System.out.print(tms.getName() + " ");
        }
        System.out.println("");
        int numInsert;
        NonTerminalModel variable;

        //System.out.println("Checking Stop Condition " + tokens.size());
        if (matchTokenIndex == tokens.size() && leafNodeIndex == matchTokenIndex) {
            return true;
        }

        if (leafNodeIndex >= this.leafNodes.size()) {
            return false;
        }

        TerminalModel currentAlphabet = this.leafNodes.get(leafNodeIndex);
        TerminalModel removedVariable;
        if (isTerminal(currentAlphabet.getName())) {
            //You found a terminal
            if (currentAlphabet.getName().equals(this.tokens.get(matchTokenIndex).getCategoryName())) {
                //the stuff matches move on to the next
                return parse(callNum + 1, matchTokenIndex + 1, leafNodeIndex + 1);
            } else {
                //Back Track.
                return false;
            }

        } else {
            //Non Terminal try all production rules
            //remove yourself

            int productionIndex = 0;
            int numInsertion = 0;
            removedVariable = this.leafNodes.remove(leafNodeIndex);
            variable = (NonTerminalModel) variableMap.get(removedVariable.getName());
            ArrayList<ProductionModel> pm = variable.getProductions();

            while (productionIndex < pm.size()) {
                numInsertion = pm.get(productionIndex).getSymbols().size();

                for (int i = 0; i < numInsertion; i++) {
                    this.leafNodes.add(i + leafNodeIndex, pm.get(productionIndex).getSymbols().get(i));
                }

                if (parse(callNum + 1, matchTokenIndex, leafNodeIndex)) {
                    return true;
                } else {
                    //bro backtrack remove everything you have added.

                    System.out.println("CallNum " + callNum + " Removing Chars " + numInsertion);
                    System.out.print("\t");
                    for (TerminalModel tms : leafNodes) {
                        System.out.print(tms.getName() + " ");
                    }
                    System.out.println("");

                    for (int j = 0; j < numInsertion; j++) {
                        this.leafNodes.remove(leafNodeIndex);
                    }
                    System.out.println("CallNum " + callNum + "After Removing Chars " + numInsertion);
                    System.out.print("\t");
                    for (TerminalModel tms : leafNodes) {
                        System.out.print(tms.getName() + " ");
                    }
                    System.out.println("");

                    //return false;
                }
               // this.leafNodes.add(leafNodeIndex, removedVariable);
                productionIndex++;
            }

        }
        this.leafNodes.add(leafNodeIndex, removedVariable);
        System.out.println(" this removed "+removedVariable.getName());
        return false;
    }

    public static void main(String args[]) throws FileNotFoundException {

        try {
            Scanner s = new Scanner("int*(int)");

            //Scanner s = new Scanner(new File("hello.txt"));
            Analyzer a = Analyzer.getInstance();
            a.loadCategories(new Scanner(new File("tokens3.txt")));
            a.loadRegex();

            ArrayList<Token> tokens = a.dump(s, "output.txt");

            // PARSER
            String GRAMMAR_FILENAME = "cgrammar2.txt";
            ArrayList<String> contents = TXTIO.read(GRAMMAR_FILENAME);

            GrammarModel grammar = new GrammarLoader().generateGrammar(contents);

            Map m = grammar.getVariables();
            Set<String> keys = m.keySet();

            NonTerminalModel model;
            ArrayList<ProductionModel> prods;
            ArrayList<TerminalModel> terminals;

            Parser parser = new Parser(grammar, tokens, "E");
            boolean jet = parser.parse(0, 0, 0);

            System.out.println("DID YOU GENERATE IT? " + jet);

            for (String str : keys) {
                model = (NonTerminalModel) m.get(str);
                System.out.println("Model " + model.getName());
                prods = model.getProductions();

                for (ProductionModel pm : prods) {
                    System.out.println("Next prod");
                    terminals = pm.getSymbols();

                    for (TerminalModel tm : terminals) {
                        System.out.println("    " + tm.getName());
                    }

                }

            }

        } catch (FileNotFoundException | IllegalStateException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //a.dump(s, "output.txt");
}