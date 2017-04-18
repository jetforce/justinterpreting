package parser;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter filepath of the Grammar file: ");
        String grammarPath = sc.nextLine();

        ArrayList<String> contents = TXTIO.read(grammarPath);

        /*for(String content : contents) {
            System.out.println(content);
        }*/
        System.out.println("---END PRINT---");

        new GrammarLoader();

        sc.close();

    }

}
