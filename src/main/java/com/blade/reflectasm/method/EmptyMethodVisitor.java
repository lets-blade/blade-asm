package com.blade.reflectasm.method;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Empty method visitor.
 */
public abstract class EmptyMethodVisitor extends MethodVisitor {

	protected EmptyMethodVisitor() {
		super(Opcodes.ASM6);
	}

}