package com.hellokaton.blade.asm.method;

public class MethodParameter {

	private String name;
	private String signature;

	public MethodParameter(String name, String signature) {
		this.name = name;
		this.signature = signature;
	}

	/**
	 * Returns method parameter name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns method parameter signature.
	 * Generics information is available, too.
	 */
	public String getSignature() {
		return signature;
	}


}