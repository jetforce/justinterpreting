package parser;

import java.util.ArrayList;

public class ProductionModel {

    private ArrayList<TerminalModel> symbols;

    public ProductionModel(ArrayList<TerminalModel> symbols) {
        this.symbols = symbols;
    }

    public ArrayList<TerminalModel> getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {

        String s = "";

        for (TerminalModel terminal : symbols) {
            s += terminal.getName() + " ";
        }

        return s;

    }

}
