package packTest;

import packWork.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String inputFileName = readInput("Bine ai venit!", "Introdu numele fisierului de intrare:");
        String outputFileName = readInput("Introdu numele fisierului de iesire:");

       App.start(inputFileName, outputFileName);
    }

    public static String readInput(String... messages) {
        for (String message : messages) {
            System.out.println(message);
        }

        Scanner sc = new Scanner(System.in);

        return sc.next();
    }
}