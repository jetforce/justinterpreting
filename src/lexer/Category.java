/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

/**
 *
 * @author jet
 */
public class Category {
    
    private String name;
    private String pattern;
    
    public Category(String name, String pattern){
        this.name =  name;
        this.pattern = pattern;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getPattern(){
        return this.pattern;
    }
    
    
}
