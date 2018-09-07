package com.blade.reflectasm.method;

import org.objectweb.asm.Label;

import java.lang.reflect.Array;

/**
 * Extracts param information from a method.
 */
public final class ParamExtractor extends EmptyMethodVisitor {

	private final int paramCount;
	private final int ignoreCount;
	private MethodParameter[] methodParameters;
	private int currentParam;
	boolean debugInfoPresent;

	ParamExtractor(final int ignoreCount, final int paramCount) {
		this.ignoreCount = ignoreCount;
		this.paramCount = paramCount;
		this.methodParameters = new MethodParameter[paramCount];
		this.currentParam = 0;
		this.debugInfoPresent = paramCount == 0;		// for 0 params, no need for debug info
	}

	@Override
	public void visitLocalVariable(final String name, final String desc, String signature, final Label start, final Label end, final int index) {
		if ((index >= ignoreCount) && (index < (ignoreCount + paramCount))) {
			if (!name.equals("arg" + currentParam)) {
				debugInfoPresent = true;
			}
			if (signature == null) {
				signature = desc;
			}
			methodParameters[currentParam] = new MethodParameter(name, signature);
			currentParam++;
		}
	}

	@Override
	public void visitEnd() {
		if (methodParameters.length > currentParam) {
			methodParameters = subarray(methodParameters, 0, currentParam);
		}
	}

	MethodParameter[] getMethodParameters() {
		return methodParameters;
	}

	/**
	 * Returns subarray.
	 */
	public static <T> T[] subarray(T[] buffer, int offset, int length) {
		Class<T> componentType = (Class<T>) buffer.getClass().getComponentType();
		return subarray(buffer, offset, length, componentType);
	}

	/**
	 * Returns subarray.
	 */
	@SuppressWarnings({"unchecked"})
	public static <T> T[] subarray(T[] buffer, int offset, int length, Class<T> componentType) {
		T[] temp = (T[]) Array.newInstance(componentType, length);
		System.arraycopy(buffer, offset, temp, 0, length);
		return temp;
	}

}