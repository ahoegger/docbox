/*
 * Copyright (c) BSI Business Systems Integration AG. All rights reserved.
 * http://www.bsiag.com/
 */
package ch.ahoegger.docbox.server.security;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.nls.NlsLocale;
import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.transaction.TransactionScope;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.context.ServerRunContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>{@link DatabaseUserCredentialVerifier}</h3>
 *
 * @author Andreas Hoegger
 */
public class DatabaseUserCredentialVerifier implements ICredentialVerifier {
  @SuppressWarnings("unused")
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

        if (BEANS.get(SecurityService.class).authenticate(user, password)) {
          return AUTH_OK;
        }
        return AUTH_FORBIDDEN;
      }
    });

  }
}
