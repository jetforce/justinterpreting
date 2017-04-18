/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 *
 * @author jet
 */
public class Analyzer {

    public ArrayList<Category> categories;
    public HashMap mapCategories;
    
    private String regex;
    private Pattern pattern;

    public static Analyzer analyzer = new Analyzer();

    public static Analyzer getInstance() {
        return Analyzer.analyzer;
    }

    public Analyzer() {
        categories = new ArrayList<>();
        mapCategories = new HashMap();
        //categories.add(new Category("EOF","\\z"));
    }

    public void loadCategories() {
        categories.add(new Category("NUMBER", "-?[0-9]+"));
        categories.add(new Category("BINARYOP", "[*|/|+|-]"));
        categories.add(new Category("WHITESPACE", "[ \t\f\r\n]+"));
        

        //Always Add Error
        categories.add(new Category("ERROR", ".+"));
    }

    public void loadCategories(Scanner input) {
        
        
        String s[];
        Category cat;      
        while (input.hasNextLine()) {
  
            String line = input.nextLine();
            s = line.split("\\t");
            cat = new Category(s[1],s[0]);
            categories.add(cat);
            mapCategories.put(s[1], cat);
            
        }
        input.close();
           
        categories.add(new Category("ERROR", ".+"));
        
        
    }

    public void loadRegex() {
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (Category c : categories) {
            tokenPatternsBuffer.append(String.format("|(%s)", c.getPattern()));
        }
        this.regex = new String(tokenPatternsBuffer.substring(1));
        this.pattern = Pattern.compile(this.regex);
        System.out.println("Regex is: " + regex);
    }

    public Token nextToken(Scanner inputStream) {

        //This method returns null if EOF is reached.
        //Else it will return Token Object
        //Token Object can be ERROR type look at loadCategory method    
        String foundMatch = inputStream.findWithinHorizon(pattern, 0);
        if (foundMatch == null) {
            return null;
        }

        Token token = null;
        for (int i = 0; i < this.categories.size(); i++) {
            if (inputStream.match().group(i + 1) != null) {

                token = new Token(foundMatch, this.categories.get(i), inputStream.match().start(),inputStream.match().end());
                break;
            }

        }

        return token;
    }

    public void dump(Scanner inputStream, String filename) {

        Token token = nextToken(inputStream);
        File f = new File(filename);
        PrintWriter out = null;
        try {

            out = new PrintWriter(f);
            while (token != null) {
                out.println(token.getCategoryName() + "," + token.getStatement());
                System.out.println(token.getCategoryName() + ">>" + token.getStatement());
                
                token = nextToken(inputStream);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.close();
    }

    public HashMap getCategories(){
        return this.mapCategories;
    }
     
    public static void main(String args[]) {

        try {
            Scanner s = new Scanner(new File("hello.txt"));
            Analyzer a = Analyzer.getInstance();
            a.loadCategories(new Scanner(new File("tokens2.txt")));
            a.loadRegex();
                
            a.dump(s, "output.txt");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Analyzer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
