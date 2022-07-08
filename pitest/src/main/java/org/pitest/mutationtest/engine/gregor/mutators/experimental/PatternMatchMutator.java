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
  public static final String REACTOR_MONO_CLASS = "reactor/core/publisher/Mono";

  public static final List FLUX_METHODS = List.of(
                                 "cache",
                                          "cancelOn",
                                          "checkpoint",
                                          "concatWith",
                                          "concatWithValues",
                                          "contextWrite",
                                          "defaultIfEmpty",
                                          "delayElements",
                                          "delaySequence",
                                          "delaySubscription",
                                          "delayUntil",
                                          "distinct",
                                          "distinctUntilChanged",
                                          "doAfterTerminate",
                                          "doFinally",
                                          "doFirst",
                                          "doOnCancel",
                                          "doOnComplete",
                                          "doOnEach",
                                          "doOnError",
                                          "doOnNext",
                                          "doOnRequest",
                                          "doOnSubscribe",
                                          "doOnTerminate",
                                          "expand",
                                          "expandDeep",
                                          "filter",
                                          "filterWhen",
                                          "hide",
                                          "limitRate",
                                          "limitRequest",
                                          "log",
                                          "mergeComparingWith",
                                          "mergeOrderedWith",
                                          "mergeWith",
                                          "metrics",
                                          "name",
                                          "onBackpressureBuffer",
                                          "onBackpressureDrop",
                                          "onBackpressureError",
                                          "onBackpressureLatest",
                                          "onErrorContinue",
                                          "onErrorMap",
                                          "onErrorResume",
                                          "onErrorReturn",
                                          "onErrorStop",
                                          "onTerminateDetach",
                                          "or",
                                          "publishOn",
                                          "repeat",
                                          "repeatWhen",
                                          "retry",
                                          "retryWhen",
                                          "sample",
                                          "sampleFirst",
                                          "scan",
                                          "share",
                                          "skip",
                                          "skipLast",
                                          "skipUntil",
                                          "skipUntilOther",
                                          "skipWhile",
                                          "sort",
                                          "startWith",
                                          "subscribeOn",
                                          "subscriberContext",
                                          "switchIfEmpty",
                                          "tag",
                                          "take",
                                          "takeLast",
                                          "takeUntil",
                                          "takeUntilOther",
                                          "takeWhile",
                                          "timeout"
  );

  public static final List MONO_METHODS = List.of();

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
    return (name, desc, owner) -> FLUX_METHODS.contains(name)
        && desc.endsWith(REACTOR_FLUX_CLASS + ";")
        && owner.equals(REACTOR_FLUX_CLASS)
        || MONO_METHODS.contains(name)
        && desc.endsWith(REACTOR_MONO_CLASS + ";")
        && owner.equals(REACTOR_MONO_CLASS);
  }

}

