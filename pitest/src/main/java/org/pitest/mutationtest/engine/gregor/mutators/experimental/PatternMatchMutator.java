///*
// * Copyright 2021 Barry Wang, Harry Lee
// */
//
package org.pitest.mutationtest.engine.gregor.mutators.experimental;
//
//
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.pitest.bytecode.ASMVersion;
//import org.pitest.mutationtest.engine.MutationIdentifier;
//import org.pitest.mutationtest.engine.gregor.MethodInfo;
//import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
//import org.pitest.mutationtest.engine.gregor.MutationContext;
//
//
//public enum PatternMatchMutator implements MethodMutatorFactory {
//  PATTERN_MATCH_MUTATOR;
//
//  @Override
//  public MethodVisitor create(final MutationContext context,
//                              final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
//    return new PatternMatchMethodVisitor(this, context, methodVisitor);
//  }
//
//  @Override
//  public String getGloballyUniqueId() {
//    return this.getClass().getName();
//  }
//
//  @Override
//  public String getName() {
//    return name();
//  }
//}
//
//
//class PatternMatchMethodVisitor extends MethodVisitor {
//
//  private final MethodMutatorFactory factory;
//  private final MutationContext      context;
//
//
//  PatternMatchMethodVisitor(final MethodMutatorFactory factory,
//                            final MutationContext context, final MethodVisitor delegateMethodVisitor) {
//    super(ASMVersion.ASM_VERSION, delegateMethodVisitor);
//    this.factory = factory;
//    this.context = context;
//  }
//
//  @Override
//  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//    if(name.equals("filter")) {
//      context.shouldMutate(context.registerMutation(factory, "Remove filter"));
////      super.visitInsn(Opcodes.POP);
//    } else {
//      super.visitMethodInsn(opcode, owner, name, desc, itf);
//    }
//    System.out.println("METHODDDD"+opcode+", "+owner+", "+name+", "+desc+", "+itf);
//  }
//}

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.objectweb.asm.MethodVisitor;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.mutators.MethodCallMethodVisitor;

public enum PatternMatchMutator implements MethodMutatorFactory {

  PATTERN_MATCH_MUTATOR;

  @Override
  public MethodVisitor create(final MutationContext context,
                              final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
    return new MethodCallMethodVisitor(methodInfo, context, methodVisitor,
        this, voidMethods());
  }

  @Override
  public String getGloballyUniqueId() {
    return this.getClass().getName();
  }

  @Override
  public String getName() {
    return name();
  }

  private static TriFunction<String, String, String, Boolean> voidMethods() {
    return (name, desc, owner) -> {
      System.out.println("===== VISIT " + name + ", " + desc + " =====");
      if (name.equals("cache") && desc.equals("()Lreactor/core/publisher/Flux;")) {
        return true;
      }
      else if (name.equals("checkpoint") && desc.equals("()Lreactor/core/publisher/Flux;")) {
        return true;
      }
      else if (name.equals("filter")) {
        return true;
      }
      else {
        return false;
      }
    };
  }

}

