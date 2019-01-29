package ch.ahoegger.docbox.server.test.util;

import java.util.function.Consumer;

/**
 * <h3>{@link MethodInvocationConsumer}</h3>
 *
 * @author aho
 */
public class MethodInvocationConsumer<T> implements Consumer<T> {

  private int m_invocationCount = 0;

  @Override
  public final void accept(T t) {
    increment();
    execAccept(t);
  }

  /**
   * @param t
   */
  protected void execAccept(T t) {
  }

  public void increment() {
    m_invocationCount++;
  }

  public int getInvocationCount() {
    return m_invocationCount;
  }
}
