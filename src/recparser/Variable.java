/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recparser;

import java.util.ArrayList;

/**
 *
 * @author jet
 */
public class Variable {
    
    private String name;
    private ArrayList<Production> productions;
    
    public Variable(String name, ArrayList<Production> productions){
        this.name = name;
        this.productions = productions;
    }
    
    
    
    
}
