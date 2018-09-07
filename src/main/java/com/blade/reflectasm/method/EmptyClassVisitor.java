package com.blade.reflectasm.method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Empty class visitor.
 */
public abstract class EmptyClassVisitor extends ClassVisitor {

	protected EmptyClassVisitor() {
		super(Opcodes.ASM6);
	}

}