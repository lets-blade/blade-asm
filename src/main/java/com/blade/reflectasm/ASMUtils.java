package com.blade.reflectasm;

import com.blade.reflectasm.method.MethodVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ASMUtils
 *
 * @author biezhi
 * @date 2018/9/7
 */
public final class ASMUtils {

    /**
     * Cached method names
     */
    private static final Map<Method, String[]> METHOD_NAMES_POOL = new ConcurrentHashMap<>(64);

    public static String[] findMethodParmeterNames(Method method) {
        if (METHOD_NAMES_POOL.containsKey(method)) return METHOD_NAMES_POOL.get(method);

        final String n = method.getDeclaringClass().getName();
        ClassReader  cr;
        try {
            cr = new ClassReader(n);
        } catch (IOException e) {
            return null;
        }
        MethodVisitor methodVisitor = new MethodVisitor(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
        cr.accept(methodVisitor, 0);

        String[] paramNames = methodVisitor.getParamNames();
        METHOD_NAMES_POOL.put(method, paramNames);
        return paramNames;
    }

}
