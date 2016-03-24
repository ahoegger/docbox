/*
 * Copyright (c) BSI Business Systems Integration AG. All rights reserved.
 * http://www.bsiag.com/
 */
package ch.ahoegger.docbox.server.security;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.nls.NlsLocale;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.commons.authentication.ICredentialVerifier;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.eclipse.scout.rt.server.transaction.TransactionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link DatabaseUserCredentialVerifier}</h3>
 *
 * @author aho
 */
public class DatabaseUserCredentialVerifier implements ICredentialVerifier {
  private static final Logger LOG = LoggerFactory.getLogger(DatabaseUserCredentialVerifier.class);

  @Override
  public int verify(final String username, final char[] password) throws IOException {
    return ServerRunContexts.copyCurrent().withTransactionScope(TransactionScope.REQUIRES_NEW).call(new Callable<Integer>() {
      @Override
      public Integer call() throws Exception {
        if (StringUtility.isNullOrEmpty(username)) {
          return AUTH_CREDENTIALS_REQUIRED;
        }

        final String user = username.toLowerCase(NlsLocale.get());

        final boolean authenticated = BEANS.get(SecurityService.class).authenticate(user, password);
        LOG.warn("Authenicator: authenicated - " + authenticated);
        if (authenticated) {
          return AUTH_OK;
        }
        return AUTH_FORBIDDEN;
      }
    });

  }
}
