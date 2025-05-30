package com.lab;

public class Calc {

    private static String expression;
    private static int position;
    private static char currentChar;

    public static int run(String expression) {
        Calc.expression = expression.replace(" ", "");
        position = -1;
        return parseExpression();
    }

    private static void nextChar() {
        position++;
        if (position < expression.length()) {
            currentChar = expression.charAt(position);
        } else {
            currentChar = '\0';
        }
    }

    private static int parseExpression() {
        int x = parseTerm();
        while (true) {
            if (currentChar == '+') x += parseTerm();
            else if (currentChar == '-') x -= parseTerm();
            else break;
        }
        return x;
    }

    private static int parseTerm() {
        int x = parseFactor();
        while (true) {
            if (currentChar == '*') x *= parseTerm();
            else break;
        }
        return x;
    }

    private static int parseFactor() {
        int x = 0;
        nextChar();
        if (currentChar == '-') {
            x = -parseFactor();
        } else if (currentChar == '(') {
            x = parseExpression();
            nextChar();
        } else if (Character.isDigit(currentChar)) {
            while (Character.isDigit(currentChar)) {
                x = x * 10 + currentChar - '0';
                nextChar();
            }
        }
        return x;
    }
}
