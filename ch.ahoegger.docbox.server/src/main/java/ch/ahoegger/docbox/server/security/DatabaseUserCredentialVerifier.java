/*
 * Copyright (c) BSI Business Systems Integration AG. All rights reserved.
 * http://www.bsiag.com/
 */
package ch.ahoegger.docbox.server.security;

import java.io.IOException;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.nls.NlsLocale;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.commons.authentication.ICredentialVerifier;

/**
 * <h3>{@link DatabaseUserCredentialVerifier}</h3>
 *
 * @author aho
 */
public class DatabaseUserCredentialVerifier implements ICredentialVerifier {

  @Override
  public int verify(final String username, final char[] password) throws IOException {
    if (StringUtility.isNullOrEmpty(username)) {
      return AUTH_CREDENTIALS_REQUIRED;
    }

    final String user = username.toLowerCase(NlsLocale.get());

    if (BEANS.get(SecurityService.class).authenticate(user, password)) {
      return AUTH_OK;
    }
    return AUTH_FORBIDDEN;

  }
}
