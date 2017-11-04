package ch.ahoegger.docbox.server.util;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.scout.rt.platform.context.RunContext;
import org.eclipse.scout.rt.platform.exception.IExceptionTranslator;
import org.eclipse.scout.rt.platform.exception.PlatformException;
import org.eclipse.scout.rt.platform.job.IDoneHandler;
import org.eclipse.scout.rt.platform.job.IExecutionSemaphore;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.JobInput;
import org.eclipse.scout.rt.platform.job.JobState;
import org.eclipse.scout.rt.platform.job.listener.IJobListener;
import org.eclipse.scout.rt.platform.job.listener.JobEvent;
import org.eclipse.scout.rt.platform.util.IRegistrationHandle;

/**
 * <h3>{@link FutureUtility}</h3>
 *
 * @author aho
 */
public class FutureUtility {

  public static <F_RES, RESULT> IFuture<RESULT> map(IFuture<F_RES> future, Function<? super F_RES, ? extends RESULT> mapper) {
    return new IFuture<RESULT>() {

      @Override
      public JobInput getJobInput() {
        return future.getJobInput();
      }

      @Override
      public IExecutionSemaphore getExecutionSemaphore() {
        return future.getExecutionSemaphore();
      }

      @Override
      public boolean isSingleExecution() {
        return future.isSingleExecution();
      }

      @Override
      public boolean cancel(boolean interruptIfRunning) {
        return future.cancel(interruptIfRunning);
      }

      @Override
      public boolean isCancelled() {
        return future.isCancelled();
      }

      @Override
      public boolean isDone() {
        return future.isDone();
      }

      @Override
      public boolean isFinished() {
        return future.isFinished();
      }

      @Override
      public JobState getState() {
        return future.getState();
      }

      @Override
      public void awaitDone() {
        future.awaitDone();
      }

      @Override
      public void awaitDone(long timeout, TimeUnit unit) {
        future.awaitDone(timeout, unit);
      }

      @Override
      public void awaitFinished(long timeout, TimeUnit unit) {
        future.awaitFinished(timeout, unit);
      }

      @Override
      public RESULT awaitDoneAndGet() {
        return Optional.of(future.awaitDoneAndGet()).map(res -> mapper.apply(res)).get();
      }

      @Override
      public <EXCEPTION extends Throwable> RESULT awaitDoneAndGet(Class<? extends IExceptionTranslator<EXCEPTION>> exceptionTranslator) throws EXCEPTION {
        return Optional.of(future.awaitDoneAndGet(exceptionTranslator)).map(res -> mapper.apply(res)).get();
      }

      @Override
      public RESULT awaitDoneAndGet(long timeout, TimeUnit unit) {
        return Optional.of(future.awaitDoneAndGet(timeout, unit)).map(res -> mapper.apply(res)).get();
      }

      @Override
      public <EXCEPTION extends Throwable> RESULT awaitDoneAndGet(long timeout, TimeUnit unit, Class<? extends IExceptionTranslator<EXCEPTION>> exceptionTranslator) throws EXCEPTION {
        return Optional.of(future.awaitDoneAndGet(timeout, unit, exceptionTranslator)).map(res -> mapper.apply(res)).get();
      }

      @Override
      public IFuture<RESULT> whenDone(IDoneHandler<RESULT> callback, RunContext runContext) {
        throw new PlatformException("Not implemented");
      }

      @Override
      public <FUNCTION_RESULT> IFuture<FUNCTION_RESULT> whenDoneSchedule(BiFunction<RESULT, Throwable, FUNCTION_RESULT> function, JobInput input) {
        throw new PlatformException("Not implemented");
      }

      @Override
      public IFuture<Void> whenDoneSchedule(BiConsumer<RESULT, Throwable> function, JobInput input) {
        throw new PlatformException("Not implemented");
      }

      @Override
      public IRegistrationHandle addListener(IJobListener listener) {
        return future.addListener(listener);
      }

      @Override
      public IRegistrationHandle addListener(Predicate<JobEvent> filter, IJobListener listener) {
        return future.addListener(filter, listener);
      }

      @Override
      public boolean addExecutionHint(String hint) {
        return future.addExecutionHint(hint);
      }

      @Override
      public boolean removeExecutionHint(String hint) {
        return future.removeExecutionHint(hint);
      }

      @Override
      public boolean containsExecutionHint(String hint) {
        return future.containsExecutionHint(hint);
      }

    };
  }
}
