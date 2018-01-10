/*
 * Copyright (c) BSI Business Systems Integration AG. All rights reserved.
 * http://www.bsiag.com/
 */
package ch.ahoegger.docbox.ui.html.security;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.config.CONFIG;
import org.eclipse.scout.rt.platform.exception.DefaultRuntimeExceptionTranslator;
import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.util.Assertions;
import org.eclipse.scout.rt.server.commons.authentication.ServletFilterHelper;
import org.eclipse.scout.rt.shared.SharedConfigProperties.BackendUrlProperty;

/**
 * <h3>{@link UserCredentialVerifierProxy}</h3>
 *
 * @author Andreas Hoegger
 */
public class UserCredentialVerifierProxy implements ICredentialVerifier {

  protected URL m_remoteAuthUrl;
  protected ICredentialVerifier m_externalPasswordVerifier;

  @PostConstruct
  protected void postConstruct() {
    m_remoteAuthUrl = getRemoteAuthUrl();
    HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
  }

  public UserCredentialVerifierProxy withExternalPasswordVerifier(final ICredentialVerifier externalPasswordVerifier) {
    m_externalPasswordVerifier = externalPasswordVerifier;
    return this;
  }

  @Override
  public int verify(final String username, final char[] password) throws IOException {
    final HttpURLConnection con = (HttpURLConnection) m_remoteAuthUrl.openConnection();
    con.setDefaultUseCaches(false);
    con.setUseCaches(false);
    con.setRequestProperty(ServletFilterHelper.HTTP_HEADER_AUTHORIZATION, BEANS.get(ServletFilterHelper.class).createBasicAuthRequest(username, password));

    switch (con.getResponseCode()) {
      case HttpServletResponse.SC_OK:
        return AUTH_OK;
      case HttpServletResponse.SC_FORBIDDEN:
      default:
        return AUTH_FORBIDDEN;
    }
  }

  protected URL getRemoteAuthUrl() {
    final RemoteAuthUrlProperty remoteAuthUrlProperty = BEANS.get(RemoteAuthUrlProperty.class);
    Assertions.assertNotNull(remoteAuthUrlProperty.getValue(), "Missing config value for remote authentication URL. [property={}]", remoteAuthUrlProperty.getKey());

    try {
      return new URL(remoteAuthUrlProperty.getValue());
    }
    catch (final MalformedURLException e) {
      throw BEANS.get(DefaultRuntimeExceptionTranslator.class).translate(e);
    }
  }

  /**
   * Property representing the remote authentication URL.
   * <p>
   * This property is based on convention over configuration, meaning that without an explicit configuration, the remote
   * authentication URL points to '{@link BackendUrlProperty}/auth'.
   */
  public static class RemoteAuthUrlProperty extends AbstractStringConfigProperty {

    public static final String KEY = "docbox.security.remote-auth-url";

    @Override
    public String getDefaultValue() {
      String base = CONFIG.getPropertyValue(BackendUrlProperty.class);
      if (base == null) {
        return null;
      }
      return base + "/auth";
    }

    @Override
    public String getKey() {
      return KEY;
    }

    @Override
    public String description() {
      // TODO
      return null;
    }
  }
}
