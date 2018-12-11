package com.company;

public class OperationExpr {
    private String value;   // signature of operation, f.e. "+' or '/' etc
    private int priority;
    private int arity;      // arity operation, f.e. unary, binary etc


    public OperationExpr(String value, int priority, int arity) {
        this.value = value;
        this.priority = priority;
        this.arity = arity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getArity() {
        return arity;
    }

    public void setArity(int arity) {
        this.arity = arity;
    }
}
