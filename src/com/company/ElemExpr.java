package com.company;

public class ElemExpr {

    private String value;
    private TypeElem type;
    private int priority;
    private int arity;  // arity operation, f.e. unary, binary etc

    public ElemExpr(String value, TypeElem type, int priority, int arity) {
        this.value = value;
        this.type = type;
        this.priority = priority;
        this.arity = arity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TypeElem getType() {
        return type;
    }

    public void setType(TypeElem type) {
        this.type = type;
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

    enum TypeElem {
        OPERAND,
        OPERATION,
        OPEN_BRACKET,
        CLOSE_BRACKET
    }
}
