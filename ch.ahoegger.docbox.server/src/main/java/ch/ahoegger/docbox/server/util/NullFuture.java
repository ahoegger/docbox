package ch.ahoegger.docbox.server.util;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.eclipse.scout.rt.platform.context.RunContext;
import org.eclipse.scout.rt.platform.exception.IExceptionTranslator;
import org.eclipse.scout.rt.platform.job.IDoneHandler;
import org.eclipse.scout.rt.platform.job.IExecutionSemaphore;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.JobInput;
import org.eclipse.scout.rt.platform.job.JobState;
import org.eclipse.scout.rt.platform.job.listener.IJobListener;
import org.eclipse.scout.rt.platform.job.listener.JobEvent;
import org.eclipse.scout.rt.platform.util.IRegistrationHandle;

/**
 * <h3>{@link NullFuture}</h3>
 *
 * @author aho
 */
public class NullFuture<RESULT> implements IFuture<RESULT> {

  private final RESULT m_result;

  public NullFuture() {
    this(null);
  }

  public NullFuture(RESULT result) {
    m_result = result;
  }

  @Override
  public JobInput getJobInput() {
    return null;
  }

  @Override
  public IExecutionSemaphore getExecutionSemaphore() {
    return null;
  }

  @Override
  public boolean isSingleExecution() {
    return false;
  }

  @Override
  public boolean cancel(boolean interruptIfRunning) {
    return false;
  }

  @Override
  public boolean isCancelled() {
    return false;
  }

  @Override
  public boolean isDone() {
    return true;
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public JobState getState() {
    return null;
  }

  @Override
  public void awaitDone() {
  }

  @Override
  public void awaitDone(long timeout, TimeUnit unit) {
  }

  @Override
  public void awaitFinished(long timeout, TimeUnit unit) {
  }

  @Override
  public RESULT awaitDoneAndGet() {
    return m_result;
  }

  @Override
  public <EXCEPTION extends Throwable> RESULT awaitDoneAndGet(Class<? extends IExceptionTranslator<EXCEPTION>> exceptionTranslator) throws EXCEPTION {
    return m_result;
  }

  @Override
  public RESULT awaitDoneAndGet(long timeout, TimeUnit unit) {
    return m_result;
  }

  @Override
  public <EXCEPTION extends Throwable> RESULT awaitDoneAndGet(long timeout, TimeUnit unit, Class<? extends IExceptionTranslator<EXCEPTION>> exceptionTranslator) throws EXCEPTION {
    return m_result;
  }

  @Override
  public IFuture<RESULT> whenDone(IDoneHandler<RESULT> callback, RunContext runContext) {

    return null;
  }

  @Override
  public <FUNCTION_RESULT> IFuture<FUNCTION_RESULT> whenDoneSchedule(BiFunction<RESULT, Throwable, FUNCTION_RESULT> function, JobInput input) {
    return null;
  }

  @Override
  public IFuture<Void> whenDoneSchedule(BiConsumer<RESULT, Throwable> function, JobInput input) {
    return null;
  }

  @Override
  public IRegistrationHandle addListener(IJobListener listener) {
    return null;
  }

  @Override
  public IRegistrationHandle addListener(Predicate<JobEvent> filter, IJobListener listener) {
    return null;
  }

  @Override
  public boolean addExecutionHint(String hint) {
    return false;
  }

  @Override
  public boolean removeExecutionHint(String hint) {
    return false;
  }

  @Override
  public boolean containsExecutionHint(String hint) {
    return false;
  }

}
