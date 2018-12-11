package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String expr = "(55+8)*12-120/4+29*2.5";
        ArrayList<ElemExpr> list = new ArrayList<ElemExpr>();

        try {
            list = UtilExpr.getListElements(expr);
        } catch (Exception ex) {
            System.out.println("Error expression\n" + ex.getMessage());
            System.exit(1);
        }

        ArrayList<ElemExpr> listPoland = UtilExpr.getPolandListElements(list);

        double result = UtilExpr.evalExpression(listPoland);
        System.out.println("Result: " + expr + " = " + result);
    }
}
