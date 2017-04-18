package parser;

import java.util.ArrayList;

public class NonTerminalModel extends TerminalModel {

    private ArrayList<ProductionModel> productions;

    public NonTerminalModel(String name) {

        super(name);
        this.productions = new ArrayList<ProductionModel>();

    }

    public void addProduction(ProductionModel production) {

        productions.add(production);

    }

    public ArrayList<ProductionModel> getProductions() {
        return productions;
    }

}
