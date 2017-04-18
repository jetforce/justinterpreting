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
public class Token {
    
    private String statement;
    private Category category;
    private int startIndex, endIndex;
    
    public Token(String statement, Category category, int start, int end){
        this.statement = statement;
        this.category = category;
        startIndex = start;
        endIndex = end;
    }
    
    public String getStatement(){
        return statement;
    }
    
    public String getCategoryName(){
        return this.category.getName();
    }
    
}
