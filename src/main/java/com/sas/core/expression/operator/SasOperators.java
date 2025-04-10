package com.sas.core.expression.operator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@OperatorLibrary
public class SasOperators {

    public static final Operator SYSGET
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "%SYSGET", ReturnTypes.STRING);

    public static final Operator PLUS
            = new Operator(Operator.OperatorType.BINARY_OPERATOR, "+", ReturnTypes.PLUS_OPERAND);

    public static final Operator MINUS
            = new Operator(Operator.OperatorType.BINARY_OPERATOR, "-", ReturnTypes.MINUS_OPERAND);

    public static final Operator INPUT
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "INPUT", ReturnTypes.DATE);

    public static final Operator PUT
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "PUT", ReturnTypes.STRING);

    public static final Operator SYMPUT
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "SYMPUT", ReturnTypes.STRING);

    public static final Operator SYSFUNC
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "%SYSFUNC", ReturnTypes.UNKNOWN);

    public static final Operator FILEESXIST
            = new Operator(Operator.OperatorType.SYSTEM_FUNCTION, "FILEEXIST", ReturnTypes.NUMBER);

    public static Operator findOperator(String name) {
        Field[] fields = SasOperators.class.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isPublic(field.getModifiers())
                    && Modifier.isStatic(field.getModifiers())
                    && field.getType() == Operator.class) {
                try {
                    Operator op = (Operator) field.get(null);
                    if (op.getOperatorName().equalsIgnoreCase(name)) {
                        return op;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(SasOperators.findOperator("%SYSGET"));
    }
}
