package ch.ahoegger.docbox.client;

import java.util.Locale;

import org.eclipse.scout.rt.client.AbstractClientSession;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.ClientUIPreferences;

/**
 * <h3>{@link ClientSession}</h3>
 *
 * @author Andreas Hoegger
 */
public class ClientSession extends AbstractClientSession {

  public static final String PREF_USER_LOCALE = "PREF_USER_LOCALE";

  public ClientSession() {
    super(true);
  }

  /**
   * @return The {@link IClientSession} which is associated with the current thread, or <code>null</code> if not found.
   */
  public static ClientSession get() {
    return ClientSessionProvider.currentSession(ClientSession.class);
  }

  @Override
  protected void execLoadSession() {
    initializeSharedVariables();
//		// pre-load all known code types
//
//		CODES.getAllCodeTypes("ch.ahoegger.docbox.shared");

    // The locale needs to be set before the Desktop is created.
    String localeString = ClientUIPreferences.getClientPreferences(get()).get(PREF_USER_LOCALE, null);
    if (localeString != null) {
      setLocale(Locale.forLanguageTag(localeString));
    }

    setDesktop(new Desktop());
  }
}
