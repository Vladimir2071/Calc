package com.company;

import com.sun.istack.internal.NotNull;
import com.sun.jmx.remote.internal.ArrayQueue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class UtilExpr {

    public static double evalBinaryOperation(List<ElemExpr> list, ElemExpr elem) {

        double x1, x2, result = 0.0;

        x1 = Double.parseDouble(list.get(0).getValue());
        x2 = Double.parseDouble(list.get(1).getValue());

        switch (elem.getValue()) {
            case "+": {
                result = x1 + x2;
                break;
            }
            case "-": {
                result = x1 - x2;
                break;
            }
            case "*": {
                result = x1 * x2;
                break;
            }
            case "/": {
                result = x1 / x2;
                break;
            }
        }

        return result;
    }
    /* ==============================================================
    вычисление выражения
    ================================================================ */
    public static double evalExpression(ArrayList<ElemExpr> list) {

        List<ElemExpr> operands = new ArrayList<ElemExpr>();

        double tempResult = 0;
        int index = 0;
        int arity = 0;

        while (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getType() == ElemExpr.TypeElem.OPERATION) {
                    arity = list.get(i).getArity();
                    index = i;
                    break;
                }
            }

            operands = list.subList(index - arity, index);
            switch (arity) {
                case 1:
                    break;
                case 2: {
                    tempResult =  evalBinaryOperation(operands, list.get(index));
                    break;
                }
            }

            list.get(index - arity).setValue(Double.toString(tempResult));

            for (int i = index;  i > index - arity; i--) {
                list.remove(i);
            }
        }

        return Double.parseDouble(list.get(0).getValue());
    }

    /* ==============================================================
    конвертация выражения в польскую обратную запись
    ================================================================ */
    public static ArrayList<ElemExpr> getPolandListElements(ArrayList<ElemExpr> list) {
        ElemExpr elem;
        ArrayDeque<ElemExpr> stack = new ArrayDeque<ElemExpr>();
        ArrayList<ElemExpr> listPoland = new ArrayList<ElemExpr>();

        for (int i = 0; i < list.size(); i++) {
            elem = list.get(i);

            switch (elem.getType()) {
                case OPERAND: {
                    //System.out.println("Operand: " + elem.getValue());
                    listPoland.add(elem);
                    break;
                }
                case OPERATION: {
                    //System.out.println("Operation: " + elem.getValue());
                    while (!stack.isEmpty() && elem.getPriority() <= stack.getLast().getPriority()) {
                        listPoland.add(stack.removeLast());
                    }
                    stack.addLast(elem);
                    break;
                }
                case OPEN_BRACKET:{
                    //System.out.println("Bracket: " + elem.getValue());
                    //stack.addLast(elem);
                    break;
                }
                case CLOSE_BRACKET:{
                    //System.out.println("Bracket: " + elem.getValue());
                    break;
                }
                default: {
                    System.out.println("Error");
                }
            }
        }

        while (!stack.isEmpty()) {
            listPoland.add(stack.removeLast());
        }

        return  listPoland;

    }
    /* ==============================================================
        парсинг текстовой строки выражения и конвертация ее в массив элементов выражения
    ================================================================ */
    public static ArrayList<ElemExpr> getListElements(String expr) throws Exception {

        ArrayList<ElemExpr> list = new  ArrayList<ElemExpr>();
        ArrayDeque<Character> stackBrackets = new ArrayDeque<Character>();

        String readOperandNumber = new String();
        String readOperation = new String();
        char ch;

        for (int i = 0; i < expr.length(); i++) {
            ch = expr.charAt(i);

            if (ch == ' ') {
                continue;
            } else if (Character.isDigit(ch) || ch == '.') {
                readOperandNumber += ch;
                if (!readOperation.isEmpty()) {
                    list.add(addElemOfExpr(2, readOperation));
                    readOperation = "";
                }
            } else if (isSymbolOperation(ch)) {
                readOperation += ch;
                if (!readOperandNumber.isEmpty()) {
                    if (readOperandNumber.indexOf(".") == readOperandNumber.lastIndexOf(".")) {
                        list.add(addElemOfExpr(1, readOperandNumber));
                        readOperandNumber = "";
                    } else {
                        throw new Exception("There is error number(too much radix point)! Number: " + readOperandNumber);
                    }
                }
            } else if (ch == '(') {
                if (!readOperation.isEmpty()) {
                    list.add(addElemOfExpr(2, readOperation));
                    readOperation = "";
                } else if (!readOperandNumber.isEmpty()) {
                    throw new Exception("There is open bracket is incorrect! Position: " + (i + 1));
                }
                list.add(addElemOfExpr(3, "("));
                stackBrackets.addLast(ch);
            } else if (ch == ')') {
                list.add(addElemOfExpr(4, ")"));
                if (!stackBrackets.isEmpty()) {
                    if (stackBrackets.getLast() == '(') {
                        // remove pair of brackets
                        stackBrackets.removeLast();
                    } else {
                        // add close bracket
                        stackBrackets.addLast(ch);
                    }
                } else {
                    throw new Exception("There are odd close bracket in position " + (++i));
                }
            } else {
                // other symbol, may be something function
                // It'll be developed in the future

            }
        }
        // check stack brackets
        if (!stackBrackets.isEmpty()) {
            // there are odd brackets
            throw new Exception("There are odd brackets");
        }

        // after end of cycle check buffers readOperand and readOperation
        if (readOperandNumber != "") {
            list.add(addElemOfExpr(1, readOperandNumber));
        } else if (readOperation != "") {
            list.add(addElemOfExpr(2, readOperation));
        }

        return list;

    }

    /* ==============================================================
    проверка символа на вхождение в знак операции
    (операция может состоянть из нескольких знаков, например ++ или какая-то новая, придуманная
    ================================================================= */
    public static boolean isSymbolOperation(char ch) {
        String oper = "+-*/";
        if (oper.indexOf(ch) == -1) {
            return false;
        } else {
            return true;
        }
    }

    /* ==============================================================
        Создание элемента выражения, может быть число - операнд, знак операции - операция или скобки

    type = 1 - add Operand
    type = 2 - add Operation
    type = 3 - add '('
    type = 4 - add ')'
    ================================================================= */
    public static ElemExpr addElemOfExpr(int type, String value) {
        int priority = 0;
        int arity = 0;
        ElemExpr.TypeElem typeElem = null;

        if (type == 1) {
            typeElem = ElemExpr.TypeElem.OPERAND;
        } else if (type == 2) {
            typeElem = ElemExpr.TypeElem.OPERATION;
            if (value.equals("+") || value.equals("-")) {
                priority = 1;
            } else {
                priority = 2;
            };
            arity = 2;
        } else if (type == 3) {
            typeElem = ElemExpr.TypeElem.OPEN_BRACKET;
        } else {
            typeElem = ElemExpr.TypeElem.CLOSE_BRACKET;
        };

        ElemExpr elem;
        elem = new ElemExpr(value, typeElem, priority, arity);

        return elem;
    }
}
