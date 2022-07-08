package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import java.util.List;
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
    return (name, desc, owner) -> {
      if (desc.contains("Lreactor/core/publisher/Flux;") && owner.equals("reactor/core/publisher/Flux")) {
        System.out.println("Name " + name + " Desc " + desc + " Owner " + owner);
        return List.of("filter", "skip", "repeat", "delayElements").contains(name);
      }
      else if (desc.contains("Lreactor/core/publisher/Mono;") && owner.equals("reactor/core/publisher/Mono")) {
        return false;
      }
      else {
        return false;
      }
    };
  }

}

