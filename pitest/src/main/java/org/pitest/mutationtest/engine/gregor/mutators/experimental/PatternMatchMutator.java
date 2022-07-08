package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import java.util.List;
import org.objectweb.asm.MethodVisitor;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;
import org.pitest.mutationtest.engine.gregor.mutators.MethodCallMethodVisitor;

public enum PatternMatchMutator implements MethodMutatorFactory {

  PATTERN_MATCH_MUTATOR;

  public static final String REACTOR_FLUX_CLASS = "reactor/core/publisher/Flux";

  @Override
  public MethodVisitor create(final MutationContext context,
                              final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
    return new MethodCallMethodVisitor(methodInfo, context, methodVisitor, this, reactiveMethods());
  }

  @Override
  public String getGloballyUniqueId() {
    return this.getClass().getName();
  }

  @Override
  public String getName() {
    return name();
  }

  private static TriFunction<String, String, String, Boolean> reactiveMethods() {
    return (name, desc, owner) -> List.of("filter", "skip", "repeat", "delayElements").contains(name)
        && desc.endsWith(REACTOR_FLUX_CLASS + ";")
        && owner.equals(REACTOR_FLUX_CLASS);
  }

}

