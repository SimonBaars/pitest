/*
 * Copyright 2021 Barry Wang, Harry Lee
 */

package org.pitest.mutationtest.engine.gregor.mutators.experimental;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.bytecode.ASMVersion;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;


public enum PatternMatchMutator implements MethodMutatorFactory {
  PATTERN_MATCH_MUTATOR;

  @Override
  public MethodVisitor create(final MutationContext context,
                              final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
    return new PatternMatchMethodVisitor(this, context, methodVisitor);
  }

  @Override
  public String getGloballyUniqueId() {
    return this.getClass().getName();
  }

  @Override
  public String getName() {
    return name();
  }
}


class PatternMatchMethodVisitor extends MethodVisitor {

  private final MethodMutatorFactory factory;
  private final MutationContext context;

  private static int seenInvokeVirtual = 0;
  private static int seenLdc = 0;
  private static int seenEquals = 0;


  PatternMatchMethodVisitor(final MethodMutatorFactory factory,
                            final MutationContext context, final MethodVisitor delegateMethodVisitor) {
    super(ASMVersion.ASM_VERSION, delegateMethodVisitor);
    this.factory = factory;
    this.context = context;
  }

  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    System.out.println("=== Entered visitMethodInsn ===");
    if (opcode == Opcodes.INVOKEVIRTUAL) {
      System.out.println("Opcode: INVOKEVIRTUAL");
      System.out.println("Name: " + name);
      System.out.println("Owner: " + owner);
    }

    if (opcode == Opcodes.INVOKEVIRTUAL && owner.contains("Flux") && name.equals("filter")) {
      //System.out.println("------------START------------");
      System.out.println("INVOKEVIRTUAL Flux Method filter CALLED");

    } else if (opcode == Opcodes.INVOKEVIRTUAL && owner.contains("App") && name.equals("isEqual")) {
      //System.out.println("------------START------------");
      System.out.println("INVOKEVIRTUAL App isEqual Method CALLED");
      if (seenInvokeVirtual == 1 && seenLdc == 1) {
        seenEquals = 1;
      } else {
        seenInvokeVirtual = 0;
        seenLdc = 0;
        seenEquals = 0;
      }
    } else {
      //System.out.println("------------START------------");
      //System.out.println("STATE RESET AT: INVOKE VIRTUAL");
      seenInvokeVirtual = 0;
      seenLdc = 0;
      seenEquals = 0;
    }
    //logInternalState();
    //System.out.println("-------------END-------------");
    super.visitMethodInsn(opcode, owner, name, desc, itf);
  }

  @Override
  public void visitLdcInsn(Object value) {
    System.out.println("Entered visitLdcInsn");
    if (seenInvokeVirtual == 1) {
      //System.out.println("------------START------------");
      //System.out.println("SEEN LDC AFTER INVOKE VIRTUAL");
      seenLdc = 1;
    } else {
      //System.out.println("------------START------------");
      //System.out.println("STATE RESET AT: LDC");
      seenInvokeVirtual = 0;
      seenLdc = 0;
      seenEquals = 0;
    }
    logInternalState();
    //System.out.println("-------------END-------------");
    super.visitLdcInsn(value);
  }

  @Override
  public void visitJumpInsn(final int opcode, final Label label) {
    System.out.println("Entered visitJumpInsn");
    // Hier wordt mutation gedaan?
    if (canMutate(opcode)
        && seenInvokeVirtual == 1
    ) {
      final MutationIdentifier newId = this.context.registerMutation(this.factory, "CSTP Pattern-Match Mutator");

      // should mutate?
      //System.out.println("!!!!!!!!!!!!START!!!!!!!!!!!!");
      //System.out.println("MUTATED AT: JUMP INSN");
      super.visitInsn(Opcodes.POP);
    } else {
      //System.out.println("!!!!!!!!!!!!START!!!!!!!!!!!!");
      //System.out.println("CAN MUTATE YIELDS FALSE");
      this.mv.visitJumpInsn(opcode, label);
    }

    seenInvokeVirtual = 0;
    seenLdc = 0;
    seenEquals = 0;
    //System.out.println("!!!!!!!!!!!!END!!!!!!!!!!!!");
  }

  private boolean canMutate(final int opcode) {
    return opcode == Opcodes.INVOKESPECIAL;
  }

  private void logInternalState() {
    System.out.println("SEEN_INVOKE_VIRTUAL = " + seenInvokeVirtual);
    System.out.println("SEEN_LDC = " + seenLdc);
    System.out.println("SEEN_EQUALS = " + seenEquals);
  }
}
