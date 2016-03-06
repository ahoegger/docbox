package ch.ahoegger.docbox.ui.html;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.job.IFuture;
import org.eclipse.scout.rt.platform.job.Jobs;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.commons.authentication.ConfigFileCredentialVerifier;
import org.eclipse.scout.rt.server.commons.authentication.ICredentialVerifier;
import org.eclipse.scout.rt.shared.ui.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.administration.user.IUserService;

/**
 * <h3>{@link UserCredentialsVerifier}</h3>
 *
 * @author aho
 */
public class UserCredentialsVerifier implements ICredentialVerifier {

  private static final Logger LOG = LoggerFactory.getLogger(ConfigFileCredentialVerifier.class);

  public UserCredentialsVerifier() {
  }

  @Override
  public int verify(final String username, final char[] passwordPlainText) {
    IFuture<Integer> future = Jobs.schedule(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        if (StringUtility.isNullOrEmpty(username) || passwordPlainText == null || passwordPlainText.length == 0) {
          return AUTH_CREDENTIALS_REQUIRED;
        }

        IUserService userService = BEANS.get(IUserService.class);
        if (userService.authenticate(username, passwordPlainText)) {
          return AUTH_OK;
        }
        return AUTH_FORBIDDEN;
      }
    }, Jobs.newInput().withRunContext(ClientRunContexts.copyCurrent().withUserAgent(UserAgent.createDefault())));
    // Wait until done without consuming the result

    Integer result = future.awaitDoneAndGet(3, TimeUnit.SECONDS);
    if (result == null) {
      return AUTH_FAILED;
    }
    return result.intValue();

  }

}
