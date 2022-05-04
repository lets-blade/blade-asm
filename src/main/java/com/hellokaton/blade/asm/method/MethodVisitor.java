package com.hellokaton.blade.asm.method;

import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class MethodVisitor extends EmptyClassVisitor {

    private static final Map<String, String> primitives = new HashMap<>(8);

    private static final String TYPE_INT     = "int";
    private static final String TYPE_BOOLEAN = "boolean";
    private static final String TYPE_BYTE    = "byte";
    private static final String TYPE_CHAR    = "char";
    private static final String TYPE_SHORT   = "short";
    private static final String TYPE_FLOAT   = "float";
    private static final String TYPE_LONG    = "long";
    private static final String TYPE_DOUBLE  = "double";
    private static final String ARRAY        = "[]";

    static {
        primitives.put(TYPE_INT, "I");
        primitives.put(TYPE_BOOLEAN, "Z");
        primitives.put(TYPE_CHAR, "C");
        primitives.put(TYPE_BYTE, "B");
        primitives.put(TYPE_FLOAT, "F");
        primitives.put(TYPE_LONG, "J");
        primitives.put(TYPE_DOUBLE, "D");
        primitives.put(TYPE_SHORT, "S");
    }

    private final Class   declaringClass;
    private final String  methodName;
    private final Class[] parameterTypes;

    private ParamExtractor paramExtractor;

    public MethodVisitor(final Class declaringClass, final String methodName, final Class[] parameterTypes) {
        this.declaringClass = declaringClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.paramExtractor = null;
    }

    @Override
    public org.objectweb.asm.MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        if (paramExtractor != null) {
            return null;                // method already found, skip all further methods
        }
        if (!name.equals(methodName)) {
            return null;                // different method
        }

        Type[] argumentTypes = Type.getArgumentTypes(desc);
        int    dwordsCount   = 0;
        for (Type t : argumentTypes) {
            if (t.getClassName().equals(TYPE_LONG) || t.getClassName().equals(TYPE_DOUBLE)) {
                dwordsCount++;
            }
        }

        int paramCount = argumentTypes.length;
        if (paramCount != this.parameterTypes.length) {
            return null;                // different number of params
        }

        for (int i = 0; i < argumentTypes.length; i++) {
            if (!isEqualTypeName(argumentTypes[i], this.parameterTypes[i])) {
                return null;            // wrong param types
            }
        }

        this.paramExtractor = new ParamExtractor((Modifier.isStatic(access) ? 0 : 1), argumentTypes.length + dwordsCount);
        return paramExtractor;
    }

    /**
     * Returns <code>true</code> if type name equals param type.
     */
    boolean isEqualTypeName(final Type argumentType, final Class paramType) {
        String s = argumentType.getClassName();
        if (s.endsWith(ARRAY)) {        // arrays detected
            String prefix         = s.substring(0, s.length() - 2);
            String bytecodeSymbol = primitives.get(prefix);
            if (bytecodeSymbol != null) {
                s = '[' + bytecodeSymbol;
            } else {
                s = "[L" + prefix + ';';
            }
        }
        return s.equals(paramType.getName());
    }


    /**
     * Returns method parameters once when method is parsed.
     * If method has no parameters, an empty array is returned.
     */
    public String[] getParamNames() {
        if (paramExtractor == null) {
            return null;
        }
        if (!paramExtractor.debugInfoPresent) {
            throw new RuntimeException("Parameter names not available for method: "
                    + declaringClass.getName() + '#' + methodName);
        }
        MethodParameter[] methodParameters = paramExtractor.getMethodParameters();
        String[]          parameters       = new String[methodParameters.length];
        for (int i = 0; i < methodParameters.length; i++) {
            parameters[i] = methodParameters[i].getName();
        }
        return parameters;
    }

}