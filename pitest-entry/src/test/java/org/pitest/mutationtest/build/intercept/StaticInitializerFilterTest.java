package org.pitest.mutationtest.build.intercept;

import static org.assertj.core.api.Assertions.assertThat;
import static org.pitest.mutationtest.engine.MutationDetailsMother.aMutationDetail;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.PoisonStatus;

@RunWith(MockitoJUnitRunner.class)
public class StaticInitializerFilterTest {
  
  StaticInitializerFilter testee  = new StaticInitializerFilter();  
  
  @Test
  public void shouldRemoveMutationsInStaticInitCode() {
    Collection<MutationDetails> marked = aMutationDetail()
        .withPoison(PoisonStatus.IS_STATIC_INITIALIZER_CODE)
        .build(2);
    
    assertThat(testee.intercept(marked, null)).isEmpty();
  }

  @Test
  public void shouldNotFilterNotStaticMutants() {
    Collection<MutationDetails> unmarked = aMutationDetail()
        .build(2);    
    assertThat(testee.intercept(unmarked, null)).containsAll(unmarked);
  }
  
}
